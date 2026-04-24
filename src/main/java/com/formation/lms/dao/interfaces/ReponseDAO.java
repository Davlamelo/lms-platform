package com.formation.lms.dao.interfaces;

import com.formation.lms.models.Reponse;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour la gestion des réponses aux questions.
 */
public interface ReponseDAO extends GenericDAO<Reponse, Long> {

    /**
     * Récupère toutes les réponses d'une question, triées par ordre.
     */
    List<Reponse> findByQuestionId(Long questionId) throws SQLException;

    /**
     * Récupère uniquement les réponses correctes d'une question.
     */
    List<Reponse> findReponsesCorrectes(Long questionId) throws SQLException;
}