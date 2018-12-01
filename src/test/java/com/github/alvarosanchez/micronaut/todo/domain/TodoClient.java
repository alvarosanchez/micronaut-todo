package com.github.alvarosanchez.micronaut.todo.domain;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;

@Client("/")
public interface TodoClient {

    @Post("/signup")
    HttpResponse signup(String username, String password);

    @Post("/login")
    BearerAccessRefreshToken login(String username, String password);

}
