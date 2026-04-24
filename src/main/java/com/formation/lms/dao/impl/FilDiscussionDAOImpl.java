package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.FilDiscussionDAO;
import com.formation.lms.models.FilDiscussion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FilDiscussionDAOImpl implements FilDiscussionDAO {

    private FilDiscussion mapperEntite(ResultSet rs) throws SQLException {
        FilDiscussion fil = new FilDiscussion();
        fil.setId(rs.getLong("id"));
        fil.setCoursId(rs.getLong("cours_id"));
        fil.setAuteurId(rs.getLong("auteur_id"));
        fil.setTitre(rs.getString("titre"));
        fil.setContenu(rs.getString("contenu"));
        fil.setEstResolu(rs.getBoolean("est_resolu"));
        fil.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        return fil;
    }

    @Override
    public Optional<FilDiscussion> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM fils_discussion WHERE id = ?";
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
    public List<FilDiscussion> findAll() throws SQLException {
        String sql = "SELECT * FROM fils_discussion ORDER BY date_creation DESC";
        List<FilDiscussion> fils = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) fils.add(mapperEntite(rs));
        }
        return fils;
    }

    @Override
    public FilDiscussion save(FilDiscussion fil) throws SQLException {
        String sql = "INSERT INTO fils_discussion (cours_id, auteur_id, titre, contenu) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, fil.getCoursId());
            ps.setLong(2, fil.getAuteurId());
            ps.setString(3, fil.getTitre());
            ps.setString(4, fil.getContenu());
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) fil.setId(cles.getLong(1));
            }
        }
        return fil;
    }

    @Override
    public boolean update(FilDiscussion fil) throws SQLException {
        String sql = "UPDATE fils_discussion SET titre = ?, contenu = ?, est_resolu = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fil.getTitre());
            ps.setString(2, fil.getContenu());
            ps.setBoolean(3, fil.isEstResolu());
            ps.setLong(4, fil.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM fils_discussion WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM fils_discussion";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public List<FilDiscussion> findByCoursId(Long coursId) throws SQLException {
        String sql = "SELECT * FROM fils_discussion WHERE cours_id = ? ORDER BY date_creation DESC";
        List<FilDiscussion> fils = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, coursId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) fils.add(mapperEntite(rs));
            }
        }
        return fils;
    }

    @Override
    public List<FilDiscussion> findNonResolus(Long coursId) throws SQLException {
        String sql = "SELECT * FROM fils_discussion WHERE cours_id = ? AND est_resolu = FALSE "
                + "ORDER BY date_creation DESC";
        List<FilDiscussion> fils = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, coursId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) fils.add(mapperEntite(rs));
            }
        }
        return fils;
    }

    @Override
    public boolean marquerResolu(Long id) throws SQLException {
        String sql = "UPDATE fils_discussion SET est_resolu = TRUE WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}