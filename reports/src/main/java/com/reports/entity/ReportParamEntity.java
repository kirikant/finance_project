package com.reports.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "report_param_entity")
public class ReportParamEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    private UUID uuid;
    @ManyToMany
    @JoinTable(
            name = "report_param_Account",
            joinColumns = { @JoinColumn(name = "report_param_uuid") },
            inverseJoinColumns = { @JoinColumn(name = "account_uuid") }
    )
    private List<AccountEntity> accounts;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    @ManyToMany
    @JoinTable(
            name = "report_param_categories",
            joinColumns = { @JoinColumn(name = "report_param_uuid") },
            inverseJoinColumns = { @JoinColumn(name = "category_uuid") }
    )
    private List<CategoryEntity> categories;

    public ReportParamEntity() {
    }

    public ReportParamEntity(UUID uuid, List<AccountEntity> accounts, LocalDateTime fromTime,
                             LocalDateTime toTime, List<CategoryEntity> categories) {
        this.uuid = uuid;
        this.accounts = accounts;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.categories = categories;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<AccountEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountEntity> accounts) {
        this.accounts = accounts;
    }

    public LocalDateTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalDateTime toTime) {
        this.toTime = toTime;
    }

    public LocalDateTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalDateTime fromTime) {
        this.fromTime = fromTime;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }
}
