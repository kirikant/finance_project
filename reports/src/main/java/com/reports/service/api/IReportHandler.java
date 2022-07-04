package com.reports.service.api;

import com.reports.dto.OperationDto;
import com.reports.dto.OperationEntry;
import com.reports.dto.ReportParamDto;
import com.reports.entity.ReportType;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IReportHandler {
    File createBalanceReport(ReportParamDto reportParamDto, UUID reportUuid, String token) throws IOException;
    File createByParameterReport(ReportParamDto reportParamDto,
                                 UUID reportUuid,
                                 ReportType reportType,
                                 String token) throws IOException;


}
