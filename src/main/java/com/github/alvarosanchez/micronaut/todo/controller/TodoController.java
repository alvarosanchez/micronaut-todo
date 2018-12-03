package com.github.alvarosanchez.micronaut.todo.controller;

import com.github.alvarosanchez.micronaut.todo.domain.Todo;
import com.github.alvarosanchez.micronaut.todo.event.TodoEvent;
import com.github.alvarosanchez.micronaut.todo.event.TodoEventType;
import com.github.alvarosanchez.micronaut.todo.service.TodoService;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.sse.Event;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

import javax.annotation.Nullable;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Controller("/todos")
@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
public class TodoController {

    private TodoService todoService;

    private Map<String, BlockingQueue<TodoEvent>> events;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
        this.events = new ConcurrentHashMap<>();
    }

    @Get
    public Flowable<Todo> listTodos(@Nullable Principal principal) {
        return this.todoService.findAllByUser(principal.getName());
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Maybe<Todo> addTodo(@Nullable Principal principal, String text) {
        return this.todoService.addTodo(principal.getName(), text)
                .map(todo -> {
                    System.out.println("Adding event");
                    addEvent(principal.getName(), new TodoEvent(TodoEventType.CREATED, todo));
                    return todo;
                });
    }

    @Put("/{id}")
    public Maybe<Todo> complete(@Nullable Principal principal, @Parameter Long id) {
        return this.todoService
                .complete(id)
                .map(todo -> {
                    addEvent(principal.getName(), new TodoEvent(TodoEventType.COMPLETED, todo));
                    return todo;
                });
    }

    @Get(uri = "/watch")
    public Flowable<Event<TodoEvent>> watch(@Nullable Principal principal) {
        return Flowable.generate(eventEmitter -> eventEmitter.onNext(Event.of(getEvents(principal.getName()).take())));
    }

    private void addEvent(String username, TodoEvent todoEvent) {
        try {
            getEvents(username).put(todoEvent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private BlockingQueue<TodoEvent> getEvents(String username) {
        BlockingQueue<TodoEvent> userEvents = this.events.get(username);
        if (userEvents == null) {
            userEvents = new LinkedBlockingQueue<>();
            this.events.put(username, userEvents);
        }
        return userEvents;
    }

}
