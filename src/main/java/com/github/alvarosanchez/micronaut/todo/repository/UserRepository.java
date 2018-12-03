package com.github.alvarosanchez.micronaut.todo.repository;

import com.github.alvarosanchez.micronaut.todo.domain.User;
import io.micronaut.security.authentication.providers.AuthoritiesFetcher;
import io.micronaut.security.authentication.providers.UserFetcher;

/**
 * Repository definition for {@link User}
 */
public interface UserRepository extends UserFetcher, AuthoritiesFetcher {

    /**
     * Stores the given user in the database
     */
    User save(User user);

}
