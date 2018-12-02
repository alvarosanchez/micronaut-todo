package com.github.alvarosanchez.micronaut.todo.service;

import com.github.alvarosanchez.micronaut.todo.domain.Todo;
import com.github.alvarosanchez.micronaut.todo.domain.TodoRepository;
import com.github.alvarosanchez.micronaut.todo.domain.User;
import com.github.alvarosanchez.micronaut.todo.domain.UserRepository;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class TodoServiceTest {

    @Inject
    TodoService todoService;

    @Inject
    TodoRepository todoRepository;

    @Inject
    UserRepository userRepository;

    @Test
    void testAddTodo() {
        User user = new User("user", "pass");
        userRepository.save(user);

        Assertions.assertIterableEquals(todoRepository.findAllByUser(user), Collections.emptyList());

        Todo todo = todoService.addTodo("user", "Do something").blockingGet();
        Assertions.assertIterableEquals(todoRepository.findAllByUser(user), Collections.singletonList(todo));
    }
}