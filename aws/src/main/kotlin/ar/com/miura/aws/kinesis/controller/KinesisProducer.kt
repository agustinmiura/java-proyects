package ar.com.miura.aws.kinesis.controller

import ar.com.miura.aws.kinesis.config.KinesisConfig
import ar.com.miura.aws.kinesis.models.StockTrade
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse
import software.amazon.kinesis.common.KinesisClientUtil
import java.util.*

@Component
class KinesisProducer(@Autowired val kinesisConfig: KinesisConfig) {

    val logger = LoggerFactory.getLogger(javaClass)
    val gson = Gson();

    private fun sendStockTrade(trade: StockTrade, kinesisClient: KinesisAsyncClient, streamName: String) {
        var asString = trade.asString();
        asString = gson.toJson(trade)

        logger.info(" I see the json entity : ${asString} ")

        val bytes: ByteArray = asString.toByteArray();

        if (bytes.size == 0) {
            logger.error(" Error with the stream ")
            return;
        }

        logger.info(" Putting trade : ${asString} ")

        val putRecordRequest = PutRecordRequest
                .builder()
                .partitionKey(trade.tickerSymbol)
                .streamName(streamName)
                .data(SdkBytes.fromByteArray(bytes))
                .build()
        val response: PutRecordResponse = kinesisClient.putRecord(putRecordRequest).get()
        logger.info("The response is : ${response} ")
    }

    fun send() {
        val randomId = UUID.randomUUID().toString()
        val trade = StockTrade(
                "XOM",
                "BUY",
                10.1,
                1000.0,
                randomId
        )
        val region: Region = Region.of(kinesisConfig.awsRegion)

        val kinesisClient = KinesisClientUtil.createKinesisAsyncClient(
                KinesisAsyncClient
                        .builder()
                        .region(region)
                        .credentialsProvider(
                                StaticCredentialsProvider.create(AwsBasicCredentials.create(kinesisConfig.accessKey, kinesisConfig.secretKey))
                        )

        );
        val response = sendStockTrade(trade, kinesisClient, kinesisConfig.streamName)
        logger.info("The response is ${response}")
    }
}


