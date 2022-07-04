package com.reports.service;

import com.reports.dto.*;
import com.reports.entity.ReportType;
import com.reports.service.api.IXlsCreator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class XlsCreator implements IXlsCreator {

    private final CrossServiceGetter crossServiceGetter;

    public XlsCreator(CrossServiceGetter crossServiceGetter) {
        this.crossServiceGetter = crossServiceGetter;
    }

    public Workbook createBalanceXls(List<AccountDto> accountDtos) {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Accounts balances");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Title");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Balance");
        headerCell.setCellStyle(headerStyle);

        int row = 0;
        for (AccountDto sortedAccount :
                accountDtos) {
            Row line = sheet.createRow(row++);
            Cell cell1 = line.createCell(0);
            cell1.setCellValue(sortedAccount.getTitle());
            Cell cell2 = line.createCell(1);
            cell2.setCellValue(sortedAccount.getBalance().doubleValue());
        }
        CellRangeAddress region = new CellRangeAddress(0, row, 0, 1);
        RegionUtil.setBorderTop(BorderStyle.DASH_DOT, region, sheet);

        return workbook;
    }


    public Workbook createByParameterXls(Map<?, List<OperationEntry>> accountOperationMap,
                                         ReportType reportType,String token) throws IOException {
        List<CategoryDto> categoriesDtos = crossServiceGetter.getCategoriesDtos(token);
        List<CurrencyDto> currencyDtos = crossServiceGetter.getCurrencyDtos(token);

        Workbook workbook = new XSSFWorkbook();
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = null;
        switch (reportType) {
            case BY_DATE:
                sheet = workbook.createSheet("Accounts statement(by date)");
                break;
            case BY_CATEGORY:
                sheet = workbook.createSheet("Accounts statement(by category)");
                break;
        }

        for (int i = 0; i < 7; i++) {
            sheet.setColumnWidth(i, 6000);
        }

        CellStyle cellStyleTime = workbook.createCellStyle();
        cellStyleTime.setDataFormat(
                createHelper.createDataFormat().getFormat("hh:mm:ss"));
        CellStyle cellStyleDate = workbook.createCellStyle();
        cellStyleDate.setDataFormat(
                createHelper.createDataFormat().getFormat("dd.MM.yy"));

        CellStyle cellStyleDateTime = workbook.createCellStyle();
        cellStyleDateTime.setDataFormat(
                createHelper.createDataFormat().getFormat("dd.MM.yy hh:mm:ss"));

        for (Map.Entry<?, List<OperationEntry>> entry : accountOperationMap.entrySet()) {

            if (sheet.getLastRowNum() == -1) {
                Row header = sheet.createRow(sheet.getLastRowNum() + 1);
                switch (reportType) {
                    case BY_DATE:
                        header.createCell(1).setCellValue("time");
                        header.createCell(6).setCellValue("category");
                        break;
                    case BY_CATEGORY:
                        header.createCell(1).setCellValue("date");
                        break;
                }
                header.createCell(2).setCellValue("description");
                header.createCell(3).setCellValue("operation value");
                header.createCell(4).setCellValue("currency");
                header.createCell(5).setCellValue("account");
            }

            Row current = sheet.createRow(sheet.getLastRowNum() + 1);

            switch (reportType) {
                case BY_DATE:
                    Cell cell = current.createCell(0);
                    cell.setCellStyle(cellStyleDate);
                    cell.setCellValue(Date.from(((LocalDate) entry.getKey())
                            .atStartOfDay(ZoneOffset.UTC).toInstant()));
                    sheet.addMergedRegion(
                            new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(),
                                    0, 6));
                    break;
                case BY_CATEGORY:
                    current.createCell(0)
                            .setCellValue((((CategoryDto) entry.getKey()).getTitle()));
                    sheet.addMergedRegion(
                            new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(),
                                    0, 5));
            }

            for (OperationEntry operationEntry : entry.getValue()) {
                Row row = sheet.createRow(sheet.getLastRowNum() + 1);
                Cell cell = row.createCell(1);
                switch (reportType) {
                    case BY_DATE:
                        cell.setCellStyle(cellStyleTime);
                        break;
                    case BY_CATEGORY:
                        cell.setCellStyle(cellStyleDateTime);
                        break;

                }

                cell.setCellValue(Date.from(operationEntry.getOperationDto()
                        .getDateOperation().toInstant(ZoneOffset.UTC)));


                if (reportType == ReportType.BY_DATE) {
                    for (CategoryDto categoryDto :
                            categoriesDtos) {
                        if (categoryDto.getUuid().equals(operationEntry.getOperationDto()
                                .getCategory()))
                            row.createCell(6).setCellValue(categoryDto.getTitle());
                    }
                }

                row.createCell(2).setCellValue(operationEntry.getOperationDto().getDescription());
                row.createCell(3).setCellValue(operationEntry.getOperationDto().getValue().doubleValue());
                for (CurrencyDto currencyDto :
                        currencyDtos) {
                    if (currencyDto.getUuid().equals(operationEntry.getOperationDto().getCurrency()))
                        row.createCell(4).setCellValue(currencyDto.getTitle());
                }

                row.createCell(5).setCellValue(operationEntry.getAccountDto().getTitle());

            }
        }

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        return  workbook;

    }




}
