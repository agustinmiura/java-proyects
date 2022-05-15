package ar.com.miura.aws.quartz.dto

import java.time.LocalDateTime
import java.time.ZoneId

data class ScheduleEmailRequest(
    val email: String,
    val subject: String,
    val body: String,
    val dateTime: LocalDateTime,
    val timeZone: ZoneId,
) {
}