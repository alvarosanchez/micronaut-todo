package com.github.alvarosanchez.micronaut.todo.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void testEquals() {
        Todo todo1 = new Todo("text");
        todo1.setId(1L);

        Todo todo2 = new Todo("text");
        todo2.setId(1L);

        Todo todo3 = new Todo("text3");
        todo3.setId(1L);

        Todo todo4 = new Todo("text");
        todo4.setId(4L);

        assertAll("todo",
            () -> assertEquals(todo1, todo1),
            () -> assertEquals(todo1, todo2),

            () -> assertNotEquals(todo1, todo3),
            () -> assertNotEquals(todo1, todo4),

            () -> assertNotEquals(todo1, null),
            () -> assertNotEquals(todo1, new Object()),
            () -> assertNotEquals(todo1, new Todo("text"))
        );
    }

}