package ar.com.miura.aws.sqs.dto

import java.time.LocalDateTime

data class MessageDto(
    val id:String,
    val message:String,
    val messageAttributes:Map<String,String>,
    val received: LocalDateTime
) {
}