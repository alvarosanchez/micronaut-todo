package com.github.alvarosanchez.micronaut.todo.event;

import com.github.alvarosanchez.micronaut.todo.domain.Todo;

/**
 * An event ocurred with regards to a to-do item
 */
public class TodoEvent {

    private TodoEventType eventType;
    private Todo todo;

    public TodoEvent() {
    }

    public TodoEvent(TodoEventType eventType, Todo todo) {
        this.eventType = eventType;
        this.todo = todo;
    }

    public TodoEventType getEventType() {
        return eventType;
    }

    public void setEventType(TodoEventType eventType) {
        this.eventType = eventType;
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }
}
