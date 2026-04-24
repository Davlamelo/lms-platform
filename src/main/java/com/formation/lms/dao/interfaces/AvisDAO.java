package com.formation.lms.dao.interfaces;

import com.formation.lms.models.Avis;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour les avis sur les cours.
 */
public interface AvisDAO extends GenericDAO<Avis, Long> {

    /**
     * Récupère tous les avis d'un cours.
     */
    List<Avis> findByCoursId(Long coursId) throws SQLException;

    /**
     * Trouve l'avis d'un apprenant sur un cours (unique).
     */
    Optional<Avis> findByApprenantEtCours(Long apprenantId, Long coursId) throws SQLException;

    /**
     * Calcule la note moyenne d'un cours.
     */
    double getNoteMoyenne(Long coursId) throws SQLException;

    /**
     * Compte le nombre d'avis pour un cours.
     */
    long countByCoursId(Long coursId) throws SQLException;
}