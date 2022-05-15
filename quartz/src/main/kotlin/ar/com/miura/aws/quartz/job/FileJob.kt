package ar.com.miura.aws.quartz.job

import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
class FileJob : QuartzJobBean() {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var fileWriter: FileWriter

    override fun executeInternal(jobExecutionContext: JobExecutionContext) {
        logger.debug("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey())
        fileWriter.writeFile();
        logger.debug(" Sending the mail ")
    }

}