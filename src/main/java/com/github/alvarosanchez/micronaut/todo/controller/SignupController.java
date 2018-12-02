package com.github.alvarosanchez.micronaut.todo.controller;

import com.github.alvarosanchez.micronaut.todo.domain.User;
import com.github.alvarosanchez.micronaut.todo.repository.UserRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;

import javax.validation.constraints.NotNull;

@Controller("/")
@Validated
@Secured(SecurityRule.IS_ANONYMOUS)
public class SignupController {

    private UserRepository userRepository;

    public SignupController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Post("/signup")
    @Status(HttpStatus.CREATED)
    public void signup(@NotNull String username, @NotNull String password) {
        this.userRepository.save(new User(username, password));
    }

}
