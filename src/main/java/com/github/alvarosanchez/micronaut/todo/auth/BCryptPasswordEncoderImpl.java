package com.github.alvarosanchez.micronaut.todo.auth;

import io.micronaut.security.authentication.providers.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Singleton;

/**
 * A {@link PasswordEncoder} that delegates to {@link BCryptPasswordEncoder}
 */
@Singleton
public class BCryptPasswordEncoderImpl implements PasswordEncoder {

    org.springframework.security.crypto.password.PasswordEncoder delegate = new BCryptPasswordEncoder();

    /**
     * @param rawPassword The plain text password
     * @return The result of encoding the password
     */
    @Override
    public String encode(String rawPassword) {
        return delegate.encode(rawPassword);
    }

    /**
     * @param rawPassword     The plain text password
     * @param encodedPassword The encoded password to match against
     * @return true if the passwords match
     */
    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return delegate.matches(rawPassword, encodedPassword);
    }
}
