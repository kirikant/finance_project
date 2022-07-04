package com.telegram.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.telegram.utils.ReportParamDtoDeserializer;
import com.telegram.utils.ReportParamDtoSerializer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonDeserialize(using = ReportParamDtoDeserializer.class)
@JsonSerialize(using = ReportParamDtoSerializer.class)
public class ReportParamDto {


    private List<UUID> accounts;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private List<UUID> categories;

    public ReportParamDto() {
    }

    public List<UUID> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<UUID> accounts) {
        this.accounts = accounts;
    }

    public LocalDateTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalDateTime fromTime) {
        this.fromTime = fromTime;
    }

    public LocalDateTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalDateTime toTime) {
        this.toTime = toTime;
    }

    public List<UUID> getCategories() {
        return categories;
    }

    public void setCategories(List<UUID> categories) {
        this.categories = categories;
    }


    @Override
    public String toString() {
        return "Parameters:" +
                "accounts=" + accounts +
                ", categories=" + categories +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime ;

    }
}
