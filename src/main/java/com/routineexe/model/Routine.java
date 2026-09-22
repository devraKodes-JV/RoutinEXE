package com.routineexe.model;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

/**
 * Representa una rutina de ejercicios.
 */
public class Routine {

    private Long id;
    private Long userId;
    private String name;
    private LocalDate since;
    private LocalDate until;
    private Set<DayOfWeek> days;

    public Routine() {
        this.days = EnumSet.noneOf(DayOfWeek.class);
    }

    public Routine(Long id, Long userId, String name, LocalDate since, LocalDate until, Set<DayOfWeek> days) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.since = since;
        this.until = until;
        this.days = days != null ? EnumSet.copyOf(days) : EnumSet.noneOf(DayOfWeek.class);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getSince() {
        return since;
    }

    public void setSince(LocalDate since) {
        this.since = since;
    }

    public Optional<LocalDate> getSinceOptional() {
        return Optional.ofNullable(since);
    }

    public LocalDate getUntil() {
        return until;
    }

    public void setUntil(LocalDate until) {
        this.until = until;
    }

    public Optional<LocalDate> getUntilOptional() {
        return Optional.ofNullable(until);
    }

    public Set<DayOfWeek> getDays() {
        return days;
    }

    public void setDays(Set<DayOfWeek> days) {
        this.days = days != null ? EnumSet.copyOf(days) : EnumSet.noneOf(DayOfWeek.class);
    }

    public void addDay(DayOfWeek day) {
        if (day != null) {
            this.days.add(day);
        }
    }

    public void removeDay(DayOfWeek day) {
        if (day != null) {
            this.days.remove(day);
        }
    }

    public boolean hasDay(DayOfWeek day) {
        return day != null && this.days.contains(day);
    }

    @Override
    public String toString() {
        return "Routine{id=" + id + ", name='" + name + "', since=" + since + ", until=" + until + ", days=" + days + "}";
    }
}