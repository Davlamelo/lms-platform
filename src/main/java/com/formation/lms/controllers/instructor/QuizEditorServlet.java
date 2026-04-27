package com.formation.lms.controllers.instructor;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.*;
import com.formation.lms.models.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@WebServlet("/instructor/quiz/edit")
public class QuizEditorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String leconIdStr = req.getParameter("leconId");
        String coursIdStr = req.getParameter("coursId");

        if (leconIdStr == null || coursIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/instructor/dashboard");
            return;
        }

        try {
            Long leconId = Long.parseLong(leconIdStr);
            Long coursId = Long.parseLong(coursIdStr);

            // Récupérer la leçon
            LeconDAO leconDAO = DAOFactory.getLeconDAO();
            Lecon lecon = leconDAO.findById(leconId).orElse(null);

            if (lecon == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Leçon introuvable.");
                return;
            }

            // Récupérer ou créer le quiz associé à cette leçon
            QuizDAO quizDAO = DAOFactory.getQuizDAO();
            Optional<Quiz> optQuiz = quizDAO.findByLeconId(leconId);

            Quiz quiz;
            if (optQuiz.isEmpty()) {
                // Créer le quiz s'il n'existe pas encore
                quiz = new Quiz(leconId, lecon.getTitre() + " - Quiz", 70);
                quiz = quizDAO.save(quiz);
            } else {
                quiz = optQuiz.get();
            }

            // Récupérer les questions et leurs réponses
            QuestionDAO questionDAO = DAOFactory.getQuestionDAO();
            ReponseDAO reponseDAO = DAOFactory.getReponseDAO();

            List<Question> questions = questionDAO.findByQuizId(quiz.getId());
            Map<Long, List<Reponse>> reponsesParQuestion = new LinkedHashMap<>();
            for (Question q : questions) {
                reponsesParQuestion.put(q.getId(),
                        reponseDAO.findByQuestionId(q.getId()));
            }

            req.setAttribute("quiz", quiz);
            req.setAttribute("lecon", lecon);
            req.setAttribute("questions", questions);
            req.setAttribute("reponsesParQuestion", reponsesParQuestion);
            req.setAttribute("coursId", coursId);

            if (req.getParameter("succes") != null) {
                req.setAttribute("succes", req.getParameter("succes"));
            }
            if (req.getParameter("erreur") != null) {
                req.setAttribute("erreur", req.getParameter("erreur"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erreur", "Erreur lors du chargement du quiz.");
        }

        req.getRequestDispatcher("/WEB-INF/views/instructor/quiz-editor.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        String quizIdStr = req.getParameter("quizId");
        String leconIdStr = req.getParameter("leconId");
        String coursIdStr = req.getParameter("coursId");

        if (leconIdStr == null || coursIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/instructor/dashboard");
            return;
        }

        String redirectBase = req.getContextPath() +
                "/instructor/quiz/edit?leconId=" + leconIdStr +
                "&coursId=" + coursIdStr;

        try {
            Long leconId = Long.parseLong(leconIdStr);
            Long coursId = Long.parseLong(coursIdStr);
            Long quizId = (quizIdStr != null && !quizIdStr.isEmpty())
                    ? Long.parseLong(quizIdStr) : null;

            QuizDAO quizDAO = DAOFactory.getQuizDAO();
            QuestionDAO questionDAO = DAOFactory.getQuestionDAO();
            ReponseDAO reponseDAO = DAOFactory.getReponseDAO();

            switch (action != null ? action : "") {

                // === Mettre à jour les paramètres du quiz ===
                case "mettreAJourQuiz" -> {
                    if (quizId == null) break;
                    Quiz quiz = quizDAO.findById(quizId)
                            .orElseThrow(() -> new Exception("Quiz introuvable"));
                    String titre = req.getParameter("titre");
                    String scoreStr = req.getParameter("scoreMinimum");
                    quiz.setTitre(titre != null ? titre.trim() : quiz.getTitre());
                    quiz.setScoreMinimum(scoreStr != null && !scoreStr.isEmpty()
                            ? Integer.parseInt(scoreStr) : 70);
                    quizDAO.update(quiz);
                    resp.sendRedirect(redirectBase +
                            "&succes=Paramètres+du+quiz+enregistrés");
                }

                // === Ajouter une question ===
                case "ajouterQuestion" -> {
                    if (quizId == null) break;
                    String enonce = req.getParameter("enonce");
                    String typeStr = req.getParameter("typeQuestion");
                    String pointsStr = req.getParameter("points");

                    if (enonce == null || enonce.trim().isEmpty()) {
                        resp.sendRedirect(redirectBase +
                                "&erreur=L'énoncé+est+obligatoire");
                        return;
                    }

                    int points = (pointsStr != null && !pointsStr.isEmpty())
                            ? Integer.parseInt(pointsStr) : 1;
                    TypeQuestion type = TypeQuestion.valueOf(
                            typeStr != null ? typeStr : "QCM_UNIQUE");

                    long nbQuestions = questionDAO.countByQuizId(quizId);
                    Question q = new Question(quizId, enonce.trim(),
                            type, points, (int) nbQuestions + 1);
                    questionDAO.save(q);

                    resp.sendRedirect(redirectBase +
                            "&succes=Question+ajoutée+avec+succès");
                }

                // === Ajouter une réponse ===
                case "ajouterReponse" -> {
                    String questionIdStr = req.getParameter("questionId");
                    if (questionIdStr == null) break;

                    Long questionId = Long.parseLong(questionIdStr);
                    String texte = req.getParameter("texte");
                    boolean estCorrecte = "on".equals(
                            req.getParameter("estCorrecte"));

                    if (texte == null || texte.trim().isEmpty()) {
                        resp.sendRedirect(redirectBase +
                                "&erreur=Le+texte+de+la+réponse+est+obligatoire");
                        return;
                    }

                    List<Reponse> existantes =
                            reponseDAO.findByQuestionId(questionId);
                    Reponse r = new Reponse(questionId, texte.trim(),
                            estCorrecte, existantes.size() + 1);
                    reponseDAO.save(r);

                    resp.sendRedirect(redirectBase +
                            "&succes=Réponse+ajoutée");
                }

                // === Supprimer une question ===
                case "supprimerQuestion" -> {
                    String questionIdStr = req.getParameter("questionId");
                    if (questionIdStr == null) break;
                    Long questionId = Long.parseLong(questionIdStr);

                    // Supprimer d'abord les réponses
                    List<Reponse> reponses =
                            reponseDAO.findByQuestionId(questionId);
                    for (Reponse r : reponses) {
                        reponseDAO.delete(r.getId());
                    }
                    questionDAO.delete(questionId);

                    resp.sendRedirect(redirectBase +
                            "&succes=Question+supprimée");
                }

                // === Supprimer une réponse ===
                case "supprimerReponse" -> {
                    String reponseIdStr = req.getParameter("reponseId");
                    if (reponseIdStr == null) break;
                    reponseDAO.delete(Long.parseLong(reponseIdStr));
                    resp.sendRedirect(redirectBase +
                            "&succes=Réponse+supprimée");
                }

                default -> resp.sendRedirect(redirectBase);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(redirectBase + "&erreur=Erreur+technique");
        }
    }
}