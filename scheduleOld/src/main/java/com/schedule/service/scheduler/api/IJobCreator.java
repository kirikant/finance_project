package com.schedule.service.scheduler.api;

import com.schedule.entity.ScheduledOperationEntity;
import org.quartz.JobDetail;
import org.quartz.Trigger;

public interface IJobCreator {
    JobDetail buildJobDetail(Class jobClass, ScheduledOperationEntity scheduledOperation, String token);
    Trigger buildTrigger(Class jobClass, ScheduledOperationEntity scheduledOperation);
}
