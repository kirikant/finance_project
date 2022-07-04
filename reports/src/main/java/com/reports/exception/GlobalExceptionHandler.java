package com.reports.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e) {
        List<ValidationError> validationExceptions = e.getValidationExceptions();
        HashMap<String, Object> map = new HashMap<>();
        for (ValidationError exception :
                validationExceptions) {
            map.put("incorrect field: " + exception.getField(), exception.getMessage());
            logger.error(exception.getMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleServerExceptions(Throwable e) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("logref", e.toString());
        map.put("message", "server was unable to process the request correctly");
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);

    }


}
