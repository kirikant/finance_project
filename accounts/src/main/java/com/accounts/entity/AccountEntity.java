package com.accounts.entity;


import org.springframework.data.jpa.repository.EntityGraph;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@NamedEntityGraph(
        name = "account-balance-graph",
        attributeNodes = {
                @NamedAttributeNode("balance")
        }
)
@Entity
public class AccountEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    private UUID uuid;
    private LocalDateTime dtCreate;
    @Version
    private LocalDateTime dtUpdate;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Type type;
    @OneToOne(cascade = CascadeType.ALL)
    private BalanceEntity balance;
    @Column(name = "currency_uuid")
    private UUID currency;
    @Column(name = "finance_user")
    private String user;

    public AccountEntity() {
    }

    public AccountEntity(UUID uuid, LocalDateTime dtCreate, LocalDateTime dtUpdate,
                         String title, String description, Type type, UUID currency) {
        this.uuid = uuid;
        this.dtCreate = dtCreate;
        this.dtUpdate = dtUpdate;
        this.title = title;
        this.description = description;
        this.type = type;
        this.currency = currency;
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

    public BalanceEntity getBalance() {
        return balance;
    }

    public void setBalance(BalanceEntity balance) {
        this.balance = balance;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
