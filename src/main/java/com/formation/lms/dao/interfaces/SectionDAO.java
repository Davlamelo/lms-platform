package com.formation.lms.dao.interfaces;

import com.formation.lms.models.Section;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour la gestion des sections de cours.
 */
public interface SectionDAO extends GenericDAO<Section, Long> {

    /**
     * Récupère toutes les sections d'un cours, triées par ordre.
     */
    List<Section> findByCoursId(Long coursId) throws SQLException;

    /**
     * Compte le nombre de sections dans un cours.
     */
    long countByCoursId(Long coursId) throws SQLException;
}