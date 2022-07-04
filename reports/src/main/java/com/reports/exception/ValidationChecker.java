package com.reports.exception;

import com.reports.dto.ReportParamDto;
import com.reports.entity.ReportType;
import org.springframework.stereotype.Component;

@Component
public class ValidationChecker {

    public void checkReportParamDtoFields(ReportParamDto reportParamDto,
                                          ReportType reportType) throws ValidationException {
        ValidationException validationException = new ValidationException();
        if (reportParamDto.getAccounts() == null) {
            validationException
                    .add(new ValidationError("accounts", "the accounts were not given"));
        }
        if (reportType != ReportType.BALANCE) {
            if (reportParamDto.getCategories() == null) {
                validationException
                        .add(new ValidationError("categories", "the categories were not given"));
            }
            if (reportParamDto.getFromTime() == null) {
                validationException
                        .add(new ValidationError("from time", "the from time were not given"));
            }
            if (reportParamDto.getToTime() == null) {
                validationException
                        .add(new ValidationError("to time", "the to time were not given"));
            }
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


}

