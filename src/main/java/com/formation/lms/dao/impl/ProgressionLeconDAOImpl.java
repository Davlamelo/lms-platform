package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.ProgressionLeconDAO;
import com.formation.lms.models.ProgressionLecon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProgressionLeconDAOImpl implements ProgressionLeconDAO {

    private ProgressionLecon mapperEntite(ResultSet rs) throws SQLException {
        ProgressionLecon prog = new ProgressionLecon();
        prog.setId(rs.getLong("id"));
        prog.setInscriptionId(rs.getLong("inscription_id"));
        prog.setLeconId(rs.getLong("lecon_id"));
        prog.setEstCompletee(rs.getBoolean("est_completee"));
        Timestamp dateComp = rs.getTimestamp("date_completion");
        if (dateComp != null) {
            prog.setDateCompletion(dateComp.toLocalDateTime());
        }
        prog.setTempsPasseSec(rs.getInt("temps_passe_sec"));
        return prog;
    }

    @Override
    public Optional<ProgressionLecon> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM progression_lecons WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapperEntite(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ProgressionLecon> findAll() throws SQLException {
        String sql = "SELECT * FROM progression_lecons";
        List<ProgressionLecon> progressions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) progressions.add(mapperEntite(rs));
        }
        return progressions;
    }

    @Override
    public ProgressionLecon save(ProgressionLecon prog) throws SQLException {
        String sql = "INSERT INTO progression_lecons (inscription_id, lecon_id, est_completee, temps_passe_sec) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, prog.getInscriptionId());
            ps.setLong(2, prog.getLeconId());
            ps.setBoolean(3, prog.isEstCompletee());
            ps.setInt(4, prog.getTempsPasseSec());
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) prog.setId(cles.getLong(1));
            }
        }
        return prog;
    }

    @Override
    public boolean update(ProgressionLecon prog) throws SQLException {
        String sql = "UPDATE progression_lecons SET est_completee = ?, date_completion = ?, "
                + "temps_passe_sec = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, prog.isEstCompletee());
            if (prog.getDateCompletion() != null) {
                ps.setTimestamp(2, Timestamp.valueOf(prog.getDateCompletion()));
            } else {
                ps.setNull(2, Types.TIMESTAMP);
            }
            ps.setInt(3, prog.getTempsPasseSec());
            ps.setLong(4, prog.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM progression_lecons WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM progression_lecons";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public Optional<ProgressionLecon> findByInscriptionEtLecon(Long inscriptionId, Long leconId) throws SQLException {
        String sql = "SELECT * FROM progression_lecons WHERE inscription_id = ? AND lecon_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, inscriptionId);
            ps.setLong(2, leconId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapperEntite(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ProgressionLecon> findByInscriptionId(Long inscriptionId) throws SQLException {
        String sql = "SELECT * FROM progression_lecons WHERE inscription_id = ?";
        List<ProgressionLecon> progressions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, inscriptionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) progressions.add(mapperEntite(rs));
            }
        }
        return progressions;
    }

    @Override
    public long countCompletees(Long inscriptionId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM progression_lecons WHERE inscription_id = ? AND est_completee = TRUE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, inscriptionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return 0;
    }

    @Override
    public boolean marquerCompletee(Long inscriptionId, Long leconId) throws SQLException {
        String sql = "UPDATE progression_lecons SET est_completee = TRUE, date_completion = NOW() "
                + "WHERE inscription_id = ? AND lecon_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, inscriptionId);
            ps.setLong(2, leconId);
            return ps.executeUpdate() > 0;
        }
    }
}