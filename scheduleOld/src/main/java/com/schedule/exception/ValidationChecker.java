package com.schedule.exception;


import com.schedule.dto.OperationDto;
import com.schedule.dto.ScheduleDto;
import com.schedule.dto.ScheduledOperationDto;
import org.springframework.stereotype.Component;

@Component
public class ValidationChecker {

    public void checkScheduledOperationDtoFields(ScheduledOperationDto scheduledOperationDto) throws ValidationException {
        ValidationException validationException = new ValidationException();
        ValidationException validationException1 =
                checkOperationDtoFields(scheduledOperationDto.getOperationDto(), validationException);
        ValidationException validationException2 =
                checkScheduleDtoFields(scheduledOperationDto.getScheduleDto(), validationException1);
        if (validationException.getValidationExceptions().size() > 0) throw validationException2;
    }

    public void checkPageParams(Integer page, Integer size) throws ValidationException {
        ValidationException validationException = new ValidationException();

        if (page < 0) validationException
                .add(new ValidationError("page", "the page parameter is incorrect"));
        if (size <0) validationException
                .add(new ValidationError("size", "the size parameter is incorrect"));
        if (validationException.getValidationExceptions().size() > 0) throw validationException;
    }

    private ValidationException checkOperationDtoFields(OperationDto operationDto, ValidationException validationException)
            throws ValidationException {
        if (operationDto.getAccount() == null) {
            validationException
                    .add(new ValidationError("account uuid", "the account uuid was not given"));
        }
        if (operationDto.getDescription() == null) {
            validationException
                    .add(new ValidationError("description", "the description was not given"));
        }
        if (operationDto.getCurrency() == null) {
            validationException
                    .add(new ValidationError("currency", "the currency was not given"));
        }
        if (operationDto.getCategory() == null) {
            validationException
                    .add(new ValidationError("category", "the category was not given"));
        }
        if (operationDto.getValue() == null) {
            validationException
                    .add(new ValidationError("value", "the value was not given"));
        }

        return validationException;
    }

    private ValidationException checkScheduleDtoFields(ScheduleDto scheduleDto, ValidationException validationException)
            throws ValidationException {
        if (scheduleDto.getTimeUnit() == null) {
            validationException
                    .add(new ValidationError("time unit", "the time unit was not given"));
        }
        if (scheduleDto.getInterval() == null) {
            validationException
                    .add(new ValidationError("interval", "the interval was not given"));
        }
        if (scheduleDto.getStartTime() == null) {
            validationException
                    .add(new ValidationError("start time", "the start time was not given"));
        }
        if (scheduleDto.getStopTime() == null) {
            validationException
                    .add(new ValidationError("stop time", "the stop time was not given"));
        }

        return validationException;
    }
}

