package ru.job4j.dream.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PostManager implements DBManager<Post>{
    private final Connection cn;
    private static final Logger LOG = LoggerFactory.getLogger(PostManager.class.getName());

    public PostManager(Connection cn) {
        this.cn = cn;
    }

    public Collection<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT * FROM post"
        )) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to find Posts", e);
        }
        return posts;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    private Post create(Post post) {
        try (PreparedStatement ps = cn.prepareStatement(
                "INSERT INTO post(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, post.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to create Post", e);
        }
        return post;
    }

    private void update(Post post) {
        try (PreparedStatement ps = cn.prepareStatement(
                "UPDATE post SET name=? WHERE id=?"
        )) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.info("Unable to update Post", e);
        }
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT * from post WHERE id=?"
        )) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    post = new Post(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to find Post", e);
        }
        return post;
    }
}
