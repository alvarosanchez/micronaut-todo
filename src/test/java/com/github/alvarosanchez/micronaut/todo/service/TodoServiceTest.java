package com.github.alvarosanchez.micronaut.todo.service;

import com.github.alvarosanchez.micronaut.todo.AbstractDatabaseTest;
import com.github.alvarosanchez.micronaut.todo.domain.Todo;
import com.github.alvarosanchez.micronaut.todo.domain.TodoRepository;
import com.github.alvarosanchez.micronaut.todo.domain.User;
import com.github.alvarosanchez.micronaut.todo.domain.UserRepository;
import io.micronaut.test.annotation.MicronautTest;
import io.reactivex.Maybe;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class TodoServiceTest extends AbstractDatabaseTest {

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

        assertIterableEquals(todoRepository.findAllByUser(user), Collections.emptyList());

        Todo todo = todoService.addTodo("user", "Do something").blockingGet();
        assertIterableEquals(todoRepository.findAllByUser(user), Collections.singletonList(todo));
    }

    @Test
    void testComplete() {
        User user = new User("user", "pass");
        userRepository.save(user);
        Todo todo = todoService.addTodo("user", "Do something").blockingGet();

        Todo completed = todoService.complete(todo.getId()).blockingGet();
        assertTrue(completed.isComplete());
    }

    @Test
    void testFindAllByUser() {
        User user = new User("user", "pass");
        userRepository.save(user);

        List<Todo> added = Maybe.zip(
                todoService.addTodo("user", "Do something"),
                todoService.addTodo("user", "Do something else"),
                todoService.addTodo("user", "Do something more"),
                (todo, todo2, todo3) -> Arrays.asList(todo, todo2, todo3)
        ).blockingGet();

        assertIterableEquals(todoService.findAllByUser("user").blockingIterable(), added);
    }
}