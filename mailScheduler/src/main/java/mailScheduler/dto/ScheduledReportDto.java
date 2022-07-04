package mailScheduler.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import mailScheduler.entity.ReportType;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduledReportDto {

    private ReportParamDto reportParamDto;
    private ScheduleDto scheduleDto;
    private LocalDateTime dtCreate;
    private LocalDateTime dtUpdate;
    private ReportType reportType;

    public ScheduleDto getScheduleDto() {
        return scheduleDto;
    }

    public void setScheduleDto(ScheduleDto scheduleDto) {
        this.scheduleDto = scheduleDto;
    }


    public ReportParamDto getReportParamDto() {
        return reportParamDto;
    }

    public void setReportParamDto(ReportParamDto reportParamDto) {
        this.reportParamDto = reportParamDto;
    }

    public LocalDateTime getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(LocalDateTime dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    public LocalDateTime getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(LocalDateTime dtCreate) {
        this.dtCreate = dtCreate;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }
}
