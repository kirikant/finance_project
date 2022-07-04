package mailScheduler.exception;

import mailScheduler.dto.ReportParamDto;
import mailScheduler.dto.ScheduleDto;
import mailScheduler.dto.ScheduledReportDto;
import mailScheduler.entity.ReportType;
import org.springframework.stereotype.Component;

@Component
public class ValidationChecker {

    public void checkScheduledReportDtoFields(ScheduledReportDto scheduledReportDto) throws ValidationException {

        ValidationException validationException = new ValidationException();
        ReportParamDto reportParamDto = scheduledReportDto.getReportParamDto();
        ScheduleDto scheduleDto = scheduledReportDto.getScheduleDto();

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

        if (reportParamDto.getAccounts() == null) {
            validationException
                    .add(new ValidationError("accounts", "the accounts were not given"));
        }

        if (scheduledReportDto.getReportType() == null) {
            validationException
                    .add(new ValidationError("report type", "the report type were not given"));
        }

        if (scheduledReportDto.getReportType() != ReportType.BALANCE) {
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

