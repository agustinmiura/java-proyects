package ar.com.miura.aws.kinesis

import ar.com.miura.aws.kinesis.models.StockTrade
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse
import software.amazon.kinesis.common.KinesisClientUtil
import java.util.*

class MessageProducer {
    val logger = LoggerFactory.getLogger(javaClass)

    fun sendMessage(message: StockTrade) {

    }
}