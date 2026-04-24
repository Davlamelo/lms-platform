package com.formation.lms.dao.interfaces;

import com.formation.lms.models.Lecon;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour la gestion des leçons.
 */
public interface LeconDAO extends GenericDAO<Lecon, Long> {

    /**
     * Récupère toutes les leçons d'une section, triées par ordre.
     */
    List<Lecon> findBySectionId(Long sectionId) throws SQLException;

    /**
     * Récupère toutes les leçons d'un cours (toutes sections confondues).
     */
    List<Lecon> findByCoursId(Long coursId) throws SQLException;

    /**
     * Compte le nombre total de leçons dans un cours.
     */
    long countByCoursId(Long coursId) throws SQLException;
}