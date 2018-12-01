package com.github.alvarosanchez.micronaut.todo.domain;

import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Singleton
public class TodoRepositoryJpaImpl implements TodoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Todo> findAllByUser(User user) {
        return this.entityManager
                .createQuery("select t from Todo t where user = :user", Todo.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    @Transactional
    public Todo save(Todo todo) {
        this.entityManager.persist(todo);
        return todo;
    }

    @Override
    @Transactional
    public Todo complete(Todo todo) {
        Todo fromDb = entityManager.find(Todo.class, todo.getId());
        fromDb.setComplete(true);
        return fromDb;
    }
}
