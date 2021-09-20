package ru.job4j.dream.store;

import org.junit.Ignore;
import org.junit.Test;
import ru.job4j.dream.model.Post;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PostManagerTest {

    public Connection init() {
        try (InputStream in = PostManagerTest.class.getClassLoader().getResourceAsStream(
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
        try (Connection connection = this.init()){
            PostManager manager = new PostManager(ConnectionRollback.create(connection));
            Collection<Post> posts = manager.findAll();
            Post post = new Post(0, "Java Job");
            posts.add(post);
            manager.save(post);
            assertEquals(manager.findAll(), posts);
        }
    }

    @Ignore
    @Test
    public void whenUpdate() throws SQLException {
        try (Connection connection = this.init()){
            PostManager manager = new PostManager(ConnectionRollback.create(connection));
            Collection<Post> posts = manager.findAll();
            Post post = new Post(0, "Java Job");
            manager.save(post);
            Post post1 = new Post(post.getId(), "Junior Java Job");
            manager.save(post1);
            posts.add(post1);
            assertEquals(manager.findAll().size(), posts.size());
        }
    }

    @Ignore
    @Test
    public void findWhenUpdate() throws SQLException {
        try (Connection connection = this.init()){
            PostManager manager = new PostManager(ConnectionRollback.create(connection));
            Post post = new Post(0, "Java Job");
            manager.save(post);
            Post post1 = new Post(post.getId(), "Junior Java Job");
            manager.save(post1);
            assertEquals("Junior Java Job", manager.findById(post.getId()).getName());
        }
    }

    @Ignore
    @Test
    public void whenNotFound() throws SQLException {
        try (Connection connection = this.init()){
            PostManager manager = new PostManager(ConnectionRollback.create(connection));
            Post post = new Post(0, "Java Job");
            manager.save(post);
            assertNull(manager.findById(-1));
        }
    }
}