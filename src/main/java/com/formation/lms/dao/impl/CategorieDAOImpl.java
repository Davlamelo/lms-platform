package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.CategorieDAO;
import com.formation.lms.models.Categorie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategorieDAOImpl implements CategorieDAO {

    private Categorie mapperEntite(ResultSet rs) throws SQLException {
        Categorie categorie = new Categorie();
        categorie.setId(rs.getLong("id"));
        categorie.setNom(rs.getString("nom"));
        categorie.setSlug(rs.getString("slug"));
        categorie.setDescription(rs.getString("description"));
        categorie.setIcone(rs.getString("icone"));
        categorie.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        return categorie;
    }

    @Override
    public Optional<Categorie> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapperEntite(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Categorie> findAll() throws SQLException {
        String sql = "SELECT * FROM categories ORDER BY nom ASC";
        List<Categorie> categories = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(mapperEntite(rs));
            }
        }
        return categories;
    }

    @Override
    public Categorie save(Categorie categorie) throws SQLException {
        String sql = "INSERT INTO categories (nom, slug, description, icone) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, categorie.getNom());
            ps.setString(2, categorie.getSlug());
            ps.setString(3, categorie.getDescription());
            ps.setString(4, categorie.getIcone());
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) {
                    categorie.setId(cles.getLong(1));
                }
            }
        }
        return categorie;
    }

    @Override
    public boolean update(Categorie categorie) throws SQLException {
        String sql = "UPDATE categories SET nom = ?, slug = ?, description = ?, icone = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categorie.getNom());
            ps.setString(2, categorie.getSlug());
            ps.setString(3, categorie.getDescription());
            ps.setString(4, categorie.getIcone());
            ps.setLong(5, categorie.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM categories";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    // === Méthodes métier ===

    @Override
    public Optional<Categorie> findBySlug(String slug) throws SQLException {
        String sql = "SELECT * FROM categories WHERE slug = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, slug);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapperEntite(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByNom(String nom) throws SQLException {
        String sql = "SELECT COUNT(*) FROM categories WHERE nom = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1) > 0;
                }
            }
        }
        return false;
    }
}