package com.telegram.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.telegram.utils.OperationDtoDeserializer;
import com.telegram.utils.OperationDtoSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonSerialize(using = OperationDtoSerializer.class)
@JsonDeserialize(using = OperationDtoDeserializer.class)
public class OperationDto {

    private UUID uuid;
    private LocalDateTime dtCreate;
    private LocalDateTime dtUpdate;
    private String description;
    private BigDecimal value;
    private LocalDateTime dateOperation;
    private UUID category;
    private UUID currency;

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

    public UUID getCurrency() {
        return currency;
    }

    public void setCurrency(UUID currency) {
        this.currency = currency;
    }

    public UUID getCategory() {
        return category;
    }

    public void setCategory(UUID category) {
        this.category = category;
    }

    public LocalDateTime getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(LocalDateTime dateOperation) {
        this.dateOperation = dateOperation;
    }

    @Override
    public String toString() {
        return
                "uuid:" + uuid +
                ", description:'" + description + '\'' +
                ", value:" + value +
                ", dateOperation:" + dateOperation +
                '}';
    }
}
