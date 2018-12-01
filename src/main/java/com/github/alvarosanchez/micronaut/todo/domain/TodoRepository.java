package com.github.alvarosanchez.micronaut.todo.domain;

import java.util.List;

public interface TodoRepository {

    List<Todo> findAllByUser(User user);

    Todo save(Todo todo);

    Todo complete(Todo todo);

}
