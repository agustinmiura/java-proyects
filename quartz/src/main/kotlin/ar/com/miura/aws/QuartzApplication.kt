package ar.com.miura.aws

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableScheduling
class QuartzApplication {}

fun main(args: Array<String>) {
    runApplication<QuartzApplication>(*args)
}
