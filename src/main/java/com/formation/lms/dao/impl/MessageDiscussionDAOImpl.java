package com.formation.lms.dao.impl;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.interfaces.MessageDiscussionDAO;
import com.formation.lms.models.MessageDiscussion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDiscussionDAOImpl implements MessageDiscussionDAO {

    private MessageDiscussion mapperEntite(ResultSet rs) throws SQLException {
        MessageDiscussion msg = new MessageDiscussion();
        msg.setId(rs.getLong("id"));
        msg.setFilId(rs.getLong("fil_id"));
        msg.setAuteurId(rs.getLong("auteur_id"));
        msg.setContenu(rs.getString("contenu"));
        msg.setEstReponseOfficielle(rs.getBoolean("est_reponse_officielle"));
        msg.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        return msg;
    }

    @Override
    public Optional<MessageDiscussion> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM messages_discussion WHERE id = ?";
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
    public List<MessageDiscussion> findAll() throws SQLException {
        String sql = "SELECT * FROM messages_discussion ORDER BY date_creation ASC";
        List<MessageDiscussion> messages = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) messages.add(mapperEntite(rs));
        }
        return messages;
    }

    @Override
    public MessageDiscussion save(MessageDiscussion msg) throws SQLException {
        String sql = "INSERT INTO messages_discussion (fil_id, auteur_id, contenu) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, msg.getFilId());
            ps.setLong(2, msg.getAuteurId());
            ps.setString(3, msg.getContenu());
            ps.executeUpdate();
            try (ResultSet cles = ps.getGeneratedKeys()) {
                if (cles.next()) msg.setId(cles.getLong(1));
            }
        }
        return msg;
    }

    @Override
    public boolean update(MessageDiscussion msg) throws SQLException {
        String sql = "UPDATE messages_discussion SET contenu = ?, est_reponse_officielle = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, msg.getContenu());
            ps.setBoolean(2, msg.isEstReponseOfficielle());
            ps.setLong(3, msg.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM messages_discussion WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM messages_discussion";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public List<MessageDiscussion> findByFilId(Long filId) throws SQLException {
        String sql = "SELECT * FROM messages_discussion WHERE fil_id = ? ORDER BY date_creation ASC";
        List<MessageDiscussion> messages = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, filId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) messages.add(mapperEntite(rs));
            }
        }
        return messages;
    }

    @Override
    public boolean marquerReponseOfficielle(Long id) throws SQLException {
        String sql = "UPDATE messages_discussion SET est_reponse_officielle = TRUE WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}