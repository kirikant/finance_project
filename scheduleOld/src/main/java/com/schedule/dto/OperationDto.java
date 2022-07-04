package com.schedule.dto;



import java.math.BigDecimal;
import java.util.UUID;


public class OperationDto {

    private String description;
    private BigDecimal value;
    private UUID currency;
    private UUID account;
    private UUID category;

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

    public UUID getCurrency() {
        return currency;
    }

    public void setCurrency(UUID currency) {
        this.currency = currency;
    }

    public UUID getAccount() {
        return account;
    }

    public void setAccount(UUID accountUUID) {
        this.account = accountUUID;
    }

    public UUID getCategory() {
        return category;
    }

    public void setCategory(UUID category) {
        this.category = category;
    }


}
