package com.schedule.service;

import com.schedule.dto.ScheduleDto;
import com.schedule.dto.ScheduledOperationDto;
import com.schedule.entity.ScheduleEntity;
import com.schedule.entity.ScheduledOperationEntity;
import com.schedule.entity.SimpleTrigger;
import com.schedule.entity.TriggerEntity;
import com.schedule.repostories.ScheduleRepo;
import com.schedule.repostories.SimpleTriggerRepo;
import com.schedule.repostories.TriggerRepo;
import com.schedule.utils.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
@Transactional(readOnly = true)
public class ScheduleService  {

    private final Mapper mapper;
    private final ScheduleRepo scheduleRepo;
    private final TriggerRepo triggerRepo;
    private final SimpleTriggerRepo simpleTriggerRepo;

    public ScheduleService(Mapper mapper, ScheduleRepo scheduleRepo,
                           TriggerRepo triggerRepo, SimpleTriggerRepo simpleTriggerRepo) {
        this.mapper = mapper;
        this.scheduleRepo = scheduleRepo;
        this.triggerRepo = triggerRepo;
        this.simpleTriggerRepo = simpleTriggerRepo;
    }

    @Transactional
    public ScheduleEntity add(ScheduledOperationDto scheduledOperationDto, LocalDateTime now){
        ScheduleEntity scheduleEntity = mapper.map(scheduledOperationDto.getScheduleDto(), ScheduleEntity.class);
        scheduleEntity.setDtCreate(now);
        return scheduleRepo.save(scheduleEntity);
    }

    @Transactional
    public ScheduleEntity update(ScheduledOperationEntity scheduledOperation,
                                 ScheduledOperationDto scheduledOperationDto
                                 ){
        ScheduleDto scheduleDto = scheduledOperationDto.getScheduleDto();

        TriggerEntity  trigger = triggerRepo.findById(scheduledOperation.getUuid().toString())
                .orElseThrow(() -> new EntityNotFoundException("There is no such trigger"));
        trigger.setNextFireTime(scheduleDto.getStartTime()
                .toInstant(ZoneOffset.UTC).toEpochMilli());
        trigger.setStartTime(scheduleDto.getStartTime()
                .toInstant(ZoneOffset.UTC).toEpochMilli());
        trigger.setEndTime(scheduleDto.getStopTime()
                .toInstant(ZoneOffset.UTC).toEpochMilli());

        SimpleTrigger simpleTrigger= simpleTriggerRepo.findById(scheduledOperation.getUuid().toString())
                .orElseThrow(() -> new EntityNotFoundException("There is no such trigger"));
        simpleTrigger.setRepeatInterval(scheduleDto.getInterval()*scheduleDto.getTimeUnit().getMultiplier());

        ScheduleEntity scheduleEntity = scheduledOperation.getScheduleEntity();
        scheduleEntity.setInterval(scheduleDto.getInterval());
        scheduleEntity.setStartTime(scheduleDto.getStartTime());
        scheduleEntity.setTimeUnit(scheduleDto.getTimeUnit());
        return scheduleRepo.save(scheduleEntity);
    }
}
