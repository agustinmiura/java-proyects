package ar.com.miura.aws.quartz.job

import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
class EmailJob : QuartzJobBean() {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var mockJob: MockJob

    override fun executeInternal(jobExecutionContext: JobExecutionContext) {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey())
        mockJob.writeFile();
        logger.info(" Sending the mail ")
    }

}