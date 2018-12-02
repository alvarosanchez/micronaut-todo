package com.github.alvarosanchez.micronaut.todo.repository;

import com.github.alvarosanchez.micronaut.todo.domain.Todo;
import com.github.alvarosanchez.micronaut.todo.domain.User;

import java.util.List;

public interface TodoRepository {

    List<Todo> findAllByUser(User user);

    Todo save(Todo todo);

    Todo complete(Todo todo);

}
