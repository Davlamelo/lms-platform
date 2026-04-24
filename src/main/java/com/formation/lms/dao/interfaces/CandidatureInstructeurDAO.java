package com.formation.lms.dao.interfaces;

import com.formation.lms.models.CandidatureInstructeur;
import com.formation.lms.models.StatutCandidature;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour la gestion des candidatures instructeur.
 */
public interface CandidatureInstructeurDAO extends GenericDAO<CandidatureInstructeur, Long> {

    /**
     * Récupère les candidatures par statut (pour le tableau admin).
     */
    List<CandidatureInstructeur> findByStatut(StatutCandidature statut) throws SQLException;

    /**
     * Récupère les candidatures d'un utilisateur donné.
     */
    List<CandidatureInstructeur> findByUtilisateurId(Long utilisateurId) throws SQLException;

    /**
     * Met à jour le statut d'une candidature (approuver/rejeter).
     */
    boolean updateStatut(Long id, StatutCandidature statut, String commentaireAdmin) throws SQLException;
}