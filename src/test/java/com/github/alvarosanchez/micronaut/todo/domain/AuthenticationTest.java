package com.github.alvarosanchez.micronaut.todo.domain;

import com.github.alvarosanchez.micronaut.todo.AbstractDatabaseTest;
import com.github.alvarosanchez.micronaut.todo.TodoClient;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@MicronautTest
public class AuthenticationTest extends AbstractDatabaseTest {

    @Inject
    TodoClient todoClient;

    @Inject
    UserRepository userRepository;

    @Test
    void testSignupAndLogin() {
        todoClient.signup("user", "pass");
        BearerAccessRefreshToken token = todoClient.login("user", "pass");

        Assertions.assertNotNull(token.getAccessToken());
    }

}
