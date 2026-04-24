package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.QuestionDAO;
import com.formation.lms.models.Question;
import com.formation.lms.models.TypeQuestion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestionDAOImpl implements QuestionDAO {

    private Question mapperEntite(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setId(rs.getLong("id"));
        question.setQuizId(rs.getLong("quiz_id"));
        question.setEnonce(rs.getString("enonce"));
        question.setTypeQuestion(TypeQuestion.valueOf(rs.getString("type_question")));
        question.setPoints(rs.getInt("points"));
        question.setOrdre(rs.getInt("ordre"));
        return question;
    }

    @Override
    public Optional<Question> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM questions WHERE id = ?";
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
    public List<Question> findAll() throws SQLException {
        String sql = "SELECT * FROM questions ORDER BY quiz_id, ordre";
        List<Question> questions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) questions.add(mapperEntite(rs));
        }
        return questions;
    }

    @Override
    public Question save(Question question) throws SQLException {
        String sql = "INSERT INTO questions (quiz_id, enonce, type_question, points, ordre) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, question.getQuizId());
            ps.setString(2, question.getEnonce());
            ps.setString(3, question.getTypeQuestion().name());
            ps.setInt(4, question.getPoints());
            ps.setInt(5, question.getOrdre());
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) question.setId(cles.getLong(1));
            }
        }
        return question;
    }

    @Override
    public boolean update(Question question) throws SQLException {
        String sql = "UPDATE questions SET enonce = ?, type_question = ?, points = ?, ordre = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, question.getEnonce());
            ps.setString(2, question.getTypeQuestion().name());
            ps.setInt(3, question.getPoints());
            ps.setInt(4, question.getOrdre());
            ps.setLong(5, question.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM questions WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM questions";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public List<Question> findByQuizId(Long quizId) throws SQLException {
        String sql = "SELECT * FROM questions WHERE quiz_id = ? ORDER BY ordre ASC";
        List<Question> questions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, quizId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) questions.add(mapperEntite(rs));
            }
        }
        return questions;
    }

    @Override
    public long countByQuizId(Long quizId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM questions WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, quizId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return 0;
    }
}