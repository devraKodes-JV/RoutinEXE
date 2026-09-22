package com.routineexe.view.categories;

import com.routineexe.model.Category;
import javafx.event.Event;
import javafx.event.EventType;

public class CategoryActionEvent extends Event {

    public static final EventType<CategoryActionEvent> EDIT = new EventType<>(Event.ANY, "CATEGORY_EDIT");
    public static final EventType<CategoryActionEvent> DELETE = new EventType<>(Event.ANY, "CATEGORY_DELETE");

    private final Category category;

    public CategoryActionEvent(EventType<? extends Event> eventType, Category category) {
        super(eventType);
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }
}