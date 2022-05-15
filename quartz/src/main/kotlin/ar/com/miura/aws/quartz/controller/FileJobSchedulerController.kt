package ar.com.miura.aws.quartz.controller

import ar.com.miura.aws.quartz.dto.SchedulRequest
import ar.com.miura.aws.quartz.dto.ScheduleJobResponse
import ar.com.miura.aws.quartz.job.FileJob
import org.quartz.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*


@RestController
class FileJobSchedulerController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    private var scheduler: Scheduler? = null

    @PostMapping("/schedulejob")
    fun createSchedule(@RequestBody schedulRequest: SchedulRequest): ResponseEntity<ScheduleJobResponse> {
        try {

            val dateTime: ZonedDateTime = ZonedDateTime.of(
                schedulRequest.dateTime,
                schedulRequest.timeZone
            )

            if (dateTime.isBefore(ZonedDateTime.now())) {
                val scheduleJobResponse = ScheduleJobResponse(
                    false,
                    "dateTime must be after current time"
                )
                return ResponseEntity.badRequest().body(scheduleJobResponse)
            }

            val jobDetail: JobDetail = buildJobDetail(schedulRequest)
            val trigger: Trigger = buildJobTrigger(jobDetail, dateTime)
            scheduler!!.scheduleJob(jobDetail, trigger)

            val scheduleJobResponse = ScheduleJobResponse(
                true,
                jobDetail.getKey().getName(),
                jobDetail.getKey().getGroup(),
                "Email Scheduled Successfully!"
            )
            return ResponseEntity.ok().body(ScheduleJobResponse(true, "message"))
        } catch (e: Exception) {
            logger.error(" Error ", e);
            return ResponseEntity.internalServerError()
                .body(ScheduleJobResponse(false, "message"))
        }
    }

    private fun buildJobDetail(schedulRequest: SchedulRequest): JobDetail {
        val jobDataMap = JobDataMap()
        jobDataMap.put("dateRequested", LocalDateTime.now())
        return JobBuilder.newJob(FileJob::class.java)
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