package com.reports.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.reports.entity.Status;
import com.reports.entity.ReportType;
import com.reports.utils.ReportDtoSerializer;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonSerialize(using = ReportDtoSerializer.class)
public class ReportDto {

    private UUID uuid;
    private LocalDateTime dtCreate;
    private LocalDateTime dtUpdate;
    private Status status;
    private ReportType reportType;
    private String description;
    private ReportParamDto reportParamDto;


    public ReportDto() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(LocalDateTime dtCreate) {
        this.dtCreate = dtCreate;
    }

    public LocalDateTime getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(LocalDateTime dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setType(ReportType reportType) {
        this.reportType = reportType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReportParamDto getReportParamDto() {
        return reportParamDto;
    }

    public void setReportParamDto(ReportParamDto reportParamDto) {
        this.reportParamDto = reportParamDto;
    }
}
