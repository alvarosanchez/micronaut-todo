package com.github.alvarosanchez.micronaut.todo.controller;

import com.github.alvarosanchez.micronaut.todo.domain.Todo;
import com.github.alvarosanchez.micronaut.todo.event.TodoEvent;
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

@Controller("/todos")
@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
public class TodoController {

    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Get
    public Flowable<Todo> listTodos(@Nullable Principal principal) {
        return this.todoService.findAllByUser(principal.getName());
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Maybe<Todo> addTodo(@Nullable Principal principal, String text) {
        return this.todoService.addTodo(principal.getName(), text);
    }

    @Put("/{id}")
    public Maybe<Todo> complete(@Nullable Principal principal, @Parameter Long id) {
        return this.todoService.complete(id);
    }

    @Get(uri = "/watch")
    public Flowable<Event<TodoEvent>> watch(@Nullable Principal principal) {
        return Flowable.generate(eventEmitter -> eventEmitter.onNext(Event.of(todoService.getEvents(principal.getName()).take())));
    }

}
