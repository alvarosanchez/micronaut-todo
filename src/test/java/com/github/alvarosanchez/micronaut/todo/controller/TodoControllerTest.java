package com.github.alvarosanchez.micronaut.todo.controller;

import com.github.alvarosanchez.micronaut.todo.TodoClient;
import com.github.alvarosanchez.micronaut.todo.domain.Todo;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@MicronautTest
public class TodoControllerTest {

    @Inject
    TodoClient todoClient;

    @Test
    void testAddTodos() {
        todoClient.signup("user", "pass");
        BearerAccessRefreshToken token = todoClient.login("user", "pass");

        Todo todo = todoClient.addTodo("Bearer " + token.getAccessToken(), "Do something");
        Assertions.assertNotNull(todo.getId());
    }

}
