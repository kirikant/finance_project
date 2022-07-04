package mailScheduler.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@NamedEntityGraph(
        name = "scheduledReport-schedule,reportParam",
        attributeNodes = {
                @NamedAttributeNode(value = "scheduleEntity"),
                @NamedAttributeNode(value = "reportParamEntity",subgraph ="reportParam-account")
        },
        subgraphs = {
                @NamedSubgraph(name = "reportParam-account",
                attributeNodes = {@NamedAttributeNode("accounts")}
                )
        }
)

@Entity
public class ScheduledReportEntity {

    @Id
    @GeneratedValue( generator = "uuid")
    private UUID uuid;
    private LocalDateTime dtCreate;
    @Version
    private LocalDateTime dtUpdate;
    @OneToOne(cascade = CascadeType.ALL)
    private ScheduleEntity scheduleEntity;
    @OneToOne(cascade = CascadeType.ALL)
    private ReportParamEntity reportParamEntity;
    @Column(name = "finance_user")
    private String user;
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

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

    public ScheduleEntity getScheduleEntity() {
        return scheduleEntity;
    }

    public void setScheduleEntity(ScheduleEntity scheduleEntity) {
        this.scheduleEntity = scheduleEntity;
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

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }
}
