package mailScheduler.service;

import mailScheduler.dto.ScheduleDto;
import mailScheduler.entity.ScheduleEntity;
import mailScheduler.repositories.ScheduleRepo;
import mailScheduler.util.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ScheduleService {

    private final Mapper mapper;
    private final ScheduleRepo scheduleRepo;

    public ScheduleService(Mapper mapper, ScheduleRepo scheduleRepo) {
        this.mapper = mapper;
        this.scheduleRepo = scheduleRepo;
    }

    @Transactional
    public ScheduleEntity save(ScheduleDto scheduleDto, LocalDateTime now){
        ScheduleEntity scheduleEntity = mapper.map(scheduleDto,ScheduleEntity.class);
        scheduleEntity.setDtCreate(now);
        return scheduleRepo.save(scheduleEntity);
    }

}
