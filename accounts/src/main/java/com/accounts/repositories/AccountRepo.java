package com.accounts.repositories;

import com.accounts.entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepo extends JpaRepository<AccountEntity, UUID> {
   @EntityGraph(value = "account-balance-graph")
   Optional<AccountEntity> findByUuidAndDtUpdateAndUser(UUID uuid, LocalDateTime update,String login);
   @EntityGraph(value = "account-balance-graph")
   Page<AccountEntity> findAllByUser(Pageable pageable, String login);
   @EntityGraph(value = "account-balance-graph")
   Optional<AccountEntity> findByUuidAndUser(UUID uuid,String login);
   @EntityGraph(value = "account-balance-graph")
   List<AccountEntity> findAllByUuidInAndUser (Iterable <UUID> uuids,String login);

}
