package com.github.alvarosanchez.micronaut.todo.domain;

import com.github.alvarosanchez.micronaut.todo.AbstractDatabaseTest;
import com.github.alvarosanchez.micronaut.todo.TodoClient;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class AuthenticationTest extends AbstractDatabaseTest {

    @Inject
    TodoClient todoClient;

    @Test
    void testSignupAndLogin() {
        todoClient.signup("user", "pass");
        BearerAccessRefreshToken token = todoClient.login("user", "pass");

        assertNotNull(token.getAccessToken());
    }

}
