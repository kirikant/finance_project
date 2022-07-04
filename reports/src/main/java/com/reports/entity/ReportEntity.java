package com.reports.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@NamedEntityGraph(
        name = "report-reportParam",
        attributeNodes = {
                @NamedAttributeNode(value = "reportParamEntity",
                        subgraph = "reportParamEntity-accounts"
                        )
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "reportParamEntity-accounts",
                    attributeNodes = @NamedAttributeNode("categories"))
        }
)
@Entity
public class ReportEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    private UUID uuid;
    private LocalDateTime dtCreate;
    @Version
    private LocalDateTime dtUpdate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private ReportType reportType;
    private String description;
    @OneToOne(cascade = CascadeType.ALL)
    private ReportParamEntity reportParamEntity;
    @Column(name = "finance_user")
    private String user;

    public ReportEntity() {
    }

    public ReportEntity(UUID uuid, LocalDateTime dtCreate, LocalDateTime dtUpdate,
                        Status status,
                        ReportType reportType, String description,
                        ReportParamEntity reportParamEntity) {
        this.uuid = uuid;
        this.dtCreate = dtCreate;
        this.dtUpdate = dtUpdate;
        this.status = status;
        this.reportType = reportType;
        this.description = description;
        this.reportParamEntity = reportParamEntity;
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

    public ReportType getType() {
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

    public ReportParamEntity getReportParamEntity() {
        return reportParamEntity;
    }

    public void setReportParamEntity(ReportParamEntity reportParamEntity) {
        this.reportParamEntity = reportParamEntity;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
