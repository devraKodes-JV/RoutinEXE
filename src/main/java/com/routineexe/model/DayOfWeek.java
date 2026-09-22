package com.routineexe.model;

public enum DayOfWeek {
    MONDAY("Monday", "Mon"),
    TUESDAY("Tuesday", "Tue"),
    WEDNESDAY("Wednesday", "Wed"),
    THURSDAY("Thursday", "Thu"),
    FRIDAY("Friday", "Fri"),
    SATURDAY("Saturday", "Sat"),
    SUNDAY("Sunday", "Sun");

    private final String fullName;
    private final String shortName;

    DayOfWeek(String fullName, String shortName) {
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public int getIndex() {
        return ordinal();
    }

    public static DayOfWeek fromIndex(int index) {
        if (index >= 0 && index < values().length) {
            return values()[index];
        }
        return null;
    }

    @Override
    public String toString() {
        return fullName;
    }
}