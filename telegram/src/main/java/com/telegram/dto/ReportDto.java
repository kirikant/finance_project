package com.telegram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.telegram.entity.ReportType;
import com.telegram.entity.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReportDto {

    private UUID uuid;
    @JsonProperty("dt_create")
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

    @Override
    public String toString() {
        return
                "uuid=" + uuid +
                ", dtCreate=" + dtCreate +
                ", status=" + status +
                ", reportType=" + reportType +
                ", description='" + description + '\'' +
                ", reportParamDto=" + reportParamDto;
    }
}
