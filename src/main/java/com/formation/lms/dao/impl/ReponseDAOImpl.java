package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.ReponseDAO;
import com.formation.lms.models.Reponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReponseDAOImpl implements ReponseDAO {

    private Reponse mapperEntite(ResultSet rs) throws SQLException {
        Reponse reponse = new Reponse();
        reponse.setId(rs.getLong("id"));
        reponse.setQuestionId(rs.getLong("question_id"));
        reponse.setTexte(rs.getString("texte"));
        reponse.setEstCorrecte(rs.getBoolean("est_correcte"));
        reponse.setOrdre(rs.getInt("ordre"));
        return reponse;
    }

    @Override
    public Optional<Reponse> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM reponses WHERE id = ?";
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
    public List<Reponse> findAll() throws SQLException {
        String sql = "SELECT * FROM reponses ORDER BY question_id, ordre";
        List<Reponse> reponses = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) reponses.add(mapperEntite(rs));
        }
        return reponses;
    }

    @Override
    public Reponse save(Reponse reponse) throws SQLException {
        String sql = "INSERT INTO reponses (question_id, texte, est_correcte, ordre) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, reponse.getQuestionId());
            ps.setString(2, reponse.getTexte());
            ps.setBoolean(3, reponse.isEstCorrecte());
            ps.setInt(4, reponse.getOrdre());
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) reponse.setId(cles.getLong(1));
            }
        }
        return reponse;
    }

    @Override
    public boolean update(Reponse reponse) throws SQLException {
        String sql = "UPDATE reponses SET texte = ?, est_correcte = ?, ordre = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reponse.getTexte());
            ps.setBoolean(2, reponse.isEstCorrecte());
            ps.setInt(3, reponse.getOrdre());
            ps.setLong(4, reponse.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM reponses WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM reponses";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public List<Reponse> findByQuestionId(Long questionId) throws SQLException {
        String sql = "SELECT * FROM reponses WHERE question_id = ? ORDER BY ordre ASC";
        List<Reponse> reponses = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, questionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) reponses.add(mapperEntite(rs));
            }
        }
        return reponses;
    }

    @Override
    public List<Reponse> findReponsesCorrectes(Long questionId) throws SQLException {
        String sql = "SELECT * FROM reponses WHERE question_id = ? AND est_correcte = TRUE ORDER BY ordre";
        List<Reponse> reponses = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, questionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) reponses.add(mapperEntite(rs));
            }
        }
        return reponses;
    }
}