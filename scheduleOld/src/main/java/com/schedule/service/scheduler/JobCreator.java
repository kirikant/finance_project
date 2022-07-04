package com.schedule.service.scheduler;

import com.schedule.entity.ScheduledOperationEntity;
import com.schedule.service.scheduler.api.IJobCreator;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.Date;

@Component
public class JobCreator implements IJobCreator {

    public  JobDetail buildJobDetail(Class jobClass, ScheduledOperationEntity scheduledOperation,
                                     String token) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("uuid", scheduledOperation.getUuid().toString());
        jobDataMap.put("token", token);

        return JobBuilder.newJob()
                .ofType(jobClass)
                .setJobData(jobDataMap)
                .withIdentity(scheduledOperation.getUuid().toString())
                .build();
    }

    public  Trigger buildTrigger(Class jobClass, ScheduledOperationEntity scheduledOperation) {
        SimpleScheduleBuilder builder = SimpleScheduleBuilder
                .simpleSchedule();

        builder = builder.repeatForever();
        builder = builder.withIntervalInMilliseconds(scheduledOperation.getScheduleEntity().getInterval() *
                scheduledOperation.getScheduleEntity().getTimeUnit().getMultiplier());

        TriggerBuilder<SimpleTrigger> triggerBuilder = TriggerBuilder
                .newTrigger()
                .withIdentity(scheduledOperation.getUuid().toString())
                .withSchedule(builder)
                .startAt(Date.from(scheduledOperation
                        .getScheduleEntity().getStartTime().toInstant(ZoneOffset.UTC)));

        if (scheduledOperation.getScheduleEntity().getStopTime() != null)
            triggerBuilder.endAt(Date.from(scheduledOperation
                    .getScheduleEntity().getStopTime().toInstant(ZoneOffset.UTC)));

        return triggerBuilder.build();
    }
}

