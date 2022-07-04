package com.accounts.exception;

import com.accounts.dto.AccountDto;
import com.accounts.dto.OperationDto;
import org.springframework.stereotype.Component;

@Component
public class ValidationChecker {

    public void checkAccountDtoFields(AccountDto accountDto) throws ValidationException {
        ValidationException validationException = new ValidationException();
        if (accountDto.getTitle() == null) {
            validationException
                    .add(new ValidationError("tittle", "the tittle was not given"));
        }
        if (accountDto.getDescription() == null) {
            validationException
                    .add(new ValidationError("description", "the description was not given"));
        }
        if (accountDto.getType() == null) {
            validationException
                    .add(new ValidationError("type", "the type was not given"));
        }
        if (accountDto.getCurrency() == null) {
            validationException
                    .add(new ValidationError("currency", "the currency was not given"));
        }
        if (validationException.getValidationExceptions().size() > 0) throw validationException;
    }

    public void checkPageParams(Integer page, Integer size) throws ValidationException {
        ValidationException validationException = new ValidationException();
        if (page == null) validationException
                .add(new ValidationError("page", "the page parameter was not given"));
        if (page < 0) validationException
                .add(new ValidationError("page", "the page parameter is incorrect"));
        if (size == null) validationException
                .add(new ValidationError("size", "the size parameter was not given"));
        if (validationException.getValidationExceptions().size() > 0) throw validationException;
    }

    public void checkOperationDtoFields(OperationDto operationDto) throws ValidationException {
        ValidationException validationException = new ValidationException();

        if (operationDto.getDescription() == null) {
            validationException
                    .add(new ValidationError("description", "the description was not given"));
        }
        if (operationDto.getCategory() == null) {
            validationException
                    .add(new ValidationError("category", "the category was not given"));
        }
        if (operationDto.getValue() == null) {
            validationException
                    .add(new ValidationError("value", "the value was not given"));
        }
        if (operationDto.getCurrency() == null) {
            validationException
                    .add(new ValidationError("currency", "the currency was not given"));
        }
        if (validationException.getValidationExceptions().size() > 0) throw validationException;
    }
}

