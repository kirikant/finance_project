package com.telegram.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.telegram.entity.Type;
import com.telegram.utils.AccountDtoDeserializer;
import com.telegram.utils.AccountDtoSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonSerialize(using = AccountDtoSerializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = AccountDtoDeserializer.class)
public class AccountDto {

  private UUID uuid;
  private LocalDateTime dtCreate;
  private LocalDateTime dtUpdate;
  private String title;
  private String description;
  private Type type;
  private BigDecimal balance;
  private UUID currency;

    public AccountDto() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public UUID getCurrency() {
        return currency;
    }

    public void setCurrency(UUID currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance ) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return  "uuid:" + uuid +
                ", title:'" + title + '\'' +
                ", description:'" + description + '\'' +
                ", type:" + type +
                ", balance:" + balance ;
    }
}
