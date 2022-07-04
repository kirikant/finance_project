package mailScheduler.entity;

public enum TimeUnit {
    SECOND(1000L),
    MINUTE(60000L),
    HOUR(3600000L),
    DAY(86400000L),
    WEEK(604800000L),
    MONTH(2629743000L),
    YEAR(31556926000L);

    private Long multiplier;

    TimeUnit(Long multiplier) {
        this.multiplier = multiplier;
    }

    public Long getMultiplier() {
        return multiplier;
    }
}
