package com.formation.lms.controllers;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.UtilisateurDAO;
import com.formation.lms.models.*;
import com.formation.lms.services.AvisService;
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
    private AvisService avisService;
    private UtilisateurDAO utilisateurDAO;

    @Override
    public void init() throws ServletException {
        this.coursService = new CoursService();
        this.inscriptionService = new InscriptionService();
        this.avisService = new AvisService();
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
            // === 1. Récupérer le cours ===
            Optional<Cours> optCours = coursService.getCoursParSlug(slug);

            if (optCours.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Cours introuvable.");
                return;
            }

            Cours cours = optCours.get();

            // === 2. Vérifier les droits d'accès ===
            Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");

            boolean estAdmin = utilisateur != null
                    && utilisateur.getRole() == Role.ADMIN;

            boolean estInstructeurProprietaire = utilisateur != null
                    && utilisateur.getRole() == Role.INSTRUCTEUR
                    && utilisateur.getId().equals(cours.getInstructeurId());

            // Un cours non publié n'est accessible qu'à l'admin
            // et à l'instructeur propriétaire
            if (cours.getStatut() != StatutCours.PUBLIE
                    && !estAdmin
                    && !estInstructeurProprietaire) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Ce cours n'est pas encore disponible.");
                return;
            }

            // === 3. Vérifier si l'apprenant est inscrit ===
            boolean estInscrit = false;
            if (utilisateur != null && !estAdmin && !estInstructeurProprietaire) {
                estInscrit = inscriptionService.estInscrit(utilisateur.getId(), cours.getId());
            }

            // === 4. Récupérer l'instructeur ===
            Optional<Utilisateur> optInstructeur =
                    utilisateurDAO.findById(cours.getInstructeurId());
            Utilisateur instructeur = optInstructeur.orElse(null);

            // === 5. Récupérer les sections et leçons ===
            List<Section> sections = coursService.getSections(cours.getId());
            Map<Long, List<Lecon>> leconsParSection = new LinkedHashMap<>();
            for (Section section : sections) {
                List<Lecon> lecons = coursService.getLecons(section.getId());
                leconsParSection.put(section.getId(), lecons);
            }

            // === 6. Statistiques du cours ===
            double noteMoyenne = coursService.getNoteMoyenne(cours.getId());
            long nombreAvis = coursService.getNombreAvis(cours.getId());
            long nombreInscrits = coursService.getNombreInscrits(cours.getId());
            long nombreLecons = coursService.getNombreLecons(cours.getId());

            // === 7. Avis ===
            List<Avis> avis = avisService.getAvisCours(cours.getId());

            // Avis de l'apprenant connecté (s'il est inscrit)
            Avis monAvis = null;
            if (utilisateur != null && estInscrit) {
                monAvis = avisService.getAvisApprenant(utilisateur.getId(), cours.getId())
                        .orElse(null);
            }

            // === 8. Messages succès/erreur (venant d'une redirection) ===
            String succes = req.getParameter("succes");
            String erreur = req.getParameter("erreur");
            if (succes != null) req.setAttribute("succes", succes);
            if (erreur != null) req.setAttribute("erreur", erreur);

            // === 9. Passer toutes les données à la JSP ===
            req.setAttribute("cours", cours);
            req.setAttribute("instructeur", instructeur);
            req.setAttribute("sections", sections);
            req.setAttribute("leconsParSection", leconsParSection);
            req.setAttribute("noteMoyenne", noteMoyenne);
            req.setAttribute("nombreAvis", nombreAvis);
            req.setAttribute("nombreInscrits", nombreInscrits);
            req.setAttribute("nombreLecons", nombreLecons);
            req.setAttribute("estInscrit", estInscrit);
            req.setAttribute("estAdmin", estAdmin);
            req.setAttribute("estInstructeurProprietaire", estInstructeurProprietaire);
            req.setAttribute("avis", avis);
            req.setAttribute("monAvis", monAvis);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erreur", "Erreur lors du chargement du cours.");
        }

        req.getRequestDispatcher("/WEB-INF/views/public/course-detail.jsp")
                .forward(req, resp);
    }
}