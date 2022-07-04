package mailScheduler.repositories;

import mailScheduler.entity.ScheduledReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScheduledReportRepo  extends JpaRepository<ScheduledReportEntity, UUID> {
     @EntityGraph("scheduledReport-schedule,reportParam")
     Page<ScheduledReportEntity> getAllByUser(Pageable pageable,String user);
}
