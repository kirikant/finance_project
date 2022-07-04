package com.classifier.classifier.exception;

import com.classifier.classifier.dto.CategoryDto;
import com.classifier.classifier.dto.CurrencyDto;
import com.classifier.classifier.dto.SimpleDto;
import org.springframework.stereotype.Component;

@Component
public class ValidationChecker {

    public void checkSimpleDtoFields(SimpleDto simpleDto) throws ValidationException {
        ValidationException validationException = new ValidationException();
        if (simpleDto.getTitle() == null) {
            validationException
                    .add(new ValidationError("tittle", "the tittle was not given"));}
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


}

