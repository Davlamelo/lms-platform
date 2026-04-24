package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.LeconDAO;
import com.formation.lms.models.Lecon;
import com.formation.lms.models.TypeLecon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeconDAOImpl implements LeconDAO {

    private Lecon mapperEntite(ResultSet rs) throws SQLException {
        Lecon lecon = new Lecon();
        lecon.setId(rs.getLong("id"));
        lecon.setSectionId(rs.getLong("section_id"));
        lecon.setTitre(rs.getString("titre"));
        lecon.setTypeLecon(TypeLecon.valueOf(rs.getString("type_lecon")));
        lecon.setContenuTexte(rs.getString("contenu_texte"));
        lecon.setVideoUrl(rs.getString("video_url"));
        lecon.setRessourceUrl(rs.getString("ressource_url"));
        lecon.setDureeMin(rs.getInt("duree_min"));
        lecon.setOrdre(rs.getInt("ordre"));
        lecon.setEstGratuite(rs.getBoolean("est_gratuite"));
        lecon.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        return lecon;
    }

    @Override
    public Optional<Lecon> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM lecons WHERE id = ?";
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
    public List<Lecon> findAll() throws SQLException {
        String sql = "SELECT * FROM lecons ORDER BY section_id, ordre";
        List<Lecon> lecons = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lecons.add(mapperEntite(rs));
        }
        return lecons;
    }

    @Override
    public Lecon save(Lecon lecon) throws SQLException {
        String sql = "INSERT INTO lecons (section_id, titre, type_lecon, contenu_texte, "
                + "video_url, ressource_url, duree_min, ordre, est_gratuite) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, lecon.getSectionId());
            ps.setString(2, lecon.getTitre());
            ps.setString(3, lecon.getTypeLecon().name());
            ps.setString(4, lecon.getContenuTexte());
            ps.setString(5, lecon.getVideoUrl());
            ps.setString(6, lecon.getRessourceUrl());
            ps.setInt(7, lecon.getDureeMin());
            ps.setInt(8, lecon.getOrdre());
            ps.setBoolean(9, lecon.isEstGratuite());
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) lecon.setId(cles.getLong(1));
            }
        }
        return lecon;
    }

    @Override
    public boolean update(Lecon lecon) throws SQLException {
        String sql = "UPDATE lecons SET titre = ?, type_lecon = ?, contenu_texte = ?, "
                + "video_url = ?, ressource_url = ?, duree_min = ?, ordre = ?, est_gratuite = ? "
                + "WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lecon.getTitre());
            ps.setString(2, lecon.getTypeLecon().name());
            ps.setString(3, lecon.getContenuTexte());
            ps.setString(4, lecon.getVideoUrl());
            ps.setString(5, lecon.getRessourceUrl());
            ps.setInt(6, lecon.getDureeMin());
            ps.setInt(7, lecon.getOrdre());
            ps.setBoolean(8, lecon.isEstGratuite());
            ps.setLong(9, lecon.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM lecons WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM lecons";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public List<Lecon> findBySectionId(Long sectionId) throws SQLException {
        String sql = "SELECT * FROM lecons WHERE section_id = ? ORDER BY ordre ASC";
        List<Lecon> lecons = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lecons.add(mapperEntite(rs));
            }
        }
        return lecons;
    }

    @Override
    public List<Lecon> findByCoursId(Long coursId) throws SQLException {
        String sql = "SELECT l.* FROM lecons l "
                + "JOIN sections s ON l.section_id = s.id "
                + "WHERE s.cours_id = ? ORDER BY s.ordre, l.ordre";
        List<Lecon> lecons = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, coursId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lecons.add(mapperEntite(rs));
            }
        }
        return lecons;
    }

    @Override
    public long countByCoursId(Long coursId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM lecons l "
                + "JOIN sections s ON l.section_id = s.id "
                + "WHERE s.cours_id = ?";
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