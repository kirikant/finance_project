package com.schedule.rest;

import com.schedule.dto.ScheduledOperationDto;
import com.schedule.exception.ValidationChecker;
import com.schedule.exception.ValidationException;
import com.schedule.service.ScheduledOperationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@RestController
@RequestMapping("/scheduler")
public class ScheduledOperationController {

    private final ScheduledOperationService scheduledOperationService;
    private final ValidationChecker validationChecker;

    public ScheduledOperationController(ScheduledOperationService scheduledOperationService, ValidationChecker validationChecker) {
        this.scheduledOperationService = scheduledOperationService;
        this.validationChecker = validationChecker;
    }

    @PostMapping("/operation")
    public ResponseEntity<?> addScheduledOperation(@RequestBody ScheduledOperationDto scheduledOperationDto,
                                                   @RequestHeader("Authorization") String token)
            throws ValidationException {
        validationChecker.checkScheduledOperationDtoFields(scheduledOperationDto);
        scheduledOperationService.add(scheduledOperationDto,token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/operation/{account_uuid}")
    public ResponseEntity<?> getScheduledOperation(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @PathVariable(name = "account_uuid") UUID uuid,
            @RequestHeader("Authorization") String token) throws EntityNotFoundException, ValidationException {
        validationChecker.checkPageParams(page, size);
        Page<ScheduledOperationDto> all = scheduledOperationService.getAll(page, size, uuid, token);
        return ResponseEntity.ok(all);
    }

    @PutMapping("/operation/{uuid}/dt_update/{dt_update}")
    public ResponseEntity<?> updateScheduledOperation(
            @PathVariable UUID uuid,
            @PathVariable(name = "dt_update") Long dtUpdate,
            @RequestBody ScheduledOperationDto scheduledOperationDto,
            @RequestHeader("Authorization") String token) throws ValidationException {

        validationChecker.checkScheduledOperationDtoFields(scheduledOperationDto);
        scheduledOperationService.update(uuid, dtUpdate, scheduledOperationDto,token);
        return ResponseEntity.ok().build();
    }
}
