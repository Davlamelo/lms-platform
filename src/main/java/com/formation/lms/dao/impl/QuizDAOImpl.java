package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.QuizDAO;
import com.formation.lms.models.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuizDAOImpl implements QuizDAO {

    private Quiz mapperEntite(ResultSet rs) throws SQLException {
        Quiz quiz = new Quiz();
        quiz.setId(rs.getLong("id"));
        quiz.setLeconId(rs.getLong("lecon_id"));
        quiz.setTitre(rs.getString("titre"));
        quiz.setDescription(rs.getString("description"));
        quiz.setScoreMinimum(rs.getInt("score_minimum"));
        int dureeLimite = rs.getInt("duree_limite_min");
        quiz.setDureeLimiteMin(rs.wasNull() ? null : dureeLimite);
        quiz.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        return quiz;
    }

    @Override
    public Optional<Quiz> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM quiz WHERE id = ?";
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
    public List<Quiz> findAll() throws SQLException {
        String sql = "SELECT * FROM quiz";
        List<Quiz> quizList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) quizList.add(mapperEntite(rs));
        }
        return quizList;
    }

    @Override
    public Quiz save(Quiz quiz) throws SQLException {
        String sql = "INSERT INTO quiz (lecon_id, titre, description, score_minimum, duree_limite_min) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, quiz.getLeconId());
            ps.setString(2, quiz.getTitre());
            ps.setString(3, quiz.getDescription());
            ps.setInt(4, quiz.getScoreMinimum());
            if (quiz.getDureeLimiteMin() != null) {
                ps.setInt(5, quiz.getDureeLimiteMin());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) quiz.setId(cles.getLong(1));
            }
        }
        return quiz;
    }

    @Override
    public boolean update(Quiz quiz) throws SQLException {
        String sql = "UPDATE quiz SET titre = ?, description = ?, score_minimum = ?, duree_limite_min = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, quiz.getTitre());
            ps.setString(2, quiz.getDescription());
            ps.setInt(3, quiz.getScoreMinimum());
            if (quiz.getDureeLimiteMin() != null) {
                ps.setInt(4, quiz.getDureeLimiteMin());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setLong(5, quiz.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM quiz WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM quiz";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public Optional<Quiz> findByLeconId(Long leconId) throws SQLException {
        String sql = "SELECT * FROM quiz WHERE lecon_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, leconId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapperEntite(rs));
            }
        }
        return Optional.empty();
    }
}