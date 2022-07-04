package com.schedule.repostories;

import com.schedule.entity.ScheduledOperationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ScheduledOperationRepo extends JpaRepository <ScheduledOperationEntity, UUID> {
    //@EntityGraph("scheduledOperation-operation,schedule")
    Optional<Page<ScheduledOperationEntity>> findAllByUserAndOperationEntity_AccountUuid(String username,UUID uuid, Pageable pageable);
    //@EntityGraph("scheduledOperation-operation,schedule")
    ScheduledOperationEntity findByUuidAndDtUpdateAndUser(UUID uuid, LocalDateTime dtUpdate, String username);
}
