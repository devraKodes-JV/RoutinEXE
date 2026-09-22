package com.routineexe.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.BooleanProperty;

public class SessionExercise {

    private Long id;
    private Long sessionId;
    private Long exerciseId;
    private Integer sets;
    private Integer reps;
    private Double weight;
    private BooleanProperty done;

    public SessionExercise() {
        this.done = new SimpleBooleanProperty(false);
    }

    public SessionExercise(Long id, Long sessionId, Long exerciseId, Integer sets, Integer reps, Double weight, Boolean done) {
        this.id = id;
        this.sessionId = sessionId;
        this.exerciseId = exerciseId;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.done = new SimpleBooleanProperty(done != null ? done : false);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Boolean getDone() {
        return done.get();
    }

    public void setDone(Boolean done) {
        this.done.set(done != null ? done : false);
    }

    public BooleanProperty doneProperty() {
        return done;
    }

    @Override
    public String toString() {
        return "SessionExercise{id=" + id + ", sessionId=" + sessionId + ", exerciseId=" + exerciseId + ", sets=" + sets + ", reps=" + reps + ", weight=" + weight + ", done=" + done.get() + "}";
    }
}