package com.github.alvarosanchez.micronaut.todo.controller;

import com.github.alvarosanchez.micronaut.todo.AbstractDatabaseTest;
import com.github.alvarosanchez.micronaut.todo.TodoClient;
import com.github.alvarosanchez.micronaut.todo.domain.Todo;
import com.github.alvarosanchez.micronaut.todo.event.TodoEvent;
import com.github.alvarosanchez.micronaut.todo.event.TodoEventType;
import io.micronaut.http.sse.Event;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.annotation.MicronautTest;
import io.reactivex.Flowable;
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
        todoClient.signup("user", "pass");
        BearerAccessRefreshToken token = todoClient.login("user", "pass");
        String accessToken = "Bearer " + token.getAccessToken();

        Todo todo = todoClient.addTodo(accessToken, "Do something");
        assertNotNull(todo.getId());
    }

    @Test
    void testCompleteTodos() {
        todoClient.signup("user", "pass");
        BearerAccessRefreshToken token = todoClient.login("user", "pass");
        String accessToken = "Bearer " + token.getAccessToken();
        Todo todo = todoClient.addTodo(accessToken, "Do something");

        Todo completed = todoClient.complete(accessToken, todo.getId());

        assertTrue(completed.isComplete());
    }

    @Test
    void testListTodos() {
        todoClient.signup("user", "pass");
        BearerAccessRefreshToken token = todoClient.login("user", "pass");
        String accessToken = "Bearer " + token.getAccessToken();
        Todo todo1 = todoClient.addTodo(accessToken, "Do something");
        Todo todo2 = todoClient.addTodo(accessToken, "Do something else");
        Todo todo3 = todoClient.addTodo(accessToken, "Do something more");

        assertIterableEquals(todoClient.listTodos(accessToken), Arrays.asList(todo1, todo2, todo3));
    }

    @Test
    void testWatchTodos() {
        todoClient.signup("user", "pass");
        BearerAccessRefreshToken token = todoClient.login("user", "pass");
        String accessToken = "Bearer " + token.getAccessToken();

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

}
