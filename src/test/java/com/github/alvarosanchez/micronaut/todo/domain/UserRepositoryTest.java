package com.github.alvarosanchez.micronaut.todo.domain;

import io.micronaut.security.authentication.providers.UserState;
import io.micronaut.test.annotation.MicronautTest;
import io.reactivex.Flowable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@MicronautTest
class UserRepositoryTest {

    @Inject
    UserRepository userRepository;

    @Inject
    DataSource dataSource;

    @AfterEach
    void cleanup() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.createStatement().executeUpdate("delete from todo");
        connection.createStatement().executeUpdate("delete from user");
        connection.close();
    }

    @Test
    void testSaveUsers() {
        User user = new User("user", "pass");
        user = userRepository.save(user);
        Assertions.assertNotNull(user.getId());
    }

    @Test
    void testFindUsers() {
        User user = new User("user", "pass");
        userRepository.save(user);

        UserState userState = Flowable.fromPublisher(userRepository.findByUsername("user")).blockingFirst();
        Assertions.assertNotNull(((User)userState).getId());
        Assertions.assertEquals("user", userState.getUsername());
        Assertions.assertNotNull(userState.getPassword());
    }

    @Test
    void testPasswordsAreEncoded(){
        User user = new User("user", "pass");
        user = userRepository.save(user);

        Assertions.assertNotEquals("pass", user.getPassword());
    }

}