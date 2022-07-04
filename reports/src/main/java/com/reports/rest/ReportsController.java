package com.reports.rest;

import com.reports.dto.ReportParamDto;
import com.reports.entity.ReportType;
import com.reports.exception.ValidationChecker;
import com.reports.exception.ValidationException;
import com.reports.service.ReportService;
import com.reports.service.aws.AwsService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/report")
public class ReportsController {

    private final ReportService reportService;
    private final ValidationChecker validationChecker;
    private final AwsService awsService;

    public ReportsController(ReportService reportService, ValidationChecker validationChecker,
                             AwsService awsService) {
        this.reportService = reportService;
        this.validationChecker = validationChecker;
        this.awsService = awsService;
    }

    @PostMapping("/{reportType}")
    public ResponseEntity<?> addReport(@PathVariable ReportType reportType,
                                       @RequestBody ReportParamDto reportParamDto,
                                       @RequestHeader("Authorization") String token)
            throws IOException, ValidationException {
        validationChecker.checkReportParamDtoFields(reportParamDto, reportType);
        reportService.addReport(reportType, reportParamDto,  token);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public  ResponseEntity<?> getReports(@RequestParam Integer page,
                                         @RequestParam Integer size,
                                         @RequestHeader("Authorization") String token) throws ValidationException {
        validationChecker.checkPageParams(page, size);
     return ResponseEntity.ok().body(reportService.getReports(page, size, token));
    }

    @GetMapping("/{uuid}/export")
    public  ResponseEntity<?> getReport(@PathVariable UUID uuid,
                                        @RequestHeader("Authorization") String token) {
        reportService.checkReportPermission(uuid,token);
        String downloadURl = awsService.downloadFile(uuid.toString());
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(downloadURl)).build();
    }

    @RequestMapping(path = "/{uuid}/export",method = RequestMethod.HEAD)
    public ResponseEntity<?> head(@PathVariable UUID uuid,
                                  @RequestHeader("Authorization") String token){
        reportService.checkReportPermission(uuid,token);
       awsService.checkExistence(String.valueOf(uuid));
        return ResponseEntity.ok().build();
    }
}
