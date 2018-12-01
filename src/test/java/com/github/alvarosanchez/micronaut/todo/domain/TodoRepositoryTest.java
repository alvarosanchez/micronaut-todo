package com.github.alvarosanchez.micronaut.todo.domain;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@MicronautTest
public class TodoRepositoryTest {

    @Inject
    TodoRepository todoRepository;

    @Inject
    UserRepository userRepository;

    @Test
    void testSaveTodo() {
        User user = new User("user", "pass");
        user = userRepository.save(user);

        Todo todo = new Todo("Do something", false, user);
        todo = todoRepository.save(todo);

        Assertions.assertNotNull(todo.getId());
    }

    @Test
    void testFindAllByUser() {
        User user1 = userRepository.save(new User("user1", "pass"));
        User user2 = userRepository.save(new User("user2", "pass"));

        Todo todo1 = todoRepository.save(new Todo("Todo 1", false, user1));
        Todo todo2 = todoRepository.save(new Todo("Todo 2", false, user1));
        Todo todo3 = todoRepository.save(new Todo("Todo 3", false, user1));

        todoRepository.save(new Todo("Todo 4", false, user2));
        todoRepository.save(new Todo("Todo 5", false, user2));

        List<Todo> user1Todos = todoRepository.findAllByUser(user1);
        Assertions.assertIterableEquals(user1Todos, Arrays.asList(todo1, todo2, todo3));
    }

    @Test
    void testComplete() {
        User user = new User("user", "pass");
        user = userRepository.save(user);

        Todo todo = new Todo("Do something", false, user);
        todo = todoRepository.save(todo);

        todoRepository.complete(todo);

        Assertions.assertTrue(todoRepository.findAllByUser(user).get(0).isComplete());
    }

}