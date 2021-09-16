package ru.job4j.dream.store;

import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.sql.SQLException;
import java.util.Collection;

import static org.junit.Assert.*;

public class PsqlStoreTest {
    @Test
    public void whenCreateCandidate() throws SQLException {
        Store store = PsqlStore.instOf();
        Collection<Candidate> candidates = store.findAllCandidates();
        Candidate candidate = new Candidate(0, "Java Dev");
        candidates.add(candidate);
        store.save(candidate);
        assertEquals(store.findAllCandidates().size(), candidates.size());
    }

    @Test
    public void whenUpdateCandidate() throws SQLException {
        Store store = PsqlStore.instOf();
        Collection<Candidate> candidates = store.findAllCandidates();
        Candidate candidate = new Candidate(0, "Java Dev");
        store.save(candidate);
        candidates.add(candidate);
        Candidate candidate1 = new Candidate(candidate.getId(), "Junior Java Dev");
        store.save(candidate1);
        assertEquals(store.findAllCandidates().size(), candidates.size());
    }

    @Test
    public void whenFindCandidate() throws SQLException {
        Store store = PsqlStore.instOf();
        Candidate candidate = new Candidate(0, "Java Dev");
        store.save(candidate);
        Candidate candidate1 = new Candidate(candidate.getId(), "Middle Java Dev");
        store.save(candidate1);
        assertEquals(
                "Middle Java Dev",
                store.findCandidateById(candidate.getId()).getName()
        );
    }

    @Test
    public void findWhenNoCandidate() throws SQLException {
        Store store = PsqlStore.instOf();
        assertNull(store.findCandidateById(-1));
    }

    @Test
    public void whenCreatePost() throws SQLException {
        Store store = PsqlStore.instOf();
        Collection<Post> posts = store.findAllPosts();
        Post post = new Post(0, "Java Job");
        posts.add(post);
        store.save(post);
        assertEquals(store.findAllPosts().size(), posts.size());
    }

    @Test
    public void whenUpdatePost() throws SQLException {
        Store store = PsqlStore.instOf();
        Collection<Post> posts = store.findAllPosts();
        Post post = new Post(0, "Java Job");
        store.save(post);
        posts.add(post);
        Post post1 = new Post(post.getId(), "Junior Java Job");
        store.save(post1);
        assertEquals(store.findAllPosts().size(), posts.size());
    }

    @Test
    public void whenFindPost() throws SQLException {
        Store store = PsqlStore.instOf();
        Post post = new Post(0, "Java Job");
        store.save(post);
        Post post1 = new Post(post.getId(), "Middle Java Job");
        store.save(post1);
        assertEquals(
                "Middle Java Job",
                store.findPostById(post.getId()).getName()
        );
    }

    @Test
    public void findWhenNoPost() throws SQLException {
        Store store = PsqlStore.instOf();
        assertNull(store.findPostById(-1));
    }
}