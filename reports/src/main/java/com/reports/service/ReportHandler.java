package com.reports.service;

import com.reports.dto.*;
import com.reports.entity.ReportEntity;
import com.reports.entity.ReportType;
import com.reports.entity.Status;
import com.reports.repositories.ReportEntityRepo;
import com.reports.security.JwtTokenProvider;
import com.reports.service.api.IReportHandler;
import com.reports.service.aws.AwsService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReportHandler implements IReportHandler {

    private final XlsCreator xlsCreator;
    private final CrossServiceGetter crossServiceGetter;
    private final AwsService awsService;
    private final ReportEntityRepo reportEntityRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private Logger logger = LoggerFactory.getLogger(ReportHandler.class);


    public ReportHandler(XlsCreator xlsCreator, CrossServiceGetter crossServiceGetter, AwsService awsService, ReportEntityRepo reportEntityRepo, JwtTokenProvider jwtTokenProvider) {
        this.xlsCreator = xlsCreator;
        this.crossServiceGetter = crossServiceGetter;
        this.awsService = awsService;
        this.reportEntityRepo = reportEntityRepo;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public File createBalanceReport(ReportParamDto reportParamDto, UUID reportUuid, String token) throws IOException {
        List<AccountDto> sortedAccounts = crossServiceGetter.getSortedAccountDtos(reportParamDto,token);
        Workbook workbook = xlsCreator.createBalanceXls(sortedAccounts);
        ReportEntity report = reportEntityRepo.findById(reportUuid)
                .orElseThrow(() -> new EntityNotFoundException("There is no such report"));
        report.setStatus(Status.DONE);
        File file = saveXls(reportUuid, workbook);
        return file;
    }


    public File createByParameterReport(ReportParamDto reportParamDto,
                                        UUID reportUuid,
                                        ReportType reportType,
    String token) throws IOException {
        List<AccountDto> sortedAccountDtos = crossServiceGetter.getSortedAccountDtos(reportParamDto,token);

        List<OperationEntry> operationEntries = new LinkedList<>();

        for (AccountDto sortedAccount : sortedAccountDtos) {
            List<OperationDto> operationDtos = crossServiceGetter
                    .getOperations(sortedAccount, reportParamDto.getCategories(), token);
            operationDtos.stream().filter(x ->
                    (x.getDtCreate().isAfter(reportParamDto.getFromTime()) &&
                            x.getDtCreate().isBefore(reportParamDto.getToTime()))
            ).forEach(x -> operationEntries.add(new OperationEntry(x, sortedAccount)));
        }

        List<OperationEntry> sortedOperationEntries = operationEntries.stream()
                .sorted(Comparator.comparing(x -> x.getOperationDto().getDateOperation()
                        .toInstant(ZoneOffset.UTC)))
                .collect(Collectors.toList());

        Workbook workbook = null;

        switch (reportType) {
            case BY_DATE:
                workbook = xlsCreator
                        .createByParameterXls(createOperationEntriesByDate(sortedOperationEntries),
                                reportType,token);
                break;
            case BY_CATEGORY:
                workbook = xlsCreator.createByParameterXls(createOperationEntriesByCategory(sortedOperationEntries,token),
                        reportType,token);
        }
         ReportEntity report = reportEntityRepo.findById(reportUuid)
                .orElseThrow(() -> new EntityNotFoundException("There is no such report"));
        report.setStatus(Status.DONE);
        return saveXls(reportUuid, workbook);

    }

    private Map<LocalDate, List<OperationEntry>> createOperationEntriesByDate(
            List<OperationEntry> operationEntries) {

        Map<LocalDate, List<OperationEntry>> entryHashMap = new HashMap<>();

        operationEntries.stream().forEach(x -> {
            entryHashMap.putIfAbsent(x.getOperationDto().getDateOperation().toLocalDate(),
                    new LinkedList<>(List.of(x)));
            entryHashMap.computeIfPresent(x.getOperationDto().getDateOperation().toLocalDate(),
                    (date, operationEntryList) -> {
                        operationEntryList.add(x);
                        return operationEntryList;
                    });
        });

        return entryHashMap;
    }

    private Map<CategoryDto, List<OperationEntry>> createOperationEntriesByCategory(
            List<OperationEntry> operationEntries, String token) throws IOException {
        Map<CategoryDto, List<OperationEntry>> entryHashMap = new HashMap<>();
        List<CategoryDto> categoriesDtos = crossServiceGetter.getCategoriesDtos(token);
        Map<UUID, CategoryDto> categoryDtoMap = new HashMap<>();
        categoriesDtos.forEach((x) -> categoryDtoMap.put(x.getUuid(), x));

        operationEntries.forEach(
                x -> {
                    entryHashMap.putIfAbsent(categoryDtoMap.get(x.getOperationDto().getCategory()),
                            new LinkedList<>(List.of(x)));
                    entryHashMap.computeIfPresent(categoryDtoMap.get(x.getOperationDto().getCategory()),
                            (date, operationEntryList) -> {
                                operationEntryList.add(x);
                                return operationEntryList;
                            }
                    );
                });
        return entryHashMap;
    }


    private File saveXls(UUID reportUuid, Workbook workbook) {
        File file = new File(reportUuid + ".xlsx");

        try(FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            logger.error(e.getCause().toString());
        }

        awsService.uploadFile(file.getName(), file);
        return file;
    }

}
