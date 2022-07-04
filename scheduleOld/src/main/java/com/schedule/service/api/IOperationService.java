package com.schedule.service.api;

import com.schedule.dto.ScheduledOperationDto;
import com.schedule.entity.OperationEntity;
import com.schedule.entity.ScheduledOperationEntity;

import java.time.LocalDateTime;

public interface IOperationService {
    OperationEntity add(ScheduledOperationDto scheduledOperationDto, LocalDateTime now);
    OperationEntity update(ScheduledOperationEntity scheduledOperation,
                           ScheduledOperationDto scheduledOperationDto);
}
