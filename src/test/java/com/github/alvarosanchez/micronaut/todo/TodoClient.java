package com.github.alvarosanchez.micronaut.todo;

import com.github.alvarosanchez.micronaut.todo.domain.Todo;
import com.github.alvarosanchez.micronaut.todo.event.TodoEvent;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.sse.Event;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.reactivex.Flowable;

import java.util.List;

@Client("/")
public interface TodoClient {

    @Post("/signup")
    HttpResponse signup(String username, String password);

    @Post("/login")
    BearerAccessRefreshToken login(String username, String password);

    @Get("/todos")
    List<Todo> listTodos(@Header("Authorization") String accessToken);

    @Post("/todos")
    Todo addTodo(@Header("Authorization") String accessToken, String text);

    @Put("/todos/{id}")
    Todo complete(@Header("Authorization") String accessToken, @Parameter Long id);

    @Get(value = "/todos/watch", processes = MediaType.TEXT_EVENT_STREAM)
    Flowable<Event<TodoEvent>> watch(@Header("Authorization") String accessToken);

}
