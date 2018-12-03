package com.github.alvarosanchez.micronaut.todo.controller;

import com.github.alvarosanchez.micronaut.todo.AbstractDatabaseTest;
import com.github.alvarosanchez.micronaut.todo.TodoClient;
import com.github.alvarosanchez.micronaut.todo.domain.Todo;
import com.github.alvarosanchez.micronaut.todo.event.TodoEvent;
import com.github.alvarosanchez.micronaut.todo.event.TodoEventType;
import io.micronaut.http.sse.Event;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class TodoControllerTest extends AbstractDatabaseTest {

    @Inject
    TodoClient todoClient;

    @Test
    void testAddTodos() {
        String accessToken = signupAndLogin();

        Todo todo = todoClient.addTodo(accessToken, "Do something");
        assertNotNull(todo.getId());
    }

    @Test
    void testCompleteTodos() {
        String accessToken = signupAndLogin();
        Todo todo = todoClient.addTodo(accessToken, "Do something");

        Todo completed = todoClient.complete(accessToken, todo.getId());

        assertTrue(completed.isComplete());
    }

    @Test
    void testListTodos() {
        String accessToken = signupAndLogin();
        Todo todo1 = todoClient.addTodo(accessToken, "Do something");
        Todo todo2 = todoClient.addTodo(accessToken, "Do something else");
        Todo todo3 = todoClient.addTodo(accessToken, "Do something more");

        assertIterableEquals(Arrays.asList(todo1, todo2, todo3), todoClient.listTodos(accessToken));
    }

    @Test
    void testWatchTodos() {
        String accessToken = signupAndLogin();

        Todo todo1 = todoClient.addTodo(accessToken, "Do something");
        Todo todo2 = todoClient.addTodo(accessToken, "Do something else");
        todoClient.complete(accessToken, todo1.getId());

        List<Event<TodoEvent>> events = todoClient.watch(accessToken).take(3).toList().blockingGet();

        assertAll("events",
                () -> assertEquals(3, events.size()),
                () -> assertEquals(TodoEventType.CREATED, events.get(0).getData().getEventType()),
                () -> assertEquals(TodoEventType.CREATED, events.get(1).getData().getEventType()),
                () -> assertEquals(TodoEventType.COMPLETED, events.get(2).getData().getEventType())
        );

        todoClient.complete(accessToken, todo2.getId());
    }

    private String signupAndLogin() {
        todoClient.signup("user", "pass");
        BearerAccessRefreshToken token = todoClient.login("user", "pass");
        return "Bearer " + token.getAccessToken();
    }

}
