package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.CandidatureInstructeurDAO;
import com.formation.lms.models.CandidatureInstructeur;
import com.formation.lms.models.StatutCandidature;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CandidatureInstructeurDAOImpl implements CandidatureInstructeurDAO {

    private CandidatureInstructeur mapperEntite(ResultSet rs) throws SQLException {
        CandidatureInstructeur cand = new CandidatureInstructeur();
        cand.setId(rs.getLong("id"));
        cand.setUtilisateurId(rs.getLong("utilisateur_id"));
        cand.setMotivation(rs.getString("motivation"));
        cand.setExpertise(rs.getString("expertise"));
        cand.setStatut(StatutCandidature.valueOf(rs.getString("statut")));
        cand.setCommentaireAdmin(rs.getString("commentaire_admin"));
        cand.setDateSoumission(rs.getTimestamp("date_soumission").toLocalDateTime());
        Timestamp dateTraitement = rs.getTimestamp("date_traitement");
        if (dateTraitement != null) {
            cand.setDateTraitement(dateTraitement.toLocalDateTime());
        }
        return cand;
    }

    @Override
    public Optional<CandidatureInstructeur> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM candidatures_instructeur WHERE id = ?";
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
    public List<CandidatureInstructeur> findAll() throws SQLException {
        String sql = "SELECT * FROM candidatures_instructeur ORDER BY date_soumission DESC";
        List<CandidatureInstructeur> candidatures = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) candidatures.add(mapperEntite(rs));
        }
        return candidatures;
    }

    @Override
    public CandidatureInstructeur save(CandidatureInstructeur cand) throws SQLException {
        String sql = "INSERT INTO candidatures_instructeur (utilisateur_id, motivation, expertise, statut) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, cand.getUtilisateurId());
            ps.setString(2, cand.getMotivation());
            ps.setString(3, cand.getExpertise());
            ps.setString(4, cand.getStatut().name());
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) cand.setId(cles.getLong(1));
            }
        }
        return cand;
    }

    @Override
    public boolean update(CandidatureInstructeur cand) throws SQLException {
        String sql = "UPDATE candidatures_instructeur SET motivation = ?, expertise = ?, "
                + "statut = ?, commentaire_admin = ?, date_traitement = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cand.getMotivation());
            ps.setString(2, cand.getExpertise());
            ps.setString(3, cand.getStatut().name());
            ps.setString(4, cand.getCommentaireAdmin());
            if (cand.getDateTraitement() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(cand.getDateTraitement()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            ps.setLong(6, cand.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM candidatures_instructeur WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM candidatures_instructeur";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public List<CandidatureInstructeur> findByStatut(StatutCandidature statut) throws SQLException {
        String sql = "SELECT * FROM candidatures_instructeur WHERE statut = ? ORDER BY date_soumission DESC";
        List<CandidatureInstructeur> candidatures = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) candidatures.add(mapperEntite(rs));
            }
        }
        return candidatures;
    }

    @Override
    public List<CandidatureInstructeur> findByUtilisateurId(Long utilisateurId) throws SQLException {
        String sql = "SELECT * FROM candidatures_instructeur WHERE utilisateur_id = ? ORDER BY date_soumission DESC";
        List<CandidatureInstructeur> candidatures = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, utilisateurId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) candidatures.add(mapperEntite(rs));
            }
        }
        return candidatures;
    }

    @Override
    public boolean updateStatut(Long id, StatutCandidature statut, String commentaireAdmin) throws SQLException {
        String sql = "UPDATE candidatures_instructeur SET statut = ?, commentaire_admin = ?, "
                + "date_traitement = NOW() WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut.name());
            ps.setString(2, commentaireAdmin);
            ps.setLong(3, id);
            return ps.executeUpdate() > 0;
        }
    }
}