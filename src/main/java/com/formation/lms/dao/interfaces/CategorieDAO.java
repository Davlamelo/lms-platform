package com.formation.lms.dao.interfaces;

import com.formation.lms.models.Categorie;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des catégories de cours.
 */
public interface CategorieDAO extends GenericDAO<Categorie, Long> {

    /**
     * Trouve une catégorie par son slug (pour les URL).
     */
    Optional<Categorie> findBySlug(String slug) throws SQLException;

    /**
     * Vérifie si un nom de catégorie existe déjà.
     */
    boolean existsByNom(String nom) throws SQLException;
}