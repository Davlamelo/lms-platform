package com.formation.lms.dao.interfaces;

import com.formation.lms.models.Question;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour la gestion des questions de quiz.
 */
public interface QuestionDAO extends GenericDAO<Question, Long> {

    /**
     * Récupère toutes les questions d'un quiz, triées par ordre.
     */
    List<Question> findByQuizId(Long quizId) throws SQLException;

    /**
     * Compte le nombre de questions dans un quiz.
     */
    long countByQuizId(Long quizId) throws SQLException;
}