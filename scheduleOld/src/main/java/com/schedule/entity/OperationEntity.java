package com.schedule.entity;



import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
public class OperationEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    private UUID uuid;
    private LocalDateTime dtCreate;
    @Version
    private LocalDateTime dtUpdate;
    private String description;
    private BigDecimal value;
    private UUID currencyUuid;
    private UUID accountUuid;
    private UUID categoryUuid;


    public OperationEntity() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public UUID getCurrencyUuid() {
        return currencyUuid;
    }

    public void setCurrencyUuid(UUID currencyUuid) {
        this.currencyUuid = currencyUuid;
    }

    public UUID getAccountUuid() {
        return accountUuid;
    }

    public void setAccountUuid(UUID accountUuid) {
        this.accountUuid = accountUuid;
    }

    public UUID getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(UUID categoryUuid) {
        this.categoryUuid = categoryUuid;
    }
}
