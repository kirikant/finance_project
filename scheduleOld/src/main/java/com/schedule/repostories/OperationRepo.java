package com.schedule.repostories;


import com.schedule.entity.OperationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.UUID;

public interface OperationRepo extends JpaRepository<OperationEntity, UUID> {
     Page<OperationEntity> findAllByAccountUuid(UUID uuid, Pageable pageable);
     OperationEntity findByUuidAndDtUpdate(UUID uuid, LocalDateTime dtUpdate);
}
