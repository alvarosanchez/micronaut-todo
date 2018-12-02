package com.github.alvarosanchez.micronaut.todo.repository;

import com.github.alvarosanchez.micronaut.todo.domain.User;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.security.authentication.providers.PasswordEncoder;
import io.micronaut.security.authentication.providers.UserState;
import io.micronaut.spring.tx.annotation.Transactional;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

@Singleton
public class UserRepositoryJpaImpl implements UserRepository {

    private EntityManager entityManager;
    private PasswordEncoder passwordEncoder;

    public UserRepositoryJpaImpl(@CurrentSession EntityManager entityManager, PasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.entityManager.persist(user);
        this.entityManager.flush();
        return user;
    }

    /**
     * Fetches a user based on the username.
     *
     * @param username e.g. admin
     * @return The users information or an empty publisher if no user is present
     */
    @Override
    @Transactional(readOnly = true)
    public Publisher<UserState> findByUsername(String username) {
        User user = this.entityManager
                .createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
        return Flowable.just(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Publisher<List<String>> findAuthoritiesByUsername(String username) {
        return Flowable.just(Collections.singletonList("ROLE_USER"));
    }
}
