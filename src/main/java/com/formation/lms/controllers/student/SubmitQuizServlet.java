package com.formation.lms.controllers.student;

import com.formation.lms.models.Utilisateur;
import com.formation.lms.services.QuizService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traite la soumission d'un quiz.
 * Récupère les réponses, évalue, enregistre et affiche le résultat.
 */
@WebServlet("/student/submit-quiz")
public class SubmitQuizServlet extends HttpServlet {

    private QuizService quizService;

    @Override
    public void init() throws ServletException {
        this.quizService = new QuizService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String quizIdStr = req.getParameter("quizId");
        String inscriptionIdStr = req.getParameter("inscriptionId");
        String leconIdStr = req.getParameter("leconId");
        String coursIdStr = req.getParameter("coursId");

        try {
            Long quizId = Long.parseLong(quizIdStr);
            Long inscriptionId = Long.parseLong(inscriptionIdStr);
            Long leconId = Long.parseLong(leconIdStr);
            Long coursId = Long.parseLong(coursIdStr);

            // Récupérer les réponses choisies
            Map<Long, Long> reponsesChoisies = new HashMap<>();
            Enumeration<String> parametres = req.getParameterNames();
            while (parametres.hasMoreElements()) {
                String parametre = parametres.nextElement();
                if (parametre.startsWith("question_")) {
                    Long questionId = Long.parseLong(parametre.replace("question_", ""));
                    Long reponseId = Long.parseLong(req.getParameter(parametre));
                    reponsesChoisies.put(questionId, reponseId);
                }
            }

            // Évaluer
            QuizService.ResultatQuiz resultat = quizService.evaluerEtEnregistrer(
                    quizId, inscriptionId, leconId, coursId, reponsesChoisies);

            // Log pour vérifier
            System.out.println("Score : " + resultat.tentative.getScore());
            System.out.println("Réussi : " + resultat.tentative.isEstReussi());
            System.out.println("Nb questions : " + resultat.questions.size());

            // Convertir les Maps Long→ String pour JSTL
            Map<String, Boolean> resultatParQuestionStr = new LinkedHashMap<>();
            Map<String, Long> reponseChoisieStr = new LinkedHashMap<>();

            for (Map.Entry<Long, Boolean> entry : resultat.resultatParQuestion.entrySet()) {
                resultatParQuestionStr.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            for (Map.Entry<Long, Long> entry : resultat.reponseChoisie.entrySet()) {
                reponseChoisieStr.put(String.valueOf(entry.getKey()), entry.getValue());
            }

            req.setAttribute("quiz", resultat.quiz);
            req.setAttribute("questions", resultat.questions);
            req.setAttribute("tentative", resultat.tentative);
            req.setAttribute("reponsesParQuestion", resultat.reponsesParQuestion);
            req.setAttribute("resultatParQuestion", resultatParQuestionStr);
            req.setAttribute("reponseChoisie", reponseChoisieStr);
            req.setAttribute("coursId", coursId);
            req.setAttribute("leconId", leconId);

            req.getRequestDispatcher("/WEB-INF/views/student/quiz-result.jsp")
                    .forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() +
                    "/student/learn?coursId=" + coursIdStr + "&leconId=" + leconIdStr);
        }

}}