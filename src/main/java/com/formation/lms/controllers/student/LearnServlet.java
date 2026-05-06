package com.formation.lms.controllers.student;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.*;
import com.formation.lms.models.*;
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

    private InscriptionService inscriptionService;

    @Override
    public void init() throws ServletException {
        this.inscriptionService = new InscriptionService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession()
                .getAttribute("utilisateur");

        String coursIdStr = req.getParameter("coursId");
        String leconIdStr = req.getParameter("leconId");

        if (coursIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/student/dashboard");
            return;
        }

        try {
            Long coursId = Long.parseLong(coursIdStr);

            // === 1. Récupérer le cours ===
            Cours cours = DAOFactory.getCoursDAO().findById(coursId).orElse(null);
            if (cours == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // === 2. Vérifier l'inscription ===
            Inscription inscription = inscriptionService
                    .getInscription(utilisateur.getId(), coursId).orElse(null);
            if (inscription == null) {
                resp.sendRedirect(req.getContextPath() +
                        "/course?slug=" + cours.getSlug());
                return;
            }

            // === 3. Récupérer les sections et leçons ===
            List<Section> sections = DAOFactory.getSectionDAO()
                    .findByCoursId(coursId);
            Map<Long, List<Lecon>> leconsParSection = new LinkedHashMap<>();
            List<Lecon> toutesLecons = new ArrayList<>();

            for (Section section : sections) {
                List<Lecon> lecons = DAOFactory.getLeconDAO()
                        .findBySectionId(section.getId());
                leconsParSection.put(section.getId(), lecons);
                toutesLecons.addAll(lecons);
            }

            // === 4. Déterminer la leçon active ===
            Lecon leconActive = null;

            if (leconIdStr != null && !leconIdStr.isEmpty()) {
                Long leconId = Long.parseLong(leconIdStr);
                leconActive = DAOFactory.getLeconDAO()
                        .findById(leconId).orElse(null);
            }

            // Si aucune leçon sélectionnée → prendre la première
            if (leconActive == null && !toutesLecons.isEmpty()) {
                leconActive = toutesLecons.get(0);
            }

            // === 5. Récupérer les leçons complétées ===
            Set<Long> leconsCompletees = new HashSet<>();
            List<ProgressionLecon> progressions = DAOFactory
                    .getProgressionLeconDAO()
                    .findByInscriptionId(inscription.getId());
            for (ProgressionLecon p : progressions) {
                if (p.isEstCompletee()) {
                    leconsCompletees.add(p.getLeconId());
                }
            }

            // === 6. Préparer le contenu selon le type de leçon ===
            Quiz quizActif = null;
            List<Question> questions = new ArrayList<>();
            Map<Long, List<Reponse>> reponsesParQuestion = new LinkedHashMap<>();
            String embedUrl = null;
            boolean estVideoDirecte = false;

            if (leconActive != null) {

                // --- Cas QUIZ ---
                if (leconActive.getTypeLecon() == TypeLecon.QUIZ) {
                    quizActif = DAOFactory.getQuizDAO()
                            .findByLeconId(leconActive.getId()).orElse(null);

                    if (quizActif != null) {
                        questions = DAOFactory.getQuestionDAO()
                                .findByQuizId(quizActif.getId());
                        for (Question q : questions) {
                            reponsesParQuestion.put(q.getId(),
                                    DAOFactory.getReponseDAO()
                                            .findByQuestionId(q.getId()));
                        }
                    }
                }

                // --- Cas VIDEO (ou leçon avec videoUrl) ---
                String videoUrl = leconActive.getVideoUrl();
                if (videoUrl != null && !videoUrl.trim().isEmpty()) {
                    videoUrl = videoUrl.trim();

                    if (videoUrl.contains("youtube.com")
                            || videoUrl.contains("youtu.be")) {
                        // Extraire l'ID YouTube
                        String videoId = null;
                        if (videoUrl.contains("v=")) {
                            videoId = videoUrl.split("v=")[1].split("&")[0];
                        } else if (videoUrl.contains("youtu.be/")) {
                            videoId = videoUrl.split("youtu.be/")[1]
                                    .split("\\?")[0];
                        }
                        if (videoId != null && !videoId.isEmpty()) {
                            embedUrl = "https://www.youtube.com/embed/"
                                    + videoId;
                        }

                    } else if (videoUrl.contains("vimeo.com")) {
                        String[] parts = videoUrl.split("/");
                        String vimeoId = parts[parts.length - 1]
                                .split("\\?")[0];
                        embedUrl = "https://player.vimeo.com/video/" + vimeoId;

                    } else if (videoUrl.endsWith(".mp4")
                            || videoUrl.endsWith(".webm")
                            || videoUrl.endsWith(".ogg")) {
                        estVideoDirecte = true;
                    }
                    // Sinon : URL non reconnue → les deux flags restent false/null
                }
            }

            // === 7. Passer les attributs à la JSP ===
            req.setAttribute("cours", cours);
            req.setAttribute("inscription", inscription);
            req.setAttribute("sections", sections);
            req.setAttribute("leconsParSection", leconsParSection);
            req.setAttribute("toutesLecons", toutesLecons);
            req.setAttribute("leconActive", leconActive);
            req.setAttribute("leconsCompletees", leconsCompletees);
            req.setAttribute("quizActif", quizActif);
            req.setAttribute("questions", questions);
            req.setAttribute("reponsesParQuestion", reponsesParQuestion);
            req.setAttribute("embedUrl", embedUrl);
            req.setAttribute("estVideoDirecte", estVideoDirecte);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erreur", "Erreur lors du chargement du cours.");
        }

        req.getRequestDispatcher("/WEB-INF/views/student/learn.jsp")
                .forward(req, resp);
    }
}