package com.github.alvarosanchez.micronaut.todo;

import org.junit.jupiter.api.AfterEach;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractDatabaseTest {

    @Inject
    DataSource dataSource;

    @AfterEach
    void cleanup() throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("delete from todo");
        statement.executeUpdate("delete from user");
        statement.close();
        connection.close();
    }

}
