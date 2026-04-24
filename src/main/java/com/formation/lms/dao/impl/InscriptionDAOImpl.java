package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.InscriptionDAO;
import com.formation.lms.models.Inscription;
import com.formation.lms.models.StatutInscription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InscriptionDAOImpl implements InscriptionDAO {

    private Inscription mapperEntite(ResultSet rs) throws SQLException {
        Inscription insc = new Inscription();
        insc.setId(rs.getLong("id"));
        insc.setApprenantId(rs.getLong("apprenant_id"));
        insc.setCoursId(rs.getLong("cours_id"));
        insc.setPourcentageProgression(rs.getDouble("pourcentage_progression"));
        insc.setStatut(StatutInscription.valueOf(rs.getString("statut")));
        insc.setDateInscription(rs.getTimestamp("date_inscription").toLocalDateTime());
        Timestamp dateComp = rs.getTimestamp("date_completion");
        if (dateComp != null) {
            insc.setDateCompletion(dateComp.toLocalDateTime());
        }
        insc.setDerniereActivite(rs.getTimestamp("derniere_activite").toLocalDateTime());
        return insc;
    }

    @Override
    public Optional<Inscription> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM inscriptions WHERE id = ?";
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
    public List<Inscription> findAll() throws SQLException {
        String sql = "SELECT * FROM inscriptions ORDER BY date_inscription DESC";
        List<Inscription> inscriptions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) inscriptions.add(mapperEntite(rs));
        }
        return inscriptions;
    }

    @Override
    public Inscription save(Inscription insc) throws SQLException {
        String sql = "INSERT INTO inscriptions (apprenant_id, cours_id, pourcentage_progression, statut) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, insc.getApprenantId());
            ps.setLong(2, insc.getCoursId());
            ps.setDouble(3, insc.getPourcentageProgression());
            ps.setString(4, insc.getStatut().name());
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) insc.setId(cles.getLong(1));
            }
        }
        return insc;
    }

    @Override
    public boolean update(Inscription insc) throws SQLException {
        String sql = "UPDATE inscriptions SET pourcentage_progression = ?, statut = ?, "
                + "date_completion = ?, derniere_activite = NOW() WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, insc.getPourcentageProgression());
            ps.setString(2, insc.getStatut().name());
            if (insc.getDateCompletion() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(insc.getDateCompletion()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }
            ps.setLong(4, insc.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM inscriptions WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM inscriptions";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public Optional<Inscription> findByApprenantEtCours(Long apprenantId, Long coursId) throws SQLException {
        String sql = "SELECT * FROM inscriptions WHERE apprenant_id = ? AND cours_id = ?";
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
    public boolean existeInscription(Long apprenantId, Long coursId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM inscriptions WHERE apprenant_id = ? AND cours_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, apprenantId);
            ps.setLong(2, coursId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1) > 0;
            }
        }
        return false;
    }

    @Override
    public List<Inscription> findByApprenantId(Long apprenantId) throws SQLException {
        String sql = "SELECT * FROM inscriptions WHERE apprenant_id = ? ORDER BY derniere_activite DESC";
        List<Inscription> inscriptions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, apprenantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) inscriptions.add(mapperEntite(rs));
            }
        }
        return inscriptions;
    }

    @Override
    public List<Inscription> findByCoursId(Long coursId) throws SQLException {
        String sql = "SELECT * FROM inscriptions WHERE cours_id = ? ORDER BY date_inscription DESC";
        List<Inscription> inscriptions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, coursId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) inscriptions.add(mapperEntite(rs));
            }
        }
        return inscriptions;
    }

    @Override
    public boolean updateProgression(Long id, double pourcentage) throws SQLException {
        String sql = "UPDATE inscriptions SET pourcentage_progression = ?, derniere_activite = NOW() WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, pourcentage);
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long countByCoursId(Long coursId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM inscriptions WHERE cours_id = ?";
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