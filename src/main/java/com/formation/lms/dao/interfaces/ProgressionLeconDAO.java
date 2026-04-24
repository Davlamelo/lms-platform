package com.formation.lms.dao.interfaces;

import com.formation.lms.models.ProgressionLecon;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour le suivi de progression par leçon.
 */
public interface ProgressionLeconDAO extends GenericDAO<ProgressionLecon, Long> {

    /**
     * Trouve la progression d'une leçon pour une inscription.
     */
    Optional<ProgressionLecon> findByInscriptionEtLecon(Long inscriptionId, Long leconId) throws SQLException;

    /**
     * Récupère toute la progression d'une inscription.
     */
    List<ProgressionLecon> findByInscriptionId(Long inscriptionId) throws SQLException;

    /**
     * Compte les leçons complétées pour une inscription.
     */
    long countCompletees(Long inscriptionId) throws SQLException;

    /**
     * Marque une leçon comme complétée.
     */
    boolean marquerCompletee(Long inscriptionId, Long leconId) throws SQLException;
}