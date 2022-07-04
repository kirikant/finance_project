package mailScheduler.repositories;

import mailScheduler.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScheduleRepo extends JpaRepository<ScheduleEntity, UUID> {

}
