package com.accounts.repositories;

import com.accounts.entity.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BalanceRepo extends JpaRepository<BalanceEntity, UUID> {
}
