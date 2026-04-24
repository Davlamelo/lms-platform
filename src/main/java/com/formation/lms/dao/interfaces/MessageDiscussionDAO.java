package com.formation.lms.dao.interfaces;

import com.formation.lms.models.MessageDiscussion;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour les messages de discussion.
 */
public interface MessageDiscussionDAO extends GenericDAO<MessageDiscussion, Long> {

    /**
     * Récupère tous les messages d'un fil de discussion.
     */
    List<MessageDiscussion> findByFilId(Long filId) throws SQLException;

    /**
     * Marque un message comme réponse officielle de l'instructeur.
     */
    boolean marquerReponseOfficielle(Long id) throws SQLException;
}