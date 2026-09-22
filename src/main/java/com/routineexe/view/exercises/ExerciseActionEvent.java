package com.routineexe.view.exercises;

import com.routineexe.model.Exercise;
import javafx.event.Event;
import javafx.event.EventType;

public class ExerciseActionEvent extends Event {

    public static final EventType<ExerciseActionEvent> EDIT = new EventType<>(Event.ANY, "EXERCISE_EDIT");
    public static final EventType<ExerciseActionEvent> DELETE = new EventType<>(Event.ANY, "EXERCISE_DELETE");

    private final Exercise exercise;

    public ExerciseActionEvent(EventType<? extends Event> eventType, Exercise exercise) {
        super(eventType);
        this.exercise = exercise;
    }

    public Exercise getExercise() {
        return exercise;
    }
}