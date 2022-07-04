package mailScheduler.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import mailScheduler.entity.TimeUnit;
import mailScheduler.util.ScheduleDtoDeserializer;
import mailScheduler.util.ScheduleDtoSerializer;

import java.time.LocalDateTime;

@JsonDeserialize(using = ScheduleDtoDeserializer.class)
@JsonSerialize(using = ScheduleDtoSerializer.class)
public class ScheduleDto {


    private LocalDateTime startTime;
    private Long interval;
    private LocalDateTime stopTime;
    private TimeUnit timeUnit;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public LocalDateTime getStopTime() {
        return stopTime;
    }

    public void setStopTime(LocalDateTime stopTime) {
        this.stopTime = stopTime;
    }
}
