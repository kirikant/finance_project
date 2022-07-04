package mailScheduler.repositories;

import mailScheduler.entity.ReportParamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReportParamRepo extends JpaRepository<ReportParamEntity, UUID> {
}
