package ar.com.miura.aws.kinesis.config

import ar.com.miura.aws.kinesis.CustomShardProcessorFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient
import software.amazon.kinesis.common.ConfigsBuilder
import software.amazon.kinesis.common.KinesisClientUtil
import java.util.*

@Component
class KinesisConfig(
        @Value("\${aws.stream-name}") val streamName: String,
        @Value("\${aws.region}") val awsRegion: String,
        @Value("\${aws.access-key}") val accessKey: String,
        @Value("\${aws.access-secret}") val secretKey: String,
) {

    private var kinesisClient:KinesisAsyncClient? = null

    init {
        val region: Region = Region.of(awsRegion)
        kinesisClient = KinesisClientUtil.createKinesisAsyncClient(
        KinesisAsyncClient
                .builder()
                .region(region)
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
                ));
    }

    fun getConfigBuilder(): ConfigsBuilder {
        val streamName = streamName
        val region: Region = Region.of(awsRegion)
        val dynamoClient = DynamoDbAsyncClient.builder().region(region).credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
        ).build()

        val cloudWatchClient = CloudWatchAsyncClient.builder().region(Region.of(awsRegion)).credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
        ).build()

        return ConfigsBuilder(streamName, streamName, kinesisClient!!, dynamoClient, cloudWatchClient, UUID.randomUUID().toString(), CustomShardProcessorFactory())
    }

}