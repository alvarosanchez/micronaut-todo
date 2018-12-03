package com.github.alvarosanchez.micronaut.todo.repository;

import com.github.alvarosanchez.micronaut.todo.domain.Todo;
import com.github.alvarosanchez.micronaut.todo.domain.User;

import java.util.List;

/**
 * Repository definition for {@link Todo}
 */
public interface TodoRepository {

    /**
     * Finds all todos for the given user
     */
    List<Todo> findAllByUser(User user);

    /**
     * Stores the given to-do into the database
     */
    Todo save(Todo todo);

    /**
     * Marks the given to-do as complete
     */
    Todo complete(Todo todo);

}
