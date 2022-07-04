package com.schedule.service;

import com.schedule.dto.OperationDto;
import com.schedule.dto.ScheduledOperationDto;
import com.schedule.entity.OperationEntity;
import com.schedule.entity.ScheduledOperationEntity;
import com.schedule.repostories.OperationRepo;
import com.schedule.service.api.IOperationService;
import com.schedule.utils.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class OperationService implements IOperationService {

    private final OperationRepo operationRepo;
    private final Mapper mapper;


    public OperationService(OperationRepo operationRepo, Mapper mapper) {
        this.operationRepo = operationRepo;
        this.mapper = mapper;
    }

    @Transactional
    public OperationEntity add(ScheduledOperationDto scheduledOperationDto, LocalDateTime now) {
        OperationDto operationDto = scheduledOperationDto.getOperationDto();
        OperationEntity operationEntity = mapper.map(operationDto, OperationEntity.class);
        operationEntity.setDtCreate(now);
        return operationRepo.save(operationEntity);
    }

    @Transactional
    public OperationEntity update(ScheduledOperationEntity scheduledOperation,
                                  ScheduledOperationDto scheduledOperationDto
                                  ) {

        OperationDto operationDto = scheduledOperationDto.getOperationDto();
        OperationEntity operationEntity = scheduledOperation.getOperationEntity();
        operationEntity.setDescription(operationDto.getDescription());
        operationEntity.setValue(operationDto.getValue());
        operationEntity.setCurrencyUuid(operationDto.getCurrency());
        operationEntity.setAccountUuid(operationDto.getAccount());

        return operationRepo.save(operationEntity);
    }


}
