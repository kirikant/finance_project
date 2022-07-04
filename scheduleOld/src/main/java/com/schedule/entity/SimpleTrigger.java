package com.schedule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "quartz",name = "qrtz_simple_triggers")
public class SimpleTrigger {

    @Column(name = "sched_name")
    private  String schedName;
    @Id
    @Column(name = "trigger_name")
    private  String triggerName;
    @Column(name = "trigger_group")
    private  String triggerGroup;
    @Column(name = "repeat_count")
    private  Long  repeatCount;
    @Column(name = "repeat_interval")
    private  Long repeatInterval;
    @Column(name = "times_triggered")
    private  Long timesTriggered;

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public Long getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(Long repeatCount) {
        this.repeatCount = repeatCount;
    }

    public Long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(Long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public Long getTimesTriggered() {
        return timesTriggered;
    }

    public void setTimesTriggered(Long timesTriggered) {
        this.timesTriggered = timesTriggered;
    }
}
