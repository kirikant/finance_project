package com.accounts.service.api;

import com.accounts.dto.OperationDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IOperationService {

    void addOperation(UUID uuid, OperationDto operationDto, String token);
    Page<OperationDto> getOperations(UUID uuid, Integer page, Integer size, String token);
    void updateOperation(UUID uuid,UUID uuidOperation,Long dtUpdate,OperationDto operationDto, String token);
    void deleteOperation(UUID uuidOperation, String token);
}
