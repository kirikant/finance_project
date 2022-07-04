package mailScheduler.repositories;

import mailScheduler.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntity, UUID> {
    @Modifying
    @Query(value = "insert into category_entity(uuid) select :uuid",nativeQuery = true)
    void save(@Param("uuid") UUID uuid);
}
