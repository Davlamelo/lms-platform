package com.formation.lms.dao.interfaces;

import com.formation.lms.models.Quiz;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des quiz.
 */
public interface QuizDAO extends GenericDAO<Quiz, Long> {

    /**
     * Trouve le quiz associé à une leçon (relation 1-1).
     */
    Optional<Quiz> findByLeconId(Long leconId) throws SQLException;
}