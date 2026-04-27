package com.formation.lms.controllers.student;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.QuizDAO;
import com.formation.lms.models.*;
import com.formation.lms.services.CoursService;
import com.formation.lms.services.InscriptionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@WebServlet("/student/learn")
public class LearnServlet extends HttpServlet {

    private CoursService coursService;
    private InscriptionService inscriptionService;
    private QuizDAO quizDAO;

    @Override
    public void init() throws ServletException {
        this.coursService = new CoursService();
        this.inscriptionService = new InscriptionService();
        this.quizDAO = DAOFactory.getQuizDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");
        String coursIdStr = req.getParameter("coursId");

        if (coursIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/catalog");
            return;
        }

        try {
            Long coursId = Long.parseLong(coursIdStr);

            // Vérifier que l'apprenant est bien inscrit
            if (!inscriptionService.estInscrit(utilisateur.getId(), coursId)) {
                resp.sendRedirect(req.getContextPath() + "/course?slug=" +
                        coursService.getCoursById(coursId)
                                .map(Cours::getSlug).orElse(""));
                return;
            }

            // Récupérer le cours
            Optional<Cours> optCours = coursService.getCoursById(coursId);
            if (optCours.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            Cours cours = optCours.get();

            // Récupérer l'inscription
            Optional<Inscription> optInscription = inscriptionService
                    .getInscription(utilisateur.getId(), coursId);
            if (optInscription.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/catalog");
                return;
            }
            Inscription inscription = optInscription.get();

            // Récupérer les sections et leçons
            List<Section> sections = coursService.getSections(coursId);
            Map<Long, List<Lecon>> leconsParSection = new LinkedHashMap<>();
            for (Section section : sections) {
                leconsParSection.put(section.getId(), coursService.getLecons(section.getId()));
            }

            // Récupérer la progression
            List<ProgressionLecon> progressions = inscriptionService
                    .getProgression(inscription.getId());
            Set<Long> leconsCompletees = new HashSet<>();
            for (ProgressionLecon p : progressions) {
                if (p.isEstCompletee()) {
                    leconsCompletees.add(p.getLeconId());
                }
            }

            // Déterminer la leçon active
            String leconIdStr = req.getParameter("leconId");
            Lecon leconActive = null;
            Quiz quizActif = null;

            if (leconIdStr != null) {
                Long leconId = Long.parseLong(leconIdStr);
                leconActive = trouverLecon(leconsParSection, leconId);
            }

            // Si pas de leçon spécifiée → première leçon
            if (leconActive == null && !sections.isEmpty()) {
                List<Lecon> premieresLecons = leconsParSection.get(sections.get(0).getId());
                if (premieresLecons != null && !premieresLecons.isEmpty()) {
                    leconActive = premieresLecons.get(0);
                }
            }



            // Passer les données à la JSP
            req.setAttribute("cours", cours);
            req.setAttribute("inscription", inscription);
            req.setAttribute("sections", sections);
            req.setAttribute("leconsParSection", leconsParSection);
            req.setAttribute("leconsCompletees", leconsCompletees);
            req.setAttribute("leconActive", leconActive);
            req.setAttribute("quizActif", quizActif);
            // Si leçon de type QUIZ → charger le quiz
            if (leconActive != null && leconActive.getTypeLecon() == TypeLecon.QUIZ) {
                quizActif = quizDAO.findByLeconId(leconActive.getId()).orElse(null);
                if (quizActif != null) {
                    List<Question> questions = DAOFactory.getQuestionDAO()
                            .findByQuizId(quizActif.getId());
                    Map<Long, List<Reponse>> reponsesParQuestion = new LinkedHashMap<>();
                    for (Question q : questions) {
                        reponsesParQuestion.put(q.getId(),
                                DAOFactory.getReponseDAO().findByQuestionId(q.getId()));
                    }
                    req.setAttribute("questions", questions);
                    req.setAttribute("reponsesParQuestion", reponsesParQuestion);
                }
            }
            // Préparer l'URL vidéo embed si c'est une vidéo YouTube/Vimeo
            if (leconActive != null && leconActive.getTypeLecon() == TypeLecon.VIDEO
                    && leconActive.getVideoUrl() != null) {

                String url = leconActive.getVideoUrl();
                String embedUrl = null;

                if (url.contains("youtube.com") || url.contains("youtu.be")) {
                    // Extraire l'ID YouTube
                    String videoId = null;
                    if (url.contains("v=")) {
                        videoId = url.split("v=")[1].split("&")[0];
                    } else if (url.contains("youtu.be/")) {
                        videoId = url.split("youtu.be/")[1].split("\\?")[0];
                    }
                    if (videoId != null) {
                        embedUrl = "https://www.youtube.com/embed/" + videoId;
                    }
                } else if (url.contains("vimeo.com")) {
                    String[] parts = url.split("/");
                    String vimeoId = parts[parts.length - 1].split("\\?")[0];
                    embedUrl = "https://player.vimeo.com/video/" + vimeoId;
                }

                req.setAttribute("embedUrl", embedUrl);
            }

        } catch (Exception e) {
            req.setAttribute("erreur", "Erreur lors du chargement du cours.");
            e.printStackTrace();
        }

        req.getRequestDispatcher("/WEB-INF/views/student/learn.jsp").forward(req, resp);
    }

    /**
     * Trouve une leçon dans la map par son ID.
     */
    private Lecon trouverLecon(Map<Long, List<Lecon>> leconsParSection, Long leconId) {
        for (List<Lecon> lecons : leconsParSection.values()) {
            for (Lecon lecon : lecons) {
                if (lecon.getId().equals(leconId)) {
                    return lecon;
                }
            }
        }
        return null;
    }
}