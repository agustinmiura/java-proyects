package ar.com.miura.aws.sqs.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class AwsConfig(
        @Value("\${aws.region}") val awsRegion: String,
        @Value("\${aws.access-key}") val accessKey: String,
        @Value("\${aws.access-secret}") val secretKey: String,
        @Value("\${sqs.url}") val sqsUrl: String,
) {

}