package com.formation.lms.dao.interfaces;

import com.formation.lms.models.Cours;
import com.formation.lms.models.StatutCours;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des cours.
 */
public interface CoursDAO extends GenericDAO<Cours, Long> {

    /**
     * Trouve un cours par son slug.
     */
    Optional<Cours> findBySlug(String slug) throws SQLException;

    /**
     * Récupère tous les cours publiés (visibles dans le catalogue).
     */
    List<Cours> findPublies() throws SQLException;

    /**
     * Récupère les cours d'une catégorie donnée.
     */
    List<Cours> findByCategorie(Long categorieId) throws SQLException;

    /**
     * Récupère les cours créés par un instructeur.
     */
    List<Cours> findByInstructeur(Long instructeurId) throws SQLException;

    /**
     * Récupère les cours par statut (pour la modération admin).
     */
    List<Cours> findByStatut(StatutCours statut) throws SQLException;

    /**
     * Recherche de cours par mot-clé (utilise FULLTEXT search).
     */
    List<Cours> rechercher(String motCle) throws SQLException;

    /**
     * Recherche paginée de cours publiés.
     */
    List<Cours> findPubliesPagines(int page, int taillePage) throws SQLException;

    /**
     * Met à jour uniquement le statut d'un cours.
     */
    boolean updateStatut(Long id, StatutCours statut) throws SQLException;

    /**
     * Compte les cours publiés par catégorie.
     */
    long countByCategorie(Long categorieId) throws SQLException;
}