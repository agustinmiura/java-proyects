package ar.com.miura.aws.dynamo.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.kinesis.common.ConfigsBuilder

@Component
data class AwsConfig(
        @Value("\${aws.region}") val awsRegion: String,
        @Value("\${aws.access-key}") val accessKey: String,
        @Value("\${aws.access-secret}") val secretKey: String,
        @Value("\${dynamo.table}") val dynamoTable: String,
) {

}