package com.github.alvarosanchez.micronaut.todo.domain;

import io.micronaut.security.authentication.providers.UserState;

import javax.persistence.*;

@Entity
public class User implements UserState {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    @Column(name = "passwd")
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Enabled by default
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Non expired by default
     */
    @Override
    public boolean isAccountExpired() {
        return false;
    }

    /**
     * Non locked by default
     */
    @Override
    public boolean isAccountLocked() {
        return false;
    }

    /**
     * Password not expired by default
     */
    @Override
    public boolean isPasswordExpired() {
        return false;
    }
}
