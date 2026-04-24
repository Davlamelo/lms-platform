package com.formation.lms.dao.interfaces;

import com.formation.lms.models.FilDiscussion;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour les fils de discussion (Q&R).
 */
public interface FilDiscussionDAO extends GenericDAO<FilDiscussion, Long> {

    /**
     * Récupère tous les fils de discussion d'un cours.
     */
    List<FilDiscussion> findByCoursId(Long coursId) throws SQLException;

    /**
     * Récupère les fils non résolus d'un cours.
     */
    List<FilDiscussion> findNonResolus(Long coursId) throws SQLException;

    /**
     * Marque un fil comme résolu.
     */
    boolean marquerResolu(Long id) throws SQLException;
}