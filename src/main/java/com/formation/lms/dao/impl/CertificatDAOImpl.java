package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.CertificatDAO;
import com.formation.lms.models.Certificat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CertificatDAOImpl implements CertificatDAO {

    private Certificat mapperEntite(ResultSet rs) throws SQLException {
        Certificat cert = new Certificat();
        cert.setId(rs.getLong("id"));
        cert.setInscriptionId(rs.getLong("inscription_id"));
        cert.setCodeVerification(rs.getString("code_verification"));
        cert.setFichierPdfUrl(rs.getString("fichier_pdf_url"));
        cert.setDateEmission(rs.getTimestamp("date_emission").toLocalDateTime());
        return cert;
    }

    @Override
    public Optional<Certificat> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM certificats WHERE id = ?";
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
    public List<Certificat> findAll() throws SQLException {
        String sql = "SELECT * FROM certificats ORDER BY date_emission DESC";
        List<Certificat> certificats = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) certificats.add(mapperEntite(rs));
        }
        return certificats;
    }

    @Override
    public Certificat save(Certificat cert) throws SQLException {
        String sql = "INSERT INTO certificats (inscription_id, code_verification, fichier_pdf_url) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, cert.getInscriptionId());
            ps.setString(2, cert.getCodeVerification());
            ps.setString(3, cert.getFichierPdfUrl());
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) cert.setId(cles.getLong(1));
            }
        }
        return cert;
    }

    @Override
    public boolean update(Certificat cert) throws SQLException {
        String sql = "UPDATE certificats SET fichier_pdf_url = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cert.getFichierPdfUrl());
            ps.setLong(2, cert.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM certificats WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM certificats";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public Optional<Certificat> findByInscriptionId(Long inscriptionId) throws SQLException {
        String sql = "SELECT * FROM certificats WHERE inscription_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, inscriptionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapperEntite(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Certificat> findByCodeVerification(String code) throws SQLException {
        String sql = "SELECT * FROM certificats WHERE code_verification = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapperEntite(rs));
            }
        }
        return Optional.empty();
    }
}