package ru.job4j.dream.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Candidate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CandidateManager implements DBManager<Candidate>{
    Connection cn;
    private static final Logger LOG = LoggerFactory.getLogger(CandidateManager.class.getName());

    public CandidateManager(Connection cn) {
        this.cn = cn;
    }

    @Override
    public Collection<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT * FROM candidate"
        )) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()){
                    candidates.add(new Candidate(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to execute findAllCandidates", e);
        }
        return candidates;
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    private Candidate create(Candidate candidate) {
        try (PreparedStatement ps = cn.prepareStatement(
                "INSERT INTO candidate(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, candidate.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to create Post", e);
        }
        return candidate;
    }

    private void update(Candidate candidate) {
        try (PreparedStatement ps = cn.prepareStatement(
                "UPDATE candidate SET name=? WHERE id=?"
        )) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.info("Unable to update Candidate", e);
        }
    }

    @Override
    public Candidate findById(int id) {
        Candidate candidate = null;
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT * from candidate WHERE id=?"
        )) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    candidate = new Candidate(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.info("Unable to find Post", e);
        }
        return candidate;
    }
}
