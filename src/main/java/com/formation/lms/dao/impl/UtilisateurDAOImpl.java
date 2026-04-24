package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.UtilisateurDAO;
import com.formation.lms.models.Role;
import com.formation.lms.models.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation JDBC de UtilisateurDAO.
 * Chaque méthode traduit une opération métier en requête SQL.
 */
public class UtilisateurDAOImpl implements UtilisateurDAO {

    // =======================================================================
    // MÉTHODE PRIVÉE DE MAPPING (ResultSet → Objet Java)
    // =======================================================================

    /**
     * Convertit une ligne de ResultSet en objet Utilisateur.
     * Cette méthode est appelée par toutes les méthodes de lecture.
     *
     * PATTERN RÉCURRENT : chaque DAOImpl a sa méthode mapperEntite().
     * C'est ici que tu fais la correspondance colonne SQL → attribut Java.
     */
    private Utilisateur mapperEntite(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getLong("id"));
        utilisateur.setEmail(rs.getString("email"));
        utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setAvatarUrl(rs.getString("avatar_url"));
        utilisateur.setBiographie(rs.getString("biographie"));
        utilisateur.setRole(Role.valueOf(rs.getString("role")));
        utilisateur.setActif(rs.getBoolean("actif"));
        utilisateur.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());

        Timestamp dateMaj = rs.getTimestamp("date_maj");
        if (dateMaj != null) {
            utilisateur.setDateMaj(dateMaj.toLocalDateTime());
        }

        return utilisateur;
    }

    // =======================================================================
    // MÉTHODES CRUD (héritées de GenericDAO)
    // =======================================================================

    @Override
    public Optional<Utilisateur> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM utilisateurs WHERE id = ?";

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
    public List<Utilisateur> findAll() throws SQLException {
        String sql = "SELECT * FROM utilisateurs ORDER BY date_creation DESC";
        List<Utilisateur> utilisateurs = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                utilisateurs.add(mapperEntite(rs));
            }
        }
        return utilisateurs;
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) throws SQLException {
        String sql = "INSERT INTO utilisateurs (email, mot_de_passe, prenom, nom, avatar_url, biographie, role, actif) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, utilisateur.getEmail());
            ps.setString(2, utilisateur.getMotDePasse());
            ps.setString(3, utilisateur.getPrenom());
            ps.setString(4, utilisateur.getNom());
            ps.setString(5, utilisateur.getAvatarUrl());
            ps.setString(6, utilisateur.getBiographie());
            ps.setString(7, utilisateur.getRole().name());
            ps.setBoolean(8, utilisateur.isActif());

            int lignesAffectees = ps.executeUpdate();

            if (lignesAffectees > 0) {
                try (ResultSet clesGenerees = ps.getGeneratedKeys()) {
                    if (clesGenerees.next()) {
                        utilisateur.setId(clesGenerees.getLong(1));
                    }
                }
            }
        }
        return utilisateur;
    }

    @Override
    public boolean update(Utilisateur utilisateur) throws SQLException {
        String sql = "UPDATE utilisateurs SET email = ?, prenom = ?, nom = ?, "
                + "avatar_url = ?, biographie = ?, role = ?, actif = ? "
                + "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, utilisateur.getEmail());
            ps.setString(2, utilisateur.getPrenom());
            ps.setString(3, utilisateur.getNom());
            ps.setString(4, utilisateur.getAvatarUrl());
            ps.setString(5, utilisateur.getBiographie());
            ps.setString(6, utilisateur.getRole().name());
            ps.setBoolean(7, utilisateur.isActif());
            ps.setLong(8, utilisateur.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM utilisateurs";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    // =======================================================================
    // MÉTHODES MÉTIER (spécifiques à UtilisateurDAO)
    // =======================================================================

    @Override
    public Optional<Utilisateur> findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM utilisateurs WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapperEntite(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Utilisateur> findByRole(Role role) throws SQLException {
        String sql = "SELECT * FROM utilisateurs WHERE role = ? ORDER BY date_creation DESC";
        List<Utilisateur> utilisateurs = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    utilisateurs.add(mapperEntite(rs));
                }
            }
        }
        return utilisateurs;
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM utilisateurs WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public boolean updateStatutActif(Long id, boolean actif) throws SQLException {
        String sql = "UPDATE utilisateurs SET actif = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, actif);
            ps.setLong(2, id);

            return ps.executeUpdate() > 0;
        }
    }
}