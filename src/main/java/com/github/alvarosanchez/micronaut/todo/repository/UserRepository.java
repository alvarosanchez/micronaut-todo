package com.github.alvarosanchez.micronaut.todo.repository;

import com.github.alvarosanchez.micronaut.todo.domain.User;
import io.micronaut.security.authentication.providers.AuthoritiesFetcher;
import io.micronaut.security.authentication.providers.UserFetcher;

public interface UserRepository extends UserFetcher, AuthoritiesFetcher {

    User save(User user);

}
