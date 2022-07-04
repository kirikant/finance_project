package com.accounts.repositories;

import com.accounts.entity.OperationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OperationRepo extends JpaRepository<OperationEntity, UUID> {
     @EntityGraph(value = "operation-account")
     Page<OperationEntity> findAllByAccountEntity_UuidAndAccountEntity_User(UUID uuid,
                                                                            Pageable pageable,
                                                                            String username);
     @EntityGraph(value = "operation-account")
     Optional<OperationEntity> findByUuidAndDtUpdate(UUID uuid, LocalDateTime dtUpdate);
     @EntityGraph(value = "operation-account")
     List<OperationEntity> findAllByAccountEntity_UuidAndAccountEntity_UserAndCategoryIn(UUID uuid,
                                                                                         String username,
                                                                                         List<UUID> categories);
     void deleteByUuidAndAccountEntity_User(UUID uuid,String username);

     @EntityGraph(value = "operation-account")
     List<OperationEntity> findAllByAccountEntity_UuidAndAccountEntity_UserAndDateOperationBetween(
           UUID uuid,String username,LocalDateTime from,LocalDateTime to);

     Optional<OperationEntity> findByUuidAndAccountEntity_User(UUID uuid,String username);
}
