package com.reports.service;

import com.reports.dto.*;
import com.reports.entity.*;
import com.reports.repositories.AccountRepo;
import com.reports.repositories.CategoryRepo;
import com.reports.repositories.ReportEntityRepo;
import com.reports.security.JwtTokenProvider;
import com.reports.service.api.IReportService;
import com.reports.service.aws.AwsService;
import com.reports.utils.convertors.RepParDtoToEntity;
import com.reports.utils.convertors.ReportEntityToDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ReportService implements IReportService {

    @Value("${jwt.secret}")
    String secret;
    private final RepParDtoToEntity repParDtoToEntity;
    private final ReportEntityToDto reportEntityToDto;
    private final ReportEntityRepo reportEntityRepo;
    private final AccountRepo accountRepo;
    private final CategoryRepo categoryRepo;
    private final XlsCreator xlsCreator;
    private final CrossServiceGetter crossServiceGetter;
    private final AwsService awsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ReportCreator reportCreator;
    private final SenderService senderService;
    private Logger logger = LoggerFactory.getLogger(CrossServiceGetter.class);

    public ReportService(RepParDtoToEntity repParDtoToEntity, ReportEntityToDto reportEntityToDto,
                         ReportEntityRepo reportEntityRepo,
                         AccountRepo accountRepo, CategoryRepo categoryRepo,
                         XlsCreator xlsCreator, CrossServiceGetter crossServiceGetter,
                         AwsService awsService, JwtTokenProvider jwtTokenProvider,
                         ReportCreator reportCreator, SenderService senderService) {
        this.repParDtoToEntity = repParDtoToEntity;
        this.reportEntityToDto = reportEntityToDto;
        this.reportEntityRepo = reportEntityRepo;
        this.accountRepo = accountRepo;
        this.categoryRepo = categoryRepo;
        this.xlsCreator = xlsCreator;
        this.crossServiceGetter = crossServiceGetter;
        this.awsService = awsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.reportCreator = reportCreator;
        this.senderService = senderService;
    }

    @Transactional
    public void addReport(ReportType reportType, ReportParamDto reportParamDto, String token) throws IOException {
        String username = jwtTokenProvider.getUsername(token);
        LocalDateTime now = LocalDateTime.now();
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setUser(username);
        reportEntity.setDtCreate(now);
        reportEntity.setType(reportType);
        reportEntity.setDescription(reportType.toString().toLowerCase(Locale.ROOT) + " report(" + now + ")");
        ReportParamEntity reportParamEntity = repParDtoToEntity.convert(reportParamDto);
        reportParamEntity.getAccounts().forEach(x -> crossServiceGetter.checkAccountPermission(x.getUuid(), token));

        if (reportParamEntity.getAccounts() != null) reportParamEntity.getAccounts().forEach(x -> {
            if (accountRepo.findById(x.getUuid()).isEmpty()) accountRepo.save(x.getUuid());
        });
        if (reportParamEntity.getCategories() != null) reportParamEntity.getCategories().forEach(x -> {
            if (categoryRepo.findById(x.getUuid()).isEmpty()) categoryRepo.save(x.getUuid());
        });

        reportEntity.setReportParamEntity(reportParamEntity);
        reportEntity.setStatus(Status.PROGRESS);
        ReportEntity report = reportEntityRepo.save(reportEntity);

        switch (reportType) {
            case BALANCE:
               reportCreator.asyncCreateBalanceReport(report, reportParamDto, token);
                break;
            case BY_DATE:
                reportCreator.asyncCreateByDateReport(report,reportParamDto,token);
                break;
            case BY_CATEGORY:
                reportCreator.asyncCreateByCategoryReport(report,reportParamDto,token);
                break;
            default:
                throw new EntityNotFoundException("there is no such report type");
        }


    }


    public Page<ReportDto> getReports(Integer page, Integer size, String token) {
        String username = jwtTokenProvider.getUsername(token);
        return reportEntityRepo
                .findAllByUser(PageRequest.of(page, size, Sort.Direction.DESC, "dtCreate"), username)
                .map(reportEntityToDto::convert);
    }

    public void checkReportPermission(UUID reportUuid, String token) {
        String username = jwtTokenProvider.getUsername(token);
        reportEntityRepo.findByUuidAndUser(reportUuid, username)
                .orElseThrow(() -> new EntityNotFoundException("There is no such report"));
    }





}
