package mailScheduler.service;

import mailScheduler.dto.ScheduleDto;
import mailScheduler.dto.ScheduledReportDto;
import mailScheduler.entity.ReportParamEntity;
import mailScheduler.entity.ScheduleEntity;
import mailScheduler.entity.ScheduledReportEntity;
import mailScheduler.repositories.ScheduledReportRepo;
import mailScheduler.security.JwtTokenProvider;
import mailScheduler.service.scheduler.ScheduleJob;
import mailScheduler.service.scheduler.SchedulerJobService;
import mailScheduler.util.Mapper;
import mailScheduler.util.convertors.RepParEntityToDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class ScheduledReportService {

    private final Mapper mapper;
    private final ReportParamService reportParamService;
    private final ScheduleService scheduleService;
    private final ScheduledReportRepo scheduledReportRepo;
    private final SchedulerJobService schedulerJobService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RepParEntityToDto repParEntityToDto;

    public ScheduledReportService(Mapper mapper, ReportParamService reportParamService,
                                  ScheduleService scheduleService, ScheduledReportRepo scheduledReportRepo,
                                  SchedulerJobService schedulerJobService, JwtTokenProvider jwtTokenProvider,
                                  RepParEntityToDto repParEntityToDto) {
        this.mapper = mapper;
        this.reportParamService = reportParamService;
        this.scheduleService = scheduleService;
        this.scheduledReportRepo = scheduledReportRepo;
        this.schedulerJobService = schedulerJobService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.repParEntityToDto = repParEntityToDto;
    }

    @Transactional
    public void createScheduledReport(ScheduledReportDto scheduledReportDto, String token) {

        String username = jwtTokenProvider.getUsername(token);
        LocalDateTime now = LocalDateTime.now();
        ScheduledReportEntity scheduledReportEntity = mapper.map(scheduledReportDto, ScheduledReportEntity.class);

        ReportParamEntity reportParamEntity = reportParamService
                .save(scheduledReportDto.getReportParamDto(), token);
        ScheduleEntity scheduleEntity = scheduleService.
                save(scheduledReportDto.getScheduleDto(), now);

        scheduledReportEntity.setReportParamEntity(reportParamEntity);
        scheduledReportEntity.setScheduleEntity(scheduleEntity);
        scheduledReportEntity.setDtCreate(now);
        scheduledReportEntity.setUser(username);

        ScheduledReportEntity savedScheduledReportEntity = scheduledReportRepo.save(scheduledReportEntity);
        schedulerJobService.schedule(ScheduleJob.class, savedScheduledReportEntity, token);
    }


    public Page<ScheduledReportDto> getScheduledReports(Integer page, Integer size, String token) {
        String username = jwtTokenProvider.getUsername(token);
        return scheduledReportRepo.getAllByUser(PageRequest.of(page, size, Sort.Direction.DESC, "dtCreate"),
                username).map(x -> {
            ScheduledReportDto scheduledReportDto = mapper.map(x, ScheduledReportDto.class);

            scheduledReportDto.setReportParamDto(repParEntityToDto.convert(x.getReportParamEntity()));
            scheduledReportDto.setScheduleDto(mapper.map(x.getScheduleEntity(), ScheduleDto.class));

            return scheduledReportDto;
        });
    }


}
