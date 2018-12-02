package com.github.alvarosanchez.micronaut.todo.domain;

import com.github.alvarosanchez.micronaut.todo.AbstractDatabaseTest;
import com.github.alvarosanchez.micronaut.todo.repository.UserRepository;
import io.micronaut.security.authentication.providers.UserState;
import io.micronaut.test.annotation.MicronautTest;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class UserRepositoryTest extends AbstractDatabaseTest {

    @Inject
    UserRepository userRepository;

    @Test
    void testSaveUsers() {
        User user = new User("user", "pass");
        user = userRepository.save(user);

        assertNotNull(user.getId());
    }

    @Test
    void testFindUsers() {
        User user = new User("user", "pass");
        userRepository.save(user);

        UserState userState = Flowable.fromPublisher(userRepository.findByUsername("user")).blockingFirst();

        assertAll("user",
            () -> assertNotNull(((User)userState).getId()),
            () -> assertEquals("user", userState.getUsername()),
            () -> assertNotNull(userState.getPassword())
        );

    }

    @Test
    void testPasswordsAreEncoded(){
        User user = new User("user", "pass");
        user = userRepository.save(user);

        assertNotEquals("pass", user.getPassword());
    }

}