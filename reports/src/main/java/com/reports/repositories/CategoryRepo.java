package com.reports.repositories;


import com.reports.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CategoryRepo extends JpaRepository<CategoryEntity, UUID> {
    @Modifying
    @Query(value = "insert into category_entity(uuid) select :uuid",nativeQuery = true)
    void save(@Param("uuid") UUID uuid);
}
