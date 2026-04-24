package com.formation.lms.dao.factory;

import com.formation.lms.dao.interfaces.*;
import com.formation.lms.dao.impl.*;

/**
 * Factory qui fournit les implémentations DAO.
 * Les Services ne connaissent QUE les interfaces — la Factory
 * se charge de fournir la bonne implémentation.
 *
 * PATTERN RÉCURRENT : si tu migres de MySQL vers PostgreSQL,
 * tu changes juste les classes retournées ici.
 *
 * Usage dans un Service :
 *   UtilisateurDAO dao = DAOFactory.getUtilisateurDAO();
 */
public class DAOFactory {

    public static UtilisateurDAO getUtilisateurDAO() {
        return new UtilisateurDAOImpl();
    }

    public static CategorieDAO getCategorieDAO() {
        return new CategorieDAOImpl();
    }

    public static CoursDAO getCoursDAO() {
        return new CoursDAOImpl();
    }

    public static SectionDAO getSectionDAO() {
        return new SectionDAOImpl();
    }

    public static LeconDAO getLeconDAO() {
        return new LeconDAOImpl();
    }

    public static QuizDAO getQuizDAO() {
        return new QuizDAOImpl();
    }

    public static QuestionDAO getQuestionDAO() {
        return new QuestionDAOImpl();
    }

    public static ReponseDAO getReponseDAO() {
        return new ReponseDAOImpl();
    }

    public static CandidatureInstructeurDAO getCandidatureInstructeurDAO() {
        return new CandidatureInstructeurDAOImpl();
    }

    public static InscriptionDAO getInscriptionDAO() {
        return new InscriptionDAOImpl();
    }

    public static ProgressionLeconDAO getProgressionLeconDAO() {
        return new ProgressionLeconDAOImpl();
    }

    public static TentativeQuizDAO getTentativeQuizDAO() {
        return new TentativeQuizDAOImpl();
    }

    public static AvisDAO getAvisDAO() {
        return new AvisDAOImpl();
    }

    public static FilDiscussionDAO getFilDiscussionDAO() {
        return new FilDiscussionDAOImpl();
    }

    public static MessageDiscussionDAO getMessageDiscussionDAO() {
        return new MessageDiscussionDAOImpl();
    }

    public static CertificatDAO getCertificatDAO() {
        return new CertificatDAOImpl();
    }

    private DAOFactory() {
    }
}