package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.TentativeQuizDAO;
import com.formation.lms.models.TentativeQuiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TentativeQuizDAOImpl implements TentativeQuizDAO {

    private TentativeQuiz mapperEntite(ResultSet rs) throws SQLException {
        TentativeQuiz tentative = new TentativeQuiz();
        tentative.setId(rs.getLong("id"));
        tentative.setInscriptionId(rs.getLong("inscription_id"));
        tentative.setQuizId(rs.getLong("quiz_id"));
        tentative.setScore(rs.getDouble("score"));
        tentative.setPointsObtenus(rs.getInt("points_obtenus"));
        tentative.setPointsTotal(rs.getInt("points_total"));
        tentative.setEstReussi(rs.getBoolean("est_reussi"));
        tentative.setDateTentative(rs.getTimestamp("date_tentative").toLocalDateTime());
        return tentative;
    }

    @Override
    public Optional<TentativeQuiz> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM tentatives_quiz WHERE id = ?";
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
    public List<TentativeQuiz> findAll() throws SQLException {
        String sql = "SELECT * FROM tentatives_quiz ORDER BY date_tentative DESC";
        List<TentativeQuiz> tentatives = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) tentatives.add(mapperEntite(rs));
        }
        return tentatives;
    }

    @Override
    public TentativeQuiz save(TentativeQuiz tentative) throws SQLException {
        String sql = "INSERT INTO tentatives_quiz (inscription_id, quiz_id, score, points_obtenus, "
                + "points_total, est_reussi) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, tentative.getInscriptionId());
            ps.setLong(2, tentative.getQuizId());
            ps.setDouble(3, tentative.getScore());
            ps.setInt(4, tentative.getPointsObtenus());
            ps.setInt(5, tentative.getPointsTotal());
            ps.setBoolean(6, tentative.isEstReussi());
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) tentative.setId(cles.getLong(1));
            }
        }
        return tentative;
    }

    @Override
    public boolean update(TentativeQuiz tentative) throws SQLException {
        String sql = "UPDATE tentatives_quiz SET score = ?, points_obtenus = ?, points_total = ?, "
                + "est_reussi = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, tentative.getScore());
            ps.setInt(2, tentative.getPointsObtenus());
            ps.setInt(3, tentative.getPointsTotal());
            ps.setBoolean(4, tentative.isEstReussi());
            ps.setLong(5, tentative.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM tentatives_quiz WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM tentatives_quiz";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public List<TentativeQuiz> findByInscriptionEtQuiz(Long inscriptionId, Long quizId) throws SQLException {
        String sql = "SELECT * FROM tentatives_quiz WHERE inscription_id = ? AND quiz_id = ? "
                + "ORDER BY date_tentative DESC";
        List<TentativeQuiz> tentatives = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, inscriptionId);
            ps.setLong(2, quizId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) tentatives.add(mapperEntite(rs));
            }
        }
        return tentatives;
    }

    @Override
    public List<TentativeQuiz> findByInscriptionId(Long inscriptionId) throws SQLException {
        String sql = "SELECT * FROM tentatives_quiz WHERE inscription_id = ? ORDER BY date_tentative DESC";
        List<TentativeQuiz> tentatives = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, inscriptionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) tentatives.add(mapperEntite(rs));
            }
        }
        return tentatives;
    }

    @Override
    public TentativeQuiz findMeilleureTentative(Long inscriptionId, Long quizId) throws SQLException {
        String sql = "SELECT * FROM tentatives_quiz WHERE inscription_id = ? AND quiz_id = ? "
                + "ORDER BY score DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, inscriptionId);
            ps.setLong(2, quizId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapperEntite(rs);
            }
        }
        return null;
    }
}