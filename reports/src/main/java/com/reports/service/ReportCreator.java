package com.reports.service;

import com.reports.dto.MessageDto;
import com.reports.dto.ReportParamDto;
import com.reports.entity.ReportEntity;
import com.reports.entity.ReportType;
import com.reports.entity.Status;
import com.reports.repositories.ReportEntityRepo;
import com.reports.security.JwtTokenProvider;
import com.reports.service.aws.AwsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

@Service
public class ReportCreator {


    private final ReportEntityRepo reportEntityRepo;
    private final XlsCreator xlsCreator;
    private final CrossServiceGetter crossServiceGetter;
    private final AwsService awsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final SenderService senderService;
    private final Logger logger = LoggerFactory.getLogger(CrossServiceGetter.class);

    public ReportCreator(ReportEntityRepo reportEntityRepo, XlsCreator xlsCreator,
                         CrossServiceGetter crossServiceGetter, AwsService awsService,
                         JwtTokenProvider jwtTokenProvider, SenderService senderService) {
        this.reportEntityRepo = reportEntityRepo;
        this.xlsCreator = xlsCreator;
        this.crossServiceGetter = crossServiceGetter;
        this.awsService = awsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.senderService = senderService;
    }

    @Transactional
    @Async
    public void asyncCreateBalanceReport(ReportEntity report, ReportParamDto reportParamDto,
                                             String token) {
            ReportEntity reportAfterSave = reportEntityRepo.getById(report.getUuid());
            try {
                File balanceReport = new ReportHandler(xlsCreator, crossServiceGetter,
                        awsService, reportEntityRepo, jwtTokenProvider)
                        .createBalanceReport(reportParamDto, report.getUuid(), token);
                reportAfterSave.setStatus(Status.LOADED);    //????
                MessageDto message = senderService.createMessage(balanceReport, report.getDescription(),
                        token, report.getUuid());
                senderService.sendMail(message, token);
                balanceReport.delete();
                reportEntityRepo.save(reportAfterSave);
            } catch (IOException e) {
                reportAfterSave.setStatus(Status.ERROR);
                reportEntityRepo.save(reportAfterSave);
                logger.error(e.getCause().toString());
            }
    }

    @Transactional
    @Async
    public void asyncCreateByDateReport(ReportEntity report, ReportParamDto reportParamDto,
                                        String token){
        ReportEntity reportAfterSave = reportEntityRepo.getById(report.getUuid());
            try {
                File byParameterReport = new ReportHandler(xlsCreator, crossServiceGetter, awsService,
                        reportEntityRepo, jwtTokenProvider)
                        .createByParameterReport(reportParamDto,
                                report.getUuid(), ReportType.BY_DATE, token);
                MessageDto message = senderService.createMessage(byParameterReport, report.getDescription(),
                        token, report.getUuid());
                senderService.sendMail(message, token);
                byParameterReport.delete();
                reportAfterSave.setStatus(Status.LOADED);
                reportEntityRepo.save(reportAfterSave);

            } catch (IOException e) {
                reportAfterSave.setStatus(Status.ERROR);
                reportEntityRepo.save(reportAfterSave);
                logger.error(e.getCause().toString());
            }

    }

    @Transactional
    @Async
    public void asyncCreateByCategoryReport(ReportEntity report, ReportParamDto reportParamDto,
                                        String token) {
        ReportEntity reportAfterSave = reportEntityRepo.getById(report.getUuid());
            try {
                File byParameterReport = new ReportHandler(xlsCreator, crossServiceGetter, awsService,
                        reportEntityRepo, jwtTokenProvider)
                        .createByParameterReport(reportParamDto,
                                report.getUuid(), ReportType.BY_CATEGORY, token);
                reportAfterSave.setStatus(Status.LOADED);
                MessageDto message = senderService.createMessage(byParameterReport, report.getDescription(),
                        token, report.getUuid());
                senderService.sendMail(message, token);
                byParameterReport.delete();
                reportEntityRepo.save(reportAfterSave);
            } catch (IOException e) {
                reportAfterSave.setStatus(Status.ERROR);
                reportEntityRepo.save(reportAfterSave);
                logger.error(e.getCause().toString());
            }

    }

}
