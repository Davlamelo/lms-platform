package com.formation.lms.dao.interfaces;

import com.formation.lms.models.TentativeQuiz;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour les tentatives de quiz.
 */
public interface TentativeQuizDAO extends GenericDAO<TentativeQuiz, Long> {

    /**
     * Récupère toutes les tentatives d'un quiz pour une inscription.
     */
    List<TentativeQuiz> findByInscriptionEtQuiz(Long inscriptionId, Long quizId) throws SQLException;

    /**
     * Récupère toutes les tentatives d'une inscription.
     */
    List<TentativeQuiz> findByInscriptionId(Long inscriptionId) throws SQLException;

    /**
     * Récupère la meilleure tentative d'un quiz pour une inscription.
     */
    TentativeQuiz findMeilleureTentative(Long inscriptionId, Long quizId) throws SQLException;
}