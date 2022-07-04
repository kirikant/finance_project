package mailScheduler.repositories;

import mailScheduler.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepo extends JpaRepository<AccountEntity, UUID> {
    @Modifying
    @Query(value = "insert into account_entity(uuid) select :uuid",nativeQuery = true)
    void save(@Param("uuid") UUID uuid);
}
