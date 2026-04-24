package com.formation.lms.dao.interfaces;

import com.formation.lms.models.Role;
import com.formation.lms.models.Utilisateur;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des utilisateurs.
 * Hérite du CRUD générique + ajoute des méthodes métier.
 */
public interface UtilisateurDAO extends GenericDAO<Utilisateur, Long> {

    /**
     * Trouve un utilisateur par son email (pour la connexion).
     */
    Optional<Utilisateur> findByEmail(String email) throws SQLException;

    /**
     * Récupère tous les utilisateurs ayant un rôle donné.
     */
    List<Utilisateur> findByRole(Role role) throws SQLException;

    /**
     * Vérifie si un email est déjà utilisé.
     */
    boolean existsByEmail(String email) throws SQLException;

    /**
     * Active ou désactive un compte utilisateur.
     */
    boolean updateStatutActif(Long id, boolean actif) throws SQLException;
}