package com.formation.lms.dao.interfaces;

import com.formation.lms.models.Inscription;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des inscriptions aux cours.
 */
public interface InscriptionDAO extends GenericDAO<Inscription, Long> {

    /**
     * Trouve l'inscription d'un apprenant à un cours spécifique.
     */
    Optional<Inscription> findByApprenantEtCours(Long apprenantId, Long coursId) throws SQLException;

    /**
     * Vérifie si un apprenant est déjà inscrit à un cours.
     */
    boolean existeInscription(Long apprenantId, Long coursId) throws SQLException;

    /**
     * Récupère tous les cours auxquels un apprenant est inscrit.
     */
    List<Inscription> findByApprenantId(Long apprenantId) throws SQLException;

    /**
     * Récupère tous les inscrits d'un cours.
     */
    List<Inscription> findByCoursId(Long coursId) throws SQLException;

    /**
     * Met à jour le pourcentage de progression.
     */
    boolean updateProgression(Long id, double pourcentage) throws SQLException;

    /**
     * Compte le nombre d'inscrits à un cours.
     */
    long countByCoursId(Long coursId) throws SQLException;
}