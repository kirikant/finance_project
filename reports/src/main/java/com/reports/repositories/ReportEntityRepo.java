package com.reports.repositories;

import com.reports.entity.ReportEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReportEntityRepo extends JpaRepository<ReportEntity, UUID> {
    @EntityGraph("report-reportParam")
    Page<ReportEntity> findAllByUser (Pageable pageable, String username);
    @EntityGraph("report-reportParam")
    Optional<ReportEntity> findByUuidAndUser (UUID uuid, String username);
}
