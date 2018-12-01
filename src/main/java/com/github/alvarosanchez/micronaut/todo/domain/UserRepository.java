package com.github.alvarosanchez.micronaut.todo.domain;

import io.micronaut.security.authentication.providers.AuthoritiesFetcher;
import io.micronaut.security.authentication.providers.UserFetcher;

public interface UserRepository extends UserFetcher, AuthoritiesFetcher {

    User save(User user);

}
