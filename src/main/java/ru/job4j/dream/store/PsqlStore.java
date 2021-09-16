package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class PsqlStore implements Store {
    private final BasicDataSource pool = new BasicDataSource();
    private static final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        Collection<Post> posts = new ArrayList<>();
        try(Connection cn = pool.getConnection()) {
            DBManager<Post> manager = new PostManager(cn);
            posts =  manager.findAll();
        } catch (Exception e) {
            LOG.info("Unable to get connection", e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        Collection<Candidate> candidates = new ArrayList<>();
        try(Connection cn = pool.getConnection()) {
            DBManager<Candidate> manager = new CandidateManager(cn);
            candidates =  manager.findAll();
        } catch (Exception e) {
            LOG.info("Unable to get connection", e);
        }
        return candidates;
    }

    @Override
    public void save(Post post) {
        try(Connection cn = pool.getConnection()) {
            DBManager<Post> manager = new PostManager(cn);
            manager.save(post);
        } catch (Exception e) {
            LOG.info("Unable to get connection", e);
        }
    }

    @Override
    public void save(Candidate candidate) {
        try(Connection cn = pool.getConnection()) {
            DBManager<Candidate> manager = new CandidateManager(cn);
            manager.save(candidate);
        } catch (Exception e) {
            LOG.info("Unable to get connection", e);
        }
    }

    @Override
    public Post findPostById(int id) {
        Post post = null;
        try(Connection cn = pool.getConnection()) {
            DBManager<Post> manager = new PostManager(cn);
            post =  manager.findById(id);
        } catch (Exception e) {
            LOG.info("Unable to get connection", e);
        }
        return post;
    }

    @Override
    public Candidate findCandidateById(int id) {
        Candidate candidate = null;
        try(Connection cn = pool.getConnection()) {
            DBManager<Candidate> manager = new CandidateManager(cn);
            candidate =  manager.findById(id);
        } catch (Exception e) {
            LOG.info("Unable to get connection", e);
        }
        return candidate;
    }
}
