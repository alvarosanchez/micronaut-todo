package com.github.alvarosanchez.micronaut.todo.controller;

import com.github.alvarosanchez.micronaut.todo.domain.Todo;
import com.github.alvarosanchez.micronaut.todo.service.TodoService;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import io.reactivex.Maybe;

import javax.annotation.Nullable;
import java.security.Principal;

@Controller("/todo")
@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
public class TodoController {

    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Maybe<Todo> addTodo(@Nullable Principal principal, String text) {
        return this.todoService.addTodo(principal.getName(), text);
    }
}
