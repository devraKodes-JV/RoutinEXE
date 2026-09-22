package com.routineexe.view.routines;

import com.routineexe.model.Routine;
import javafx.event.Event;
import javafx.event.EventType;

public class RoutineActionEvent extends Event {

    public static final EventType<RoutineActionEvent> EDIT = new EventType<>(Event.ANY, "ROUTINE_EDIT");
    public static final EventType<RoutineActionEvent> ASSIGN = new EventType<>(Event.ANY, "ROUTINE_ASSIGN");
    public static final EventType<RoutineActionEvent> DELETE = new EventType<>(Event.ANY, "ROUTINE_DELETE");

    private final Routine routine;

    public RoutineActionEvent(EventType<? extends Event> eventType, Routine routine) {
        super(eventType);
        this.routine = routine;
    }

    public Routine getRoutine() {
        return routine;
    }
}