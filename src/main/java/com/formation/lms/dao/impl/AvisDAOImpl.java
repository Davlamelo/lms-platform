package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.AvisDAO;
import com.formation.lms.models.Avis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AvisDAOImpl implements AvisDAO {

    private Avis mapperEntite(ResultSet rs) throws SQLException {
        Avis avis = new Avis();
        avis.setId(rs.getLong("id"));
        avis.setApprenantId(rs.getLong("apprenant_id"));
        avis.setCoursId(rs.getLong("cours_id"));
        avis.setNote(rs.getInt("note"));
        avis.setCommentaire(rs.getString("commentaire"));
        avis.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        avis.setDateMaj(rs.getTimestamp("date_maj").toLocalDateTime());
        return avis;
    }

    @Override
    public Optional<Avis> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM avis WHERE id = ?";
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
    public List<Avis> findAll() throws SQLException {
        String sql = "SELECT * FROM avis ORDER BY date_creation DESC";
        List<Avis> avisList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) avisList.add(mapperEntite(rs));
        }
        return avisList;
    }

    @Override
    public Avis save(Avis avis) throws SQLException {
        String sql = "INSERT INTO avis (apprenant_id, cours_id, note, commentaire) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, avis.getApprenantId());
            ps.setLong(2, avis.getCoursId());
            ps.setInt(3, avis.getNote());
            ps.setString(4, avis.getCommentaire());
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) avis.setId(cles.getLong(1));
            }
        }
        return avis;
    }

    @Override
    public boolean update(Avis avis) throws SQLException {
        String sql = "UPDATE avis SET note = ?, commentaire = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, avis.getNote());
            ps.setString(2, avis.getCommentaire());
            ps.setLong(3, avis.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM avis WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM avis";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public List<Avis> findByCoursId(Long coursId) throws SQLException {
        String sql = "SELECT * FROM avis WHERE cours_id = ? ORDER BY date_creation DESC";
        List<Avis> avisList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, coursId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) avisList.add(mapperEntite(rs));
            }
        }
        return avisList;
    }

    @Override
    public Optional<Avis> findByApprenantEtCours(Long apprenantId, Long coursId) throws SQLException {
        String sql = "SELECT * FROM avis WHERE apprenant_id = ? AND cours_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, apprenantId);
            ps.setLong(2, coursId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapperEntite(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public double getNoteMoyenne(Long coursId) throws SQLException {
        String sql = "SELECT AVG(note) FROM avis WHERE cours_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, coursId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    @Override
    public long countByCoursId(Long coursId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM avis WHERE cours_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, coursId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return 0;
    }
}