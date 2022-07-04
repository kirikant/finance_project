package com.schedule.service.api;

import com.schedule.dto.ScheduledOperationDto;
import com.schedule.entity.ScheduleEntity;
import com.schedule.entity.ScheduledOperationEntity;

import java.time.LocalDateTime;

public interface IScheduleService {
    ScheduleEntity add(ScheduledOperationDto scheduledOperationDto, LocalDateTime now);
    ScheduleEntity update(ScheduledOperationEntity scheduledOperation,
                          ScheduledOperationDto scheduledOperationDto);
}
