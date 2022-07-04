package com.schedule.service.api;

import com.schedule.dto.ScheduledOperationDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IScheduledOperationService {
    void add(ScheduledOperationDto scheduledOperationDto, String token);
    Page<ScheduledOperationDto> getAll(Integer page, Integer size, UUID accountUUID, String token);
    void update(UUID uuid,Long lastUpdate,ScheduledOperationDto scheduledOperationDto, String username);
}
