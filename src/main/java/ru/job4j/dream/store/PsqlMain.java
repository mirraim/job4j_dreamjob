package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.save(new Post(0,"Java Job"));
        store.save(new Post(0, "Middle Java Job"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }
        System.out.println("-------------------------");
        store.save(new Post(1, "Junior Java Job"));
        Post post = store.findPostById(1);
        System.out.println(post.getId() + " " + post.getName());
    }
}
