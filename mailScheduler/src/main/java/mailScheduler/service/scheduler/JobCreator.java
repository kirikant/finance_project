package mailScheduler.service.scheduler;

import mailScheduler.dto.ScheduledReportDto;
import mailScheduler.entity.ScheduledReportEntity;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.Date;

@Component
public class JobCreator {

    public  JobDetail buildJobDetail(Class jobClass, ScheduledReportEntity scheduledReportEntity,
                                     String token) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("uuid", scheduledReportEntity.getUuid().toString());
        jobDataMap.put("token", token);

        return JobBuilder.newJob()
                .ofType(jobClass)
                .setJobData(jobDataMap)
                .withIdentity(scheduledReportEntity.getUuid().toString())
                .build();
    }

    public  Trigger buildTrigger(Class jobClass, ScheduledReportEntity scheduledReportEntity) {
        SimpleScheduleBuilder builder = SimpleScheduleBuilder
                .simpleSchedule();

        builder = builder.repeatForever();
        builder = builder.withIntervalInMilliseconds(scheduledReportEntity.getScheduleEntity().getInterval() *
                scheduledReportEntity.getScheduleEntity().getTimeUnit().getMultiplier());

        TriggerBuilder<SimpleTrigger> triggerBuilder = TriggerBuilder
                .newTrigger()
                .withIdentity(scheduledReportEntity.getUuid().toString())
                .withSchedule(builder)
                .startAt(Date.from(scheduledReportEntity
                        .getScheduleEntity().getStartTime().toInstant(ZoneOffset.UTC)));

        if (scheduledReportEntity.getScheduleEntity().getStopTime() != null)
            triggerBuilder.endAt(Date.from(scheduledReportEntity
                    .getScheduleEntity().getStopTime().toInstant(ZoneOffset.UTC)));

        return triggerBuilder.build();
    }
}

