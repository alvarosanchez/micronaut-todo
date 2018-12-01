package com.github.alvarosanchez.micronaut.todo.domain;

import io.micronaut.security.authentication.providers.UserFetcher;

public interface UserRepository extends UserFetcher {

    User save(User user);

}
