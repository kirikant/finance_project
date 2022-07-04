package com.reports.service.api;

import com.reports.dto.AccountDto;
import com.reports.dto.ReportDto;
import com.reports.dto.ReportParamDto;
import com.reports.entity.ReportType;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IReportService {
    void addReport(ReportType reportType, ReportParamDto reportParamDto,String token) throws IOException;
    Page<ReportDto> getReports(Integer page, Integer size, String token);

}
