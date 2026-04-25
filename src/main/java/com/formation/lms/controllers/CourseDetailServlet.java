package com.formation.lms.controllers;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.UtilisateurDAO;
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

@WebServlet("/course")
public class CourseDetailServlet extends HttpServlet {

    private CoursService coursService;
    private InscriptionService inscriptionService;
    private UtilisateurDAO utilisateurDAO;

    @Override
    public void init() throws ServletException {
        this.coursService = new CoursService();
        this.inscriptionService = new InscriptionService();
        this.utilisateurDAO = DAOFactory.getUtilisateurDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String slug = req.getParameter("slug");

        if (slug == null || slug.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/catalog");
            return;
        }

        try {
            // Récupérer le cours
            Optional<Cours> optCours = coursService.getCoursParSlug(slug);

            if (optCours.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Cours introuvable.");
                return;
            }

            Cours cours = optCours.get();

            // Récupérer l'instructeur
            Optional<Utilisateur> optInstructeur = utilisateurDAO.findById(cours.getInstructeurId());
            Utilisateur instructeur = optInstructeur.orElse(null);

            // Récupérer les sections et leçons
            List<Section> sections = coursService.getSections(cours.getId());
            Map<Long, List<Lecon>> leconsParSection = new LinkedHashMap<>();
            for (Section section : sections) {
                List<Lecon> lecons = coursService.getLecons(section.getId());
                leconsParSection.put(section.getId(), lecons);
            }

            // Statistiques
            double noteMoyenne = coursService.getNoteMoyenne(cours.getId());
            long nombreAvis = coursService.getNombreAvis(cours.getId());
            long nombreInscrits = coursService.getNombreInscrits(cours.getId());
            long nombreLecons = coursService.getNombreLecons(cours.getId());

            // Vérifier si l'utilisateur connecté est inscrit
            boolean estInscrit = false;
            Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");
            if (utilisateur != null) {
                estInscrit = inscriptionService.estInscrit(utilisateur.getId(), cours.getId());
            }

            // Avis
            List<Avis> avis = DAOFactory.getAvisDAO().findByCoursId(cours.getId());

            // Passer les données à la JSP
            req.setAttribute("cours", cours);
            req.setAttribute("instructeur", instructeur);
            req.setAttribute("sections", sections);
            req.setAttribute("leconsParSection", leconsParSection);
            req.setAttribute("noteMoyenne", noteMoyenne);
            req.setAttribute("nombreAvis", nombreAvis);
            req.setAttribute("nombreInscrits", nombreInscrits);
            req.setAttribute("nombreLecons", nombreLecons);
            req.setAttribute("estInscrit", estInscrit);
            req.setAttribute("avis", avis);

        } catch (Exception e) {
            req.setAttribute("erreur", "Erreur lors du chargement du cours.");
            e.printStackTrace();
        }

        req.getRequestDispatcher("/WEB-INF/views/public/course-detail.jsp").forward(req, resp);
    }
}