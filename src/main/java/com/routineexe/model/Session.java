package com.routineexe.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Session {

    private Long id;
    private Long routineId;
    private LocalDate date;
    private Integer dayOfWeekIndex;
    private Boolean sessionDayDone;
    private LocalDateTime createdAt;

    public Session() {
        this.sessionDayDone = false;
        this.createdAt = LocalDateTime.now();
    }

    public Session(Long id, Long routineId, LocalDate date, Integer dayOfWeekIndex, Boolean sessionDayDone, LocalDateTime createdAt) {
        this.id = id;
        this.routineId = routineId;
        this.date = date;
        this.dayOfWeekIndex = dayOfWeekIndex;
        this.sessionDayDone = sessionDayDone != null ? sessionDayDone : false;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoutineId() {
        return routineId;
    }

    public void setRoutineId(Long routineId) {
        this.routineId = routineId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getDayOfWeekIndex() {
        return dayOfWeekIndex;
    }

    public void setDayOfWeekIndex(Integer dayOfWeekIndex) {
        this.dayOfWeekIndex = dayOfWeekIndex;
    }

    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.fromIndex(dayOfWeekIndex != null ? dayOfWeekIndex : 0);
    }

    public Boolean getSessionDayDone() {
        return sessionDayDone != null ? sessionDayDone : false;
    }

    public void setSessionDayDone(Boolean sessionDayDone) {
        this.sessionDayDone = sessionDayDone != null ? sessionDayDone : false;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Session{id=" + id + ", routineId=" + routineId + ", date=" + date + ", dayOfWeek=" + getDayOfWeek() + ", done=" + sessionDayDone + "}";
    }
}