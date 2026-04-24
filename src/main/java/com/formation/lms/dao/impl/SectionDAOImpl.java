package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.SectionDAO;
import com.formation.lms.models.Section;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SectionDAOImpl implements SectionDAO {

    private Section mapperEntite(ResultSet rs) throws SQLException {
        Section section = new Section();
        section.setId(rs.getLong("id"));
        section.setCoursId(rs.getLong("cours_id"));
        section.setTitre(rs.getString("titre"));
        section.setDescription(rs.getString("description"));
        section.setOrdre(rs.getInt("ordre"));
        section.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        return section;
    }

    @Override
    public Optional<Section> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM sections WHERE id = ?";
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
    public List<Section> findAll() throws SQLException {
        String sql = "SELECT * FROM sections ORDER BY cours_id, ordre";
        List<Section> sections = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) sections.add(mapperEntite(rs));
        }
        return sections;
    }

    @Override
    public Section save(Section section) throws SQLException {
        String sql = "INSERT INTO sections (cours_id, titre, description, ordre) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, section.getCoursId());
            ps.setString(2, section.getTitre());
            ps.setString(3, section.getDescription());
            ps.setInt(4, section.getOrdre());
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) section.setId(cles.getLong(1));
            }
        }
        return section;
    }

    @Override
    public boolean update(Section section) throws SQLException {
        String sql = "UPDATE sections SET titre = ?, description = ?, ordre = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, section.getTitre());
            ps.setString(2, section.getDescription());
            ps.setInt(3, section.getOrdre());
            ps.setLong(4, section.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM sections WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM sections";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public List<Section> findByCoursId(Long coursId) throws SQLException {
        String sql = "SELECT * FROM sections WHERE cours_id = ? ORDER BY ordre ASC";
        List<Section> sections = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, coursId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) sections.add(mapperEntite(rs));
            }
        }
        return sections;
    }

    @Override
    public long countByCoursId(Long coursId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM sections WHERE cours_id = ?";
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