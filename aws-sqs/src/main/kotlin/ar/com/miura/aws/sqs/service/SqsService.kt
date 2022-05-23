package ar.com.miura.aws.sqs.service

import ar.com.miura.aws.sqs.config.AwsConfig
import ar.com.miura.aws.sqs.dto.InputMessageDto
import ar.com.miura.aws.sqs.dto.MessageDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.*
import java.time.LocalDateTime
import java.util.random.RandomGenerator

@Service
class SqsService(@Autowired val awsConfig: AwsConfig) {

    val logger = LoggerFactory.getLogger(javaClass)

    private val customAttributesName = listOf<String>("customAttribute1", "customAttribute2");

    fun sendMessage(message: InputMessageDto): MessageDto {
        try {
            val sqsClient = getSqsClient()

            val messageAttributes: Map<String, MessageAttributeValue> = customAttributesName.map {
                it to MessageAttributeValue.builder().dataType("String")
                    .stringValue(getRandomString()).build()
            }.toMap()

            val messageRequest: SendMessageRequest = SendMessageRequest.builder()
                .messageBody(message.body)
                .queueUrl(awsConfig.sqsUrl).messageAttributes(messageAttributes).build();

            val response: SendMessageResponse = sqsClient.sendMessage(messageRequest);

            logger.debug(" I see the id : " + response.messageId());

            return MessageDto(
                response.messageId(),
                message.body,
                messageAttributes.map { (key, value) -> key.lowercase() to value.stringValue() }
                    .toMap(),
                LocalDateTime.now()
            );

        } catch (e: Exception) {
            throw e;
        }
    }

    fun receiveMessages(maxMessages: Int = 10): List<MessageDto> {
        try {

            if (maxMessages <= 0 || maxMessages >= 11) {
                throw IllegalArgumentException(" Max messages between 1 and 10 ")
            }

            val messages = mutableListOf<MessageDto>()
            val client = getSqsClient();
            val receiveMessageRequest = ReceiveMessageRequest.builder().queueUrl(awsConfig.sqsUrl)
                .maxNumberOfMessages(maxMessages).messageAttributeNames(customAttributesName)
                .build();
            val receivedMessages = client.receiveMessage(receiveMessageRequest)
            val date = LocalDateTime.now();
            receivedMessages.messages().forEach {
                var attributes = if (it.hasMessageAttributes()) {
                    it.messageAttributes()
                        .map { (key, value) -> key.lowercase() to value.stringValue() }.toMap()
                } else {
                    mapOf();
                }
                messages.add(MessageDto(it.messageId(), it.body(), attributes, date))
            };

            receivedMessages.messages().forEach {
                val deleteMessageRequest: DeleteMessageRequest = DeleteMessageRequest.builder()
                    .queueUrl(awsConfig.sqsUrl)
                    .receiptHandle(it.receiptHandle())
                    .build()
                client.deleteMessage(deleteMessageRequest);
            };

            return messages;
        } catch (e: Exception) {
            logger.error(" Error receiving messages ", e);
            return listOf();
        }
    }

    private fun getSqsClient(): SqsClient {
        val region = Region.of(awsConfig.awsRegion);
        val sqsClient = SqsClient.builder().region(region).credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    awsConfig.accessKey,
                    awsConfig.secretKey
                )
            )
        ).build();
        return sqsClient
    }

    private fun getRandomString(): String {
        return RandomGenerator.getDefault().nextInt().toString();
    }


}