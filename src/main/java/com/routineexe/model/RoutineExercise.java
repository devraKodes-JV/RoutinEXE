package com.routineexe.model;

import java.util.Optional;

/**
 * Representa la relación entre una rutina y un ejercicio para un día específico.
 */
public class RoutineExercise {

    private Long id;
    private Routine routine;
    private Exercise exercise;
    private DayOfWeek dayOfWeek;
    private Integer sets;
    private Integer reps;
    private Integer orderIndex;

    public RoutineExercise() {
    }

    public RoutineExercise(Long id, Routine routine, Exercise exercise, DayOfWeek dayOfWeek, Integer sets, Integer reps) {
        this(id, routine, exercise, dayOfWeek, sets, reps, null);
    }

    public RoutineExercise(Long id, Routine routine, Exercise exercise, DayOfWeek dayOfWeek, Integer sets, Integer reps, Integer orderIndex) {
        this.id = id;
        this.routine = routine;
        this.exercise = exercise;
        this.dayOfWeek = dayOfWeek;
        this.sets = sets;
        this.reps = reps;
        this.orderIndex = orderIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Routine getRoutine() {
        return routine;
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }

    public Optional<Routine> getRoutineOptional() {
        return Optional.ofNullable(routine);
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Optional<Exercise> getExerciseOptional() {
        return Optional.ofNullable(exercise);
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Optional<DayOfWeek> getDayOfWeekOptional() {
        return Optional.ofNullable(dayOfWeek);
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public Optional<Integer> getSetsOptional() {
        return Optional.ofNullable(sets);
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Optional<Integer> getRepsOptional() {
        return Optional.ofNullable(reps);
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Optional<Integer> getOrderIndexOptional() {
        return Optional.ofNullable(orderIndex);
    }

    @Override
    public String toString() {
        return "RoutineExercise{id=" + id + ", routine=" + (routine != null ? routine.getName() : "null")
                + ", exercise=" + (exercise != null ? exercise.getName() : "null")
                + ", dayOfWeek=" + dayOfWeek + ", sets=" + sets + ", reps=" + reps
                + ", orderIndex=" + orderIndex + "}";
    }
}