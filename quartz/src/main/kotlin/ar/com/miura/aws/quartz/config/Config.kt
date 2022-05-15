package ar.com.miura.aws.quartz.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class Config(
    @Value("\${app.directory}") val appDirectory: String,
) {
}