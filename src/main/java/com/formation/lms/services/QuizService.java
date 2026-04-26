package com.formation.lms.services;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.*;
import com.formation.lms.models.*;

import java.sql.SQLException;
import java.util.*;

/**
 * Service métier pour la gestion des quiz.
 * Évalue les réponses, calcule les scores et enregistre les tentatives.
 */
public class QuizService {

    private final QuizDAO quizDAO;
    private final QuestionDAO questionDAO;
    private final ReponseDAO reponseDAO;
    private final TentativeQuizDAO tentativeDAO;
    private final InscriptionDAO inscriptionDAO;
    private final ProgressionLeconDAO progressionDAO;
    private final LeconDAO leconDAO;

    public QuizService() {
        this.quizDAO = DAOFactory.getQuizDAO();
        this.questionDAO = DAOFactory.getQuestionDAO();
        this.reponseDAO = DAOFactory.getReponseDAO();
        this.tentativeDAO = DAOFactory.getTentativeQuizDAO();
        this.inscriptionDAO = DAOFactory.getInscriptionDAO();
        this.progressionDAO = DAOFactory.getProgressionLeconDAO();
        this.leconDAO = DAOFactory.getLeconDAO();
    }

    /**
     * Évalue les réponses soumises par l'apprenant et enregistre la tentative.
     *
     * @param quizId       l'ID du quiz
     * @param inscriptionId l'ID de l'inscription
     * @param leconId      l'ID de la leçon de type QUIZ
     * @param coursId      l'ID du cours (pour recalculer la progression)
     * @param reponsesChoisies map : questionId → reponseId choisie
     * @return la tentative enregistrée avec le score
     */
    public ResultatQuiz evaluerEtEnregistrer(Long quizId, Long inscriptionId,
                                             Long leconId, Long coursId,
                                             Map<Long, Long> reponsesChoisies)
            throws SQLException {

        // 1. Récupérer le quiz
        Quiz quiz = quizDAO.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz introuvable."));

        // 2. Récupérer toutes les questions du quiz
        List<Question> questions = questionDAO.findByQuizId(quizId);

        int pointsTotal = 0;
        int pointsObtenus = 0;

        // Détail par question pour l'affichage du résultat
        Map<Long, Boolean> resultatParQuestion = new LinkedHashMap<>();
        Map<Long, List<Reponse>> toutesReponsesParQuestion = new LinkedHashMap<>();
        Map<Long, Long> reponseChoisieParQuestion = new LinkedHashMap<>();

        // 3. Évaluer chaque question
        for (Question question : questions) {
            pointsTotal += question.getPoints();

            List<Reponse> toutesReponses = reponseDAO.findByQuestionId(question.getId());
            toutesReponsesParQuestion.put(question.getId(), toutesReponses);

            Long reponseChoisieId = reponsesChoisies.get(question.getId());
            reponseChoisieParQuestion.put(question.getId(), reponseChoisieId);

            // Vérifier si la réponse choisie est correcte
            boolean estCorrect = false;
            if (reponseChoisieId != null) {
                for (Reponse reponse : toutesReponses) {
                    if (reponse.getId().equals(reponseChoisieId) && reponse.isEstCorrecte()) {
                        estCorrect = true;
                        pointsObtenus += question.getPoints();
                        break;
                    }
                }
            }
            resultatParQuestion.put(question.getId(), estCorrect);
        }

        // 4. Calculer le score en pourcentage
        double score = pointsTotal > 0
                ? Math.round((double) pointsObtenus / pointsTotal * 100 * 10.0) / 10.0
                : 0;

        boolean estReussi = score >= quiz.getScoreMinimum();

        // 5. Enregistrer la tentative
        TentativeQuiz tentative = new TentativeQuiz(inscriptionId, quizId);
        tentative.setScore(score);
        tentative.setPointsObtenus(pointsObtenus);
        tentative.setPointsTotal(pointsTotal);
        tentative.setEstReussi(estReussi);
        tentative = tentativeDAO.save(tentative);

        // 6. Si réussi → marquer la leçon comme complétée + recalculer progression
        if (estReussi) {
            // Marquer ou créer la progression de cette leçon
            Optional<ProgressionLecon> existante = progressionDAO
                    .findByInscriptionEtLecon(inscriptionId, leconId);

            if (existante.isPresent()) {
                progressionDAO.marquerCompletee(inscriptionId, leconId);
            } else {
                ProgressionLecon prog = new ProgressionLecon(inscriptionId, leconId);
                prog.setEstCompletee(true);
                progressionDAO.save(prog);
            }

            // Recalculer le pourcentage de progression
            long totalLecons = leconDAO.countByCoursId(coursId);
            long leconsCompletees = progressionDAO.countCompletees(inscriptionId);
            double pourcentage = totalLecons > 0
                    ? (double) leconsCompletees / totalLecons * 100
                    : 0;
            inscriptionDAO.updateProgression(inscriptionId, pourcentage);
        }

        // 7. Retourner le résultat complet pour l'affichage
        return new ResultatQuiz(
                quiz, questions, tentative,
                resultatParQuestion,
                toutesReponsesParQuestion,
                reponseChoisieParQuestion
        );
    }

    /**
     * Classe interne représentant le résultat complet d'un quiz.
     * Contient toutes les données nécessaires pour afficher la correction.
     */
    public static class ResultatQuiz {
        public final Quiz quiz;
        public final List<Question> questions;
        public final TentativeQuiz tentative;
        public final Map<Long, Boolean> resultatParQuestion;      // questionId → correct ?
        public final Map<Long, List<Reponse>> reponsesParQuestion; // questionId → toutes réponses
        public final Map<Long, Long> reponseChoisie;              // questionId → reponseId choisie

        public ResultatQuiz(Quiz quiz, List<Question> questions,
                            TentativeQuiz tentative,
                            Map<Long, Boolean> resultatParQuestion,
                            Map<Long, List<Reponse>> reponsesParQuestion,
                            Map<Long, Long> reponseChoisie) {
            this.quiz = quiz;
            this.questions = questions;
            this.tentative = tentative;
            this.resultatParQuestion = resultatParQuestion;
            this.reponsesParQuestion = reponsesParQuestion;
            this.reponseChoisie = reponseChoisie;
        }
    }
}