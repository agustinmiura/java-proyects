package ar.com.miura.aws.quartz.dto

import java.time.LocalDateTime
import java.time.ZoneId

data class SchedulRequest(
    val dateTime: LocalDateTime,
    val timeZone: ZoneId,
) {
}