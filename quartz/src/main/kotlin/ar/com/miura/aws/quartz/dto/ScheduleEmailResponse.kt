package ar.com.miura.aws.quartz.dto

data class ScheduleEmailResponse(
    var success: Boolean,
    var jobId: String,
    var jobGroup: String,
    var message: String
) {
    constructor(success: Boolean, message: String) : this(success, "jobId", "jobGroup", message);
}