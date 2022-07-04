package com.accounts.rest;

import com.accounts.dto.OperationDto;
import com.accounts.exception.ValidationChecker;
import com.accounts.exception.ValidationException;
import com.accounts.service.OperationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class OperationsController {

    private final OperationService operationService;
    private final ValidationChecker validationChecker;

    public OperationsController(OperationService operationService, ValidationChecker validationChecker) {
        this.operationService = operationService;
        this.validationChecker = validationChecker;
    }

    @PostMapping("/account/{uuid}/operation")
    public ResponseEntity<?> addOperation(@PathVariable UUID uuid,
                                          @RequestBody OperationDto operationDto,
                                          @RequestHeader("Authorization") String token)
            throws ValidationException, EntityNotFoundException {
        validationChecker.checkOperationDtoFields(operationDto);
        operationService.addOperation(uuid, operationDto, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/account/{uuid}/operation")
    public ResponseEntity<?> getOperations(@PathVariable UUID uuid,
                                           @RequestParam Integer page,
                                           @RequestParam Integer size,
                                           @RequestHeader("Authorization") String token)
            throws ValidationException, EntityNotFoundException {
        validationChecker.checkPageParams(page, size);
        Page<OperationDto> operations = operationService.getOperations(uuid, page, size, token);
        return ResponseEntity.ok().body(operations);
    }

    @PutMapping("/account/{uuid}/operation/{uuid_operation}/dt_update/{dt_update}")
    public ResponseEntity<?> updateOperation(@PathVariable UUID uuid,
                                             @PathVariable(name = "uuid_operation") UUID uuidOperation,
                                             @PathVariable(name = "dt_update") Long dtUpdate,
                                             @RequestBody OperationDto operationDto,
                                             @RequestHeader("Authorization") String token)
            throws ValidationException, EntityNotFoundException {
        validationChecker.checkOperationDtoFields(operationDto);
        operationService.updateOperation(uuid, uuidOperation, dtUpdate, operationDto, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/account/{uuid}/operation/{uuid_operation}/dt_update/{dt_update}")
    public ResponseEntity<?> deleteOperation(@PathVariable UUID uuid,
                                             @PathVariable(name = "uuid_operation") UUID uuidOperation,
                                             @PathVariable(name = "dt_update") Long dtUpdate,
                                             @RequestHeader("Authorization") String token) {
        operationService.deleteOperation(uuidOperation, token);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/account/{uuid}/operation/sorted")
    public ResponseEntity<?> getFilteredOperations(@PathVariable UUID uuid,
                                                   @RequestBody List<UUID> categories,
                                                   @RequestHeader("Authorization") String token)
            throws ValidationException, EntityNotFoundException {
        List<OperationDto> sortedOperations = operationService.getSortedOperations(uuid, categories,token);
        return ResponseEntity.ok().body(sortedOperations);
    }

    @PostMapping("/account/{uuid}/operation/by-date")
    public ResponseEntity<?> getByDateOperations(@PathVariable UUID uuid,
                                                   @RequestBody List<LocalDateTime> dates,
                                                   @RequestHeader("Authorization") String token)
            throws ValidationException, EntityNotFoundException {
        List<OperationDto> byDateOperations = operationService.getByDateOperations(uuid, dates, token);
        return ResponseEntity.ok().body(byDateOperations);
    }

    @GetMapping("/operation/{uuid}")
    public ResponseEntity<?> getOperation(@PathVariable UUID uuid,
                                           @RequestHeader("Authorization") String token)
            throws ValidationException, EntityNotFoundException {
        return ResponseEntity.ok().body(operationService.getOperation(uuid,token));
    }


}
