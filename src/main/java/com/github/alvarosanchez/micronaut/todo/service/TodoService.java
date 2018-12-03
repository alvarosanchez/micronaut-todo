package com.github.alvarosanchez.micronaut.todo.service;

import com.github.alvarosanchez.micronaut.todo.domain.Todo;
import com.github.alvarosanchez.micronaut.todo.domain.User;
import com.github.alvarosanchez.micronaut.todo.event.TodoEvent;
import com.github.alvarosanchez.micronaut.todo.event.TodoEventType;
import com.github.alvarosanchez.micronaut.todo.repository.TodoRepository;
import com.github.alvarosanchez.micronaut.todo.repository.UserRepository;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Service helper for operations regarding a {@link Todo}
 */
@Singleton
public class TodoService {

    private TodoRepository todoRepository;
    private UserRepository userRepository;
    private Map<String, BlockingQueue<TodoEvent>> events;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.events = new ConcurrentHashMap<>();
    }

    /**
     * Inserts a {@link Todo}
     */
    public Maybe<Todo> addTodo(String username, String text) {
        Todo todo = new Todo(text);
        return Single.fromPublisher(userRepository.findByUsername(username))
                .map(todo::withUserState)
                .map(todoRepository::save)
                .doOnSuccess(todo1 -> publishEvent(username, new TodoEvent(TodoEventType.CREATED, todo1)))
                .toMaybe();
    }

    /**
     * Marks the to-do with the given ID as complete
     */
    public Maybe<Todo> complete(Long todoId) {
        Todo todo = new Todo();
        todo.setId(todoId);
        return Maybe.just(todoRepository.complete(todo))
                .doOnSuccess(todo1 -> publishEvent(todo1.getUser().getUsername(), new TodoEvent(TodoEventType.COMPLETED, todo1)));

    }

    /**
     * Finds all to-do's for the given user
     */
    public Flowable<Todo> findAllByUser(String username) {
        return Single.fromPublisher(userRepository.findByUsername(username))
                .map(userState -> todoRepository.findAllByUser((User) userState))
                .flattenAsFlowable(todos -> todos);
    }

    /**
     * Publishes an event for the given username
     */
    public void publishEvent(String username, TodoEvent todoEvent) {
        try {
            getEvents(username).put(todoEvent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds all the un-consumed events for the given username
     */
    public BlockingQueue<TodoEvent> getEvents(String username) {
        BlockingQueue<TodoEvent> userEvents = this.events.get(username);
        if (userEvents == null) {
            userEvents = new LinkedBlockingQueue<>();
            this.events.put(username, userEvents);
        }
        return userEvents;
    }

}
