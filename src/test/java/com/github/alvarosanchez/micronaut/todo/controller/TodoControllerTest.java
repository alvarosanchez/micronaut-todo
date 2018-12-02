package com.github.alvarosanchez.micronaut.todo.controller;

import com.github.alvarosanchez.micronaut.todo.AbstractDatabaseTest;
import com.github.alvarosanchez.micronaut.todo.TodoClient;
import com.github.alvarosanchez.micronaut.todo.domain.Todo;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class TodoControllerTest extends AbstractDatabaseTest {

    @Inject
    TodoClient todoClient;

    @Test
    void testAddTodos() {
        todoClient.signup("user", "pass");
        BearerAccessRefreshToken token = todoClient.login("user", "pass");

        Todo todo = todoClient.addTodo("Bearer " + token.getAccessToken(), "Do something");
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

}
