package com.reports.service.api;

import com.reports.dto.AccountDto;
import com.reports.dto.OperationEntry;
import com.reports.entity.ReportType;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IXlsCreator {

    Workbook createBalanceXls(List<AccountDto> accountDtos);
    Workbook createByParameterXls(Map<?, List<OperationEntry>> accountOperationMap,
                                  ReportType reportType, String token) throws IOException;

}
