package com.schedule.service.scheduler.api;

import com.schedule.entity.ScheduledOperationEntity;
import org.quartz.Job;

public interface ISchedulerJobService {
    <T extends Job> void schedule(Class<T> jobClass, ScheduledOperationEntity scheduledOperation);
}
