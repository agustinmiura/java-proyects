package ar.com.miura.aws.quartz.controller

import ar.com.miura.aws.quartz.dto.ScheduleEmailRequest
import ar.com.miura.aws.quartz.dto.ScheduleEmailResponse
import ar.com.miura.aws.quartz.job.EmailJob
import org.quartz.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*
import java.util.random.RandomGenerator


@RestController
class EmailJobSchedulerController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    private var scheduler: Scheduler? = null

    @PostMapping("/schedulejob")
    fun createSchedule(@RequestBody scheduleEmailRequest: ScheduleEmailRequest): ResponseEntity<ScheduleEmailResponse> {
        try {

            val dateTime: ZonedDateTime = ZonedDateTime.of(
                scheduleEmailRequest.dateTime,
                scheduleEmailRequest.timeZone
            )

            if (dateTime.isBefore(ZonedDateTime.now())) {
                val scheduleEmailResponse = ScheduleEmailResponse(
                    false,
                    "dateTime must be after current time"
                )
                return ResponseEntity.badRequest().body(scheduleEmailResponse)
            }

            val jobDetail: JobDetail = buildJobDetail(scheduleEmailRequest)
            val trigger: Trigger = buildJobTrigger(jobDetail, dateTime)
            scheduler!!.scheduleJob(jobDetail, trigger)

            val scheduleEmailResponse = ScheduleEmailResponse(
                true,
                jobDetail.getKey().getName(),
                jobDetail.getKey().getGroup(),
                "Email Scheduled Successfully!"
            )
            return ResponseEntity.ok().body(ScheduleEmailResponse(true, "message"))
        } catch (e: Exception) {
            logger.error(" Error ", e);
            return ResponseEntity.internalServerError()
                .body(ScheduleEmailResponse(false, "message"))
        }
    }

    private fun buildJobDetail(scheduleEmailRequest: ScheduleEmailRequest): JobDetail {
        val jobDataMap = JobDataMap()
        jobDataMap.put("email", scheduleEmailRequest.email)
        jobDataMap.put("subject", scheduleEmailRequest.subject)
        jobDataMap.put("body", scheduleEmailRequest.body)
        return JobBuilder.newJob(EmailJob::class.java)
            .withIdentity(UUID.randomUUID().toString(), "email-jobs")
            .withDescription("Send Email Job")
            .usingJobData(jobDataMap)
            .storeDurably()
            .build()
    }

    private fun buildJobTrigger(jobDetail: JobDetail, startAt: ZonedDateTime): Trigger {
        return TriggerBuilder.newTrigger().forJob(jobDetail)
            .withIdentity(jobDetail.key.name, "email-triggers").startNow().build();
    }
}