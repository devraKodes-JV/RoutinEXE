package com.routineexe.view.users;

import com.routineexe.model.User;
import javafx.event.Event;
import javafx.event.EventType;

public class UserActionEvent extends Event {

    public static final EventType<UserActionEvent> EDIT = new EventType<>(Event.ANY, "USER_EDIT");
    public static final EventType<UserActionEvent> DELETE = new EventType<>(Event.ANY, "USER_DELETE");

    private final User user;

    public UserActionEvent(EventType<? extends Event> eventType, User user) {
        super(eventType);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}