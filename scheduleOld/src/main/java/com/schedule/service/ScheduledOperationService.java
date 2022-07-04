package com.schedule.service;

import com.schedule.dto.OperationDto;
import com.schedule.dto.ScheduleDto;
import com.schedule.dto.ScheduledOperationDto;
import com.schedule.entity.OperationEntity;
import com.schedule.entity.ScheduleEntity;
import com.schedule.entity.ScheduledOperationEntity;
import com.schedule.repostories.OperationRepo;
import com.schedule.repostories.ScheduleRepo;
import com.schedule.repostories.ScheduledOperationRepo;
import com.schedule.security.JwtTokenProvider;
import com.schedule.service.api.IScheduledOperationService;
import com.schedule.service.scheduler.CrossServiceGetter;
import com.schedule.service.scheduler.ScheduleJob;
import com.schedule.service.scheduler.SchedulerJobService;
import com.schedule.utils.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ScheduledOperationService implements IScheduledOperationService {

    private final ScheduledOperationRepo scheduledOperationRepo;
    private final OperationRepo operationRepo;
    private final Mapper mapper;
    private final ScheduleRepo scheduleRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final SchedulerJobService schedulerJobService;
    private final OperationService operationService;
    private final ScheduleService scheduleService;
    private final CrossServiceGetter crossServiceGetter;

    public ScheduledOperationService(ScheduledOperationRepo scheduledOperationRepo,
                                     OperationRepo operationRepo, Mapper mapper,
                                     ScheduleRepo scheduleRepo,
                                     JwtTokenProvider jwtTokenProvider, SchedulerJobService schedulerJobService, OperationService operationService, ScheduleService scheduleService, CrossServiceGetter crossServiceGetter) {
        this.scheduledOperationRepo = scheduledOperationRepo;
        this.operationRepo = operationRepo;
        this.mapper = mapper;
        this.scheduleRepo = scheduleRepo;
        this.jwtTokenProvider = jwtTokenProvider;
        this.schedulerJobService = schedulerJobService;
        this.operationService = operationService;
        this.scheduleService = scheduleService;
        this.crossServiceGetter = crossServiceGetter;
    }

    @Transactional
    public void add(ScheduledOperationDto scheduledOperationDto, String token) throws EntityNotFoundException{

        String username = jwtTokenProvider.getUsername(token);
        crossServiceGetter.checkAccountPermission(scheduledOperationDto.getOperationDto().getAccount(),token);
        ScheduledOperationEntity scheduledOperation = mapper.map(scheduledOperationDto, ScheduledOperationEntity.class);
        LocalDateTime now = LocalDateTime.now();

        OperationEntity savedOperationEntity = operationService.add(scheduledOperationDto, now);
        ScheduleEntity savedScheduleEntity = scheduleService.add(scheduledOperationDto, now);

        scheduledOperation.setDtCreate(now);
        scheduledOperation.setOperationEntity(savedOperationEntity);
        scheduledOperation.setScheduleEntity(savedScheduleEntity);

        scheduledOperation.setUser(username);

        ScheduledOperationEntity savedScheduledOperationEntity = scheduledOperationRepo
                .saveAndFlush(scheduledOperation);
       schedulerJobService.schedule(ScheduleJob.class,savedScheduledOperationEntity,token);
    }


    public Page<ScheduledOperationDto> getAll(Integer page, Integer size, UUID accountUUID, String token) throws EntityNotFoundException{
        String username = jwtTokenProvider.getUsername(token);
        crossServiceGetter.checkAccountPermission(accountUUID,token);
        return scheduledOperationRepo.findAllByUserAndOperationEntity_AccountUuid(username,accountUUID, PageRequest
                .of(page,size, Sort.Direction.DESC,"dtCreate"))
                .orElseThrow(()->new EntityNotFoundException("There is no such account"))
                .map((x)->{
                    ScheduledOperationDto scheduledOperationDto = mapper
                            .map(x, ScheduledOperationDto.class);

                    scheduledOperationDto.setOperationDto( mapper.map(x.getOperationEntity(), OperationDto.class));
                    scheduledOperationDto.setScheduleDto(mapper.map(x.getScheduleEntity(),ScheduleDto.class));

                    return  scheduledOperationDto;
                });
    }

    @Transactional
    public void update(UUID uuid,Long lastUpdate,
                       ScheduledOperationDto scheduledOperationDto,
                       String token){
        String username = jwtTokenProvider.getUsername(token);
        ScheduledOperationEntity scheduledOperation = scheduledOperationRepo
                .findByUuidAndDtUpdateAndUser(uuid, LocalDateTime
                .ofInstant(Instant.ofEpochMilli(lastUpdate), ZoneOffset.UTC),username);

        ScheduleEntity updatedScheduleEntity = scheduleService.update(scheduledOperation,
                scheduledOperationDto);
        OperationEntity updatedOperationEntity = operationService.update(scheduledOperation,
                scheduledOperationDto);

        scheduledOperation.setScheduleEntity(updatedScheduleEntity);
        scheduledOperation.setOperationEntity(updatedOperationEntity);

        scheduledOperationRepo.saveAndFlush(scheduledOperation);
    }
}
