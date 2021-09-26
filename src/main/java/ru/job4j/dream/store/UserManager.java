package ru.job4j.dream.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserManager implements DBManager<User> {
    private final Connection cn;
    private static final Logger LOG = LoggerFactory.getLogger(PostManager.class.getName());

    public UserManager(Connection cn) {
        this.cn = cn;
    }

    @Override
    public Collection<User> findAll() {
        List<User> posts = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT * FROM users"
        )) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new User(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getString("email"),
                            it.getString("password")
                    ));
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to find Posts", e);
        }
        return posts;
    }

    @Override
    public void save(User user) {
        if (user.getId() == 0) {
            create(user);
        } else {
            update(user);
        }
    }

    private User create(User user) {
        try (PreparedStatement ps = cn.prepareStatement(
                "INSERT INTO users(name, email, password) VALUES (?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getEmail());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to create User", e);
        }
        return user;
    }

    private void update(User user) {
        try (PreparedStatement ps = cn.prepareStatement(
                "UPDATE users SET name=?, email=?, password=? WHERE id=?"
        )) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.info("Unable to update User", e);
        }
    }
    @Override
    public User findById(int id) {
        User user = null;
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT * from users WHERE id=?"
        )) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    user = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to find User", e);
        }
        return user;
    }
}
