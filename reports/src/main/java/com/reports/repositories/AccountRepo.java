package com.reports.repositories;



import com.reports.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepo extends JpaRepository<AccountEntity, UUID> {
    @Modifying
    @Query(value = "insert into account_entity(uuid) select :uuid",nativeQuery = true)
    void save(@Param("uuid") UUID uuid);
}
