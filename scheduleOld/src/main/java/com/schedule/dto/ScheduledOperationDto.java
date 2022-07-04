package com.schedule.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.schedule.utils.ScheduleDtoSerializer;
import com.schedule.utils.ScheduledOperationDtoSerializer;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonSerialize(using = ScheduledOperationDtoSerializer.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduledOperationDto {

    private UUID uuid;
    private LocalDateTime dtCreate;
    private LocalDateTime dtUpdate;
    private ScheduleDto scheduleDto;
    private OperationDto operationDto;
    private String user;

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

    public ScheduleDto getScheduleDto() {
        return scheduleDto;
    }

    public void setScheduleDto(ScheduleDto scheduleDto) {
        this.scheduleDto = scheduleDto;
    }

    public OperationDto getOperationDto() {
        return operationDto;
    }

    public void setOperationDto(OperationDto operationDto) {
        this.operationDto = operationDto;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
