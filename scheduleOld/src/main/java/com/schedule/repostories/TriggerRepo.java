package com.schedule.repostories;

import com.schedule.entity.TriggerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TriggerRepo extends JpaRepository<TriggerEntity,String> {

}
