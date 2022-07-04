package com.schedule.service.scheduler;

import com.schedule.entity.ScheduledOperationEntity;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SchedulerJobService {

   private final Scheduler scheduler;
   private final JobCreator jobCreator;
   private Logger logger = LoggerFactory.getLogger(CrossServiceGetter.class);


    public SchedulerJobService(Scheduler scheduler, JobCreator jobCreator) {
        this.scheduler = scheduler;
        this.jobCreator = jobCreator;
    }

    public <T extends Job> void schedule(Class<T> jobClass, ScheduledOperationEntity scheduledOperation,
                                         String token) {
       JobDetail jobDetail = jobCreator.buildJobDetail(jobClass,scheduledOperation, token);
       Trigger trigger = jobCreator.buildTrigger(jobClass, scheduledOperation);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            logger.error(e.getCause().toString());
        }

    }
}
