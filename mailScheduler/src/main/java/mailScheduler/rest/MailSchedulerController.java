package mailScheduler.rest;

import mailScheduler.dto.ScheduledReportDto;
import mailScheduler.exception.ValidationChecker;
import mailScheduler.exception.ValidationException;
import mailScheduler.service.ScheduledReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail-scheduler")
public class MailSchedulerController {

    private final ScheduledReportService scheduledReportService;
    private final ValidationChecker validationChecker;

    public MailSchedulerController(ScheduledReportService scheduledReportService,
                                   ValidationChecker validationChecker) {
        this.scheduledReportService = scheduledReportService;
        this.validationChecker = validationChecker;
    }

    @PostMapping("/message-report")
    public ResponseEntity<?> schedule(@RequestBody ScheduledReportDto scheduledReportDto,
                                      @RequestHeader("Authorization") String token) throws ValidationException {

        validationChecker.checkScheduledReportDtoFields(scheduledReportDto);
        scheduledReportService.createScheduledReport(scheduledReportDto, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/message-report")
    public ResponseEntity<?> getScheduledReports(@RequestParam Integer page, @RequestParam Integer size,
                                                 @RequestHeader("Authorization") String token) throws ValidationException {

        validationChecker.checkPageParams(page, size);
        return ResponseEntity.ok(scheduledReportService.getScheduledReports(page, size, token));
    }
}
