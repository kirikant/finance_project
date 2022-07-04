package com.schedule.entity;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@NamedEntityGraph(
        name = "scheduledOperation-operation,schedule",
        attributeNodes = {
                @NamedAttributeNode(value = "scheduleEntity"),
                @NamedAttributeNode(value = "operationEntity")
        }
)
@Entity
public class ScheduledOperationEntity {

    @Id
    @GeneratedValue( generator = "uuid")
    private UUID uuid;
    private LocalDateTime dtCreate;
    @Version
    private LocalDateTime dtUpdate;
    @OneToOne(cascade = CascadeType.ALL)
    private ScheduleEntity scheduleEntity;
    @OneToOne(cascade = CascadeType.ALL)
    private OperationEntity operationEntity;
    @Column(name = "finance_user")
    private String user;

    public ScheduledOperationEntity() {
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

    public ScheduleEntity getScheduleEntity() {
        return scheduleEntity;
    }

    public void setScheduleEntity(ScheduleEntity scheduleEntity) {
        this.scheduleEntity = scheduleEntity;
    }

    public OperationEntity getOperationEntity() {
        return operationEntity;
    }

    public void setOperationEntity(OperationEntity operationEntity) {
        this.operationEntity = operationEntity;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
