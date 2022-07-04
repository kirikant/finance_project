package com.schedule.repostories;

import com.schedule.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScheduleRepo extends JpaRepository<ScheduleEntity, UUID> {
}
