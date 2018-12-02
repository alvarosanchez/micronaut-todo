package com.github.alvarosanchez.micronaut.todo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.security.authentication.providers.UserState;

import javax.persistence.*;

@Entity
public class User implements UserState {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
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
    @JsonIgnore
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
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    /**
     * Non expired by default
     */
    @Override
    @JsonIgnore
    public boolean isAccountExpired() {
        return false;
    }

    /**
     * Non locked by default
     */
    @Override
    @JsonIgnore
    public boolean isAccountLocked() {
        return false;
    }

    /**
     * Password not expired by default
     */
    @Override
    @JsonIgnore
    public boolean isPasswordExpired() {
        return false;
    }
}
