package com.schedule.entity;

public enum Property {
    READ("property:read"),
    WRITE("property:write"),
    REFACTOR("property:refactor"),
    SPECIAL("property:special");

    private String property;

    Property(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}