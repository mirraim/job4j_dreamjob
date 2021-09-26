package ru.job4j.dream.store;

import org.junit.Ignore;
import org.junit.Test;
import ru.job4j.dream.model.User;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserManagerTest {

    @Ignore
    public Connection init() {
        try (
                InputStream in = UserManagerTest.class.getClassLoader().getResourceAsStream(
                        "test.properties"
                )) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("jdbc.driver"));
            return DriverManager.getConnection(
                    config.getProperty("jdbc.url"),
                    config.getProperty("jdbc.username"),
                    config.getProperty("jdbc.password")

            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Ignore
    @Test
    public void whenCreate() throws SQLException {
        try (Connection connection = this.init()) {
            UserManager manager = new UserManager(ConnectionRollback.create(connection));
            Collection<User> users = manager.findAll();
            User user = new User(0, "Mary", "mary@mail.ru", "mary");
            users.add(user);
            manager.save(user);
            assertEquals(manager.findAll(), users);
        }
    }

    @Ignore
    @Test
    public void whenUpdate() throws SQLException {
        try (Connection connection = this.init()) {
            UserManager manager = new UserManager(ConnectionRollback.create(connection));
            Collection<User> users = manager.findAll();
            User user = new User(0, "Mary", "mary@mail.ru", "mary");
            manager.save(user);
            User user1 = new User(user.getId(), "Sam", "sam@mail.ru", "sam");
            manager.save(user1);
            users.add(user1);
            assertEquals(manager.findAll().size(), users.size());
        }
    }

    @Ignore
    @Test
    public void whenUpdateSameName() throws SQLException {
        try (Connection connection = this.init()) {
            UserManager manager = new UserManager(ConnectionRollback.create(connection));
            User user = new User(0, "Mary", "mary@mail.ru", "mary");
            manager.save(user);
            User user1 = new User(user.getId(), "Sam", "sam@mail.ru", "sam");
            manager.save(user1);
            assertEquals("Sam", manager.findById(user.getId()).getName());
        }
    }

    @Ignore
    @Test
    public void findWhenUpdateSameEmail() throws SQLException {
        try (Connection connection = this.init()) {
            UserManager manager = new UserManager(ConnectionRollback.create(connection));
            User user = new User(0, "Mary", "mary@mail.ru", "mary");
            manager.save(user);
            User user1 = new User(user.getId(), "Sam", "sam@mail.ru", "sam");
            manager.save(user1);
            String rsl = manager.findById(user.getId()).getEmail();
            assertEquals("sam@mail.ru", rsl);
        }
    }

    @Ignore
    @Test
    public void findWhenUpdateSamePassword() throws SQLException {
        try (Connection connection = this.init()) {
            UserManager manager = new UserManager(ConnectionRollback.create(connection));
            User user = new User(0, "Mary", "mary@mail.ru", "mary");
            manager.save(user);
            User user1 = new User(user.getId(), "Sam", "sam@mail.ru", "sam");
            manager.save(user1);
            String rsl = manager.findById(user.getId()).getPassword();
            assertEquals("sam", rsl);
        }
    }

    @Ignore
    @Test
    public void whenNotFound() throws SQLException {
        try (Connection connection = this.init()) {
            UserManager manager = new UserManager(ConnectionRollback.create(connection));
            User user = new User(0, "Mary", "mary@mail.ru", "mary");
            manager.save(user);
            assertNull(manager.findById(-1));
        }
    }
}