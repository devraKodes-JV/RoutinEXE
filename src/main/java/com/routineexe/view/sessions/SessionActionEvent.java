package com.routineexe.view.sessions;

import com.routineexe.model.Session;
import javafx.event.Event;
import javafx.event.EventType;

public class SessionActionEvent extends Event {

    public static final EventType<SessionActionEvent> CREATE = new EventType<>(Event.ANY, "SESSION_CREATE");
    public static final EventType<SessionActionEvent> EDIT = new EventType<>(Event.ANY, "SESSION_EDIT");
    public static final EventType<SessionActionEvent> DELETE = new EventType<>(Event.ANY, "SESSION_DELETE");
    public static final EventType<SessionActionEvent> TOGGLE_DONE = new EventType<>(Event.ANY, "SESSION_TOGGLE_DONE");

    private final Session session;

    public SessionActionEvent(EventType<SessionActionEvent> eventType, Session session) {
        super(eventType);
        this.session = session;
    }

    public Session getSession() {
        return session;
    }
}