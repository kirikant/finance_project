package com.reports.entity;


import javax.persistence.*;
import java.util.List;
import java.util.UUID;


@Entity
public class AccountEntity {

    @Id
    @GeneratedValue
    private UUID uuid;

    @ManyToMany(mappedBy = "accounts")
    private List<ReportParamEntity> reportParamEntities;

    public AccountEntity() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<ReportParamEntity> getReportParamEntities() {
        return reportParamEntities;
    }

    public void setReportParamEntities(List<ReportParamEntity> reportParamEntities) {
        this.reportParamEntities = reportParamEntities;
    }
}
