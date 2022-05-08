package ar.com.miura.aws.dynamo.service

import ar.com.miura.aws.dynamo.config.AwsConfig
import ar.com.miura.aws.dynamo.dto.MovieDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*


@Service
class MovieService(@Autowired val awsConfig: AwsConfig) {

    val logger = LoggerFactory.getLogger(javaClass)

    fun getItem(key:String):Map<String, Any>? {
        val dynamoClient = getClient()

        val keyToGet = HashMap<String, AttributeValue>()
        keyToGet["id"] = AttributeValue.builder().s(key).build()
        val request: GetItemRequest = GetItemRequest.builder()
            .key(keyToGet)
            .tableName(awsConfig.dynamoTable)
            .build()

        var found:MutableMap<String, Any>? = null;
        try {
            val returnedItem: Map<String, AttributeValue> = dynamoClient.getItem(request).item()
            if (!returnedItem.isNullOrEmpty()) {
                val id = key;
                val genre = returnedItem.get("genre")!!.s()
                val name = returnedItem.get("name")!!.s()
                val year = returnedItem.get("year")!!.n()
                found = mutableMapOf()
                found.put("genre", genre);
                found.put("name", name);
                found.put("year", year);
                found.put("id", id);
            }
        } catch (e: DynamoDbException) {
           throw e;
        }
        return found;
    }

    fun create(movie: MovieDto) {
        val client = getClient();

        val itemValues = HashMap<String, AttributeValue>()
        itemValues.put("id", AttributeValue.builder().s(movie.id).build());
        itemValues.put("year", AttributeValue.builder().s(movie.year.toString()).build());
        itemValues.put("name", AttributeValue.builder().s(movie.name).build());
        itemValues.put("genre", AttributeValue.builder().s(movie.genre).build());

        val request: PutItemRequest = PutItemRequest.builder()
            .tableName(awsConfig.dynamoTable)
            .item(itemValues)
            .build()

        try {
            client.putItem(request);
        }catch(e:Exception) {
            throw e;
        }

    }

    fun delete(id: String) {

        val client = getClient();

        val keyToGet = HashMap<String, AttributeValue>()

        keyToGet.put("id", AttributeValue.builder()
            .s(id)
            .build());

        val deleteReq: DeleteItemRequest = DeleteItemRequest.builder()
            .tableName(awsConfig.dynamoTable)
            .key(keyToGet)
            .build()

        try {
            client.deleteItem(deleteReq)
        } catch (e: DynamoDbException) {
            throw e;
        }

    }

    fun update(id: String, movie: MovieDto) {
        val client = getClient();

        val itemKey = HashMap<String, AttributeValue>()
        itemKey["id"] = AttributeValue.builder().s(id).build()

        val updatedValues = HashMap<String, AttributeValueUpdate>()

        updatedValues.put("genre", AttributeValueUpdate.builder()
            .value(AttributeValue.builder().s(movie.genre).build())
            .action(AttributeAction.PUT)
            .build());
        updatedValues.put("name", AttributeValueUpdate.builder()
            .value(AttributeValue.builder().s(movie.name).build())
            .action(AttributeAction.PUT)
            .build());
        updatedValues.put("year", AttributeValueUpdate.builder()
            .value(AttributeValue.builder().s(movie.year.toString()).build())
            .action(AttributeAction.PUT)
            .build());

        val request = UpdateItemRequest.builder()
            .tableName(awsConfig.dynamoTable)
            .key(itemKey)
            .attributeUpdates(updatedValues)
            .build()

        try {
            client.updateItem(request)
        } catch (e: ResourceNotFoundException) {
            throw e;
        } catch (e: DynamoDbException) {
            throw e;
        }

    }

    private fun getClient(): DynamoDbClient {
        val region: Region = Region.of(awsConfig.awsRegion)
        val dynamoClient = DynamoDbClient.builder().region(region).credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    awsConfig.accessKey,
                    awsConfig.secretKey
                )
            )
        ).build()
        return dynamoClient
    }




}