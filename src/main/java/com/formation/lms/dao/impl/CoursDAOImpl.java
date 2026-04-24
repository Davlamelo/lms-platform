package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.CoursDAO;
import com.formation.lms.models.Cours;
import com.formation.lms.models.NiveauCours;
import com.formation.lms.models.StatutCours;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoursDAOImpl implements CoursDAO {

    private Cours mapperEntite(ResultSet rs) throws SQLException {
        Cours cours = new Cours();
        cours.setId(rs.getLong("id"));
        cours.setTitre(rs.getString("titre"));
        cours.setSlug(rs.getString("slug"));
        cours.setDescriptionCourte(rs.getString("description_courte"));
        cours.setDescriptionLongue(rs.getString("description_longue"));
        cours.setMiniatureUrl(rs.getString("miniature_url"));
        cours.setVideoPromoUrl(rs.getString("video_promo_url"));
        cours.setNiveau(NiveauCours.valueOf(rs.getString("niveau")));
        cours.setLangue(rs.getString("langue"));
        cours.setDureeTotaleMin(rs.getInt("duree_totale_min"));
        cours.setStatut(StatutCours.valueOf(rs.getString("statut")));
        cours.setInstructeurId(rs.getLong("instructeur_id"));
        cours.setCategorieId(rs.getLong("categorie_id"));
        cours.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());

        Timestamp datePub = rs.getTimestamp("date_publication");
        if (datePub != null) {
            cours.setDatePublication(datePub.toLocalDateTime());
        }

        cours.setDateMaj(rs.getTimestamp("date_maj").toLocalDateTime());
        return cours;
    }

    @Override
    public Optional<Cours> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM cours WHERE id = ?";
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
    public List<Cours> findAll() throws SQLException {
        String sql = "SELECT * FROM cours ORDER BY date_creation DESC";
        List<Cours> listeCours = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                listeCours.add(mapperEntite(rs));
            }
        }
        return listeCours;
    }

    @Override
    public Cours save(Cours cours) throws SQLException {
        String sql = "INSERT INTO cours (titre, slug, description_courte, description_longue, "
                + "miniature_url, video_promo_url, niveau, langue, duree_totale_min, statut, "
                + "instructeur_id, categorie_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cours.getTitre());
            ps.setString(2, cours.getSlug());
            ps.setString(3, cours.getDescriptionCourte());
            ps.setString(4, cours.getDescriptionLongue());
            ps.setString(5, cours.getMiniatureUrl());
            ps.setString(6, cours.getVideoPromoUrl());
            ps.setString(7, cours.getNiveau().name());
            ps.setString(8, cours.getLangue());
            ps.setInt(9, cours.getDureeTotaleMin());
            ps.setString(10, cours.getStatut().name());
            ps.setLong(11, cours.getInstructeurId());
            ps.setLong(12, cours.getCategorieId());
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) {
                    cours.setId(cles.getLong(1));
                }
            }
        }
        return cours;
    }

    @Override
    public boolean update(Cours cours) throws SQLException {
        String sql = "UPDATE cours SET titre = ?, slug = ?, description_courte = ?, "
                + "description_longue = ?, miniature_url = ?, video_promo_url = ?, "
                + "niveau = ?, langue = ?, duree_totale_min = ?, statut = ?, "
                + "categorie_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cours.getTitre());
            ps.setString(2, cours.getSlug());
            ps.setString(3, cours.getDescriptionCourte());
            ps.setString(4, cours.getDescriptionLongue());
            ps.setString(5, cours.getMiniatureUrl());
            ps.setString(6, cours.getVideoPromoUrl());
            ps.setString(7, cours.getNiveau().name());
            ps.setString(8, cours.getLangue());
            ps.setInt(9, cours.getDureeTotaleMin());
            ps.setString(10, cours.getStatut().name());
            ps.setLong(11, cours.getCategorieId());
            ps.setLong(12, cours.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM cours WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM cours";
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
    public Optional<Cours> findBySlug(String slug) throws SQLException {
        String sql = "SELECT * FROM cours WHERE slug = ?";
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
    public List<Cours> findPublies() throws SQLException {
        String sql = "SELECT * FROM cours WHERE statut = 'PUBLIE' ORDER BY date_publication DESC";
        List<Cours> listeCours = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                listeCours.add(mapperEntite(rs));
            }
        }
        return listeCours;
    }

    @Override
    public List<Cours> findByCategorie(Long categorieId) throws SQLException {
        String sql = "SELECT * FROM cours WHERE categorie_id = ? AND statut = 'PUBLIE' ORDER BY date_publication DESC";
        List<Cours> listeCours = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, categorieId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listeCours.add(mapperEntite(rs));
                }
            }
        }
        return listeCours;
    }

    @Override
    public List<Cours> findByInstructeur(Long instructeurId) throws SQLException {
        String sql = "SELECT * FROM cours WHERE instructeur_id = ? ORDER BY date_creation DESC";
        List<Cours> listeCours = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, instructeurId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listeCours.add(mapperEntite(rs));
                }
            }
        }
        return listeCours;
    }

    @Override
    public List<Cours> findByStatut(StatutCours statut) throws SQLException {
        String sql = "SELECT * FROM cours WHERE statut = ? ORDER BY date_creation DESC";
        List<Cours> listeCours = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listeCours.add(mapperEntite(rs));
                }
            }
        }
        return listeCours;
    }

    @Override
    public List<Cours> rechercher(String motCle) throws SQLException {
        String sql = "SELECT * FROM cours WHERE statut = 'PUBLIE' "
                + "AND MATCH(titre, description_courte, description_longue) AGAINST(? IN NATURAL LANGUAGE MODE)";
        List<Cours> listeCours = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, motCle);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listeCours.add(mapperEntite(rs));
                }
            }
        }
        return listeCours;
    }

    @Override
    public List<Cours> findPubliesPagines(int page, int taillePage) throws SQLException {
        String sql = "SELECT * FROM cours WHERE statut = 'PUBLIE' "
                + "ORDER BY date_publication DESC LIMIT ? OFFSET ?";
        List<Cours> listeCours = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taillePage);
            ps.setInt(2, (page - 1) * taillePage);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listeCours.add(mapperEntite(rs));
                }
            }
        }
        return listeCours;
    }

    @Override
    public boolean updateStatut(Long id, StatutCours statut) throws SQLException {
        String sql;
        if (statut == StatutCours.PUBLIE) {
            sql = "UPDATE cours SET statut = ?, date_publication = NOW() WHERE id = ?";
        } else {
            sql = "UPDATE cours SET statut = ? WHERE id = ?";
        }
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut.name());
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long countByCategorie(Long categorieId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cours WHERE categorie_id = ? AND statut = 'PUBLIE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, categorieId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return 0;
    }
}