package com.github.alvarosanchez.micronaut.todo.service;

import com.github.alvarosanchez.micronaut.todo.domain.Todo;
import com.github.alvarosanchez.micronaut.todo.domain.User;
import com.github.alvarosanchez.micronaut.todo.repository.TodoRepository;
import com.github.alvarosanchez.micronaut.todo.repository.UserRepository;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.inject.Singleton;

@Singleton
public class TodoService {

    private TodoRepository todoRepository;
    private UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public Maybe<Todo> addTodo(String username, String text) {
        Todo todo = new Todo(text);
        return Single.fromPublisher(userRepository.findByUsername(username))
                .map(todo::withUserState)
                .map(todoRepository::save)
                .toMaybe();
    }

    public Maybe<Todo> complete(Long todoId) {
        Todo todo = new Todo();
        todo.setId(todoId);
        return Maybe.just(todoRepository.complete(todo));
    }

    public Flowable<Todo> findAllByUser(String username) {
        return Single.fromPublisher(userRepository.findByUsername(username))
                .map(userState -> todoRepository.findAllByUser((User) userState))
                .flattenAsFlowable(todos -> todos);
    }

}
