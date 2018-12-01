package com.github.alvarosanchez.micronaut.todo.domain;

import io.micronaut.security.authentication.providers.UserState;
import io.micronaut.spring.tx.annotation.Transactional;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class UserRepositoryJpaImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public User save(User user) {
        this.entityManager.persist(user);
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
}
