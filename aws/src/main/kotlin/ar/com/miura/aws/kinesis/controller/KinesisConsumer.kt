package ar.com.miura.aws.kinesis.controller

import ar.com.miura.aws.kinesis.config.KinesisConfig
import ar.com.miura.aws.kinesis.models.StockTrade
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse
import software.amazon.kinesis.common.KinesisClientUtil
import software.amazon.kinesis.coordinator.Scheduler
import software.amazon.kinesis.retrieval.polling.PollingConfig
import java.util.*

@Component
class KinesisConsumer(
    @Autowired val kinesisConfig:KinesisConfig
) {
    val logger = LoggerFactory.getLogger(javaClass)
    val gson = Gson();

    @GetMapping("/consume")
    fun consume() {

        val configsBuilder = kinesisConfig.getConfigBuilder()

        val scheduler = Scheduler(
                configsBuilder.checkpointConfig(),
                configsBuilder.coordinatorConfig(),
                configsBuilder.leaseManagementConfig(),
                configsBuilder.lifecycleConfig(),
                configsBuilder.metricsConfig(),
                configsBuilder.processorConfig(),
                configsBuilder.retrievalConfig()
        )
        var exitCode = 0
        try {
            logger.info("Starting scheduler...")
            scheduler.run()
        } catch (e: Exception) {
            logger.error("Caught throwable while processing data.", e)
            exitCode = 1
        }
        logger.info("Shutting down...")
        System.exit(exitCode)

    }

}