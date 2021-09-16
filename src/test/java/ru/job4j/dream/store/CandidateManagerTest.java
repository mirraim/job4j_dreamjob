package ru.job4j.dream.store;

import org.junit.Test;
import ru.job4j.dream.model.Candidate;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CandidateManagerTest {
    public Connection init() {
        try (InputStream in = CandidateManagerTest.class.getClassLoader().getResourceAsStream(
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

    @Test
    public void whenCreate() throws SQLException {
        try (Connection connection = this.init()){
            CandidateManager manager = new CandidateManager(ConnectionRollback.create(connection));
            Collection<Candidate> candidates = manager.findAll();
            Candidate candidate = new Candidate(0, "Java Dev");
            candidates.add(candidate);
            manager.save(candidate);
            assertEquals(manager.findAll(), candidates);
        }
    }

    @Test
    public void whenUpdate() throws SQLException {
        try (Connection connection = this.init()){
            CandidateManager manager = new CandidateManager(ConnectionRollback.create(connection));
            Collection<Candidate> candidates = manager.findAll();
            Candidate candidate = new Candidate(0, "Java Dev");
            manager.save(candidate);
            Candidate candidate1 = new Candidate(candidate.getId(), "Junior Java Dev");
            manager.save(candidate1);
            candidates.add(candidate1);
            assertEquals(manager.findAll().size(), candidates.size());
        }
    }

    @Test
    public void findWhenUpdate() throws SQLException {
        try (Connection connection = this.init()){
            CandidateManager manager = new CandidateManager(ConnectionRollback.create(connection));
            Candidate candidate = new Candidate(0, "Java Dev");
            manager.save(candidate);
            Candidate candidate1 = new Candidate(candidate.getId(), "Junior Java Dev");
            manager.save(candidate1);
            assertEquals("Junior Java Dev", manager.findById(candidate.getId()).getName());
        }
    }

    @Test
    public void whenNotFound() throws SQLException {
        try (Connection connection = this.init()){
            CandidateManager manager = new CandidateManager(ConnectionRollback.create(connection));
            Candidate candidate = new Candidate(0, "Java Dev");
            manager.save(candidate);
            assertNull(manager.findById(-1));
        }
    }
}