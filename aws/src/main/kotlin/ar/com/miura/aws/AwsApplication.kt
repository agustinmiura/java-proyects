package ar.com.miura.aws

import ar.com.miura.aws.kinesis.config.KinesisConfig
import ar.com.miura.aws.kinesis.controller.KinesisConsumer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
class AwsApplication : CommandLineRunner {

	val logger = LoggerFactory.getLogger(javaClass)


	@Autowired
	private val context: ApplicationContext? = null

	override fun run(vararg args: String?) {
		try {
			val kinesisConfig = context!!.getBean(KinesisConfig::class.java);
			val consumer = KinesisConsumer(kinesisConfig);
			consumer.consume()
		} catch (e: Exception) {
			logger.error("Error", e)
		}
	}
}

fun main(args: Array<String>) {
	try {
		runApplication<AwsApplication>(*args)
	} catch (e: Exception) {
		e.printStackTrace()
	}

}
