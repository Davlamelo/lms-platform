package com.formation.lms.controllers.instructor;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.models.*;
import com.formation.lms.services.InstructeurService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Gère l'affichage ET les actions sur le curriculum (sections + leçons).
 * GET  → affiche l'éditeur
 * POST → traite les actions (ajout section, ajout leçon, suppression, soumission)
 */
@WebServlet("/instructor/course/curriculum")
public class CurriculumServlet extends HttpServlet {

    private InstructeurService instructeurService;

    @Override
    public void init() throws ServletException {
        this.instructeurService = new InstructeurService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");
        String coursIdStr = req.getParameter("coursId");

        if (coursIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/instructor/dashboard");
            return;
        }

        try {
            Long coursId = Long.parseLong(coursIdStr);

            // Récupérer le cours
            Optional<Cours> optCours = DAOFactory.getCoursDAO().findById(coursId);
            if (optCours.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Cours cours = optCours.get();

            // Vérifier que l'instructeur est le propriétaire
            if (!cours.getInstructeurId().equals(utilisateur.getId())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            // Récupérer sections + leçons
            List<Section> sections = instructeurService.getSections(coursId);
            Map<Long, List<Lecon>> leconsParSection = new LinkedHashMap<>();
            for (Section section : sections) {
                leconsParSection.put(section.getId(),
                        instructeurService.getLecons(section.getId()));
            }

            req.setAttribute("cours", cours);
            req.setAttribute("sections", sections);
            req.setAttribute("leconsParSection", leconsParSection);

            // Message de succès si redirigé depuis une action
            String succes = req.getParameter("succes");
            if (succes != null) {
                req.setAttribute("succes", succes);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erreur", "Erreur lors du chargement.");
        }

        req.getRequestDispatcher("/WEB-INF/views/instructor/curriculum.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");
        String action = req.getParameter("action");
        String coursIdStr = req.getParameter("coursId");
        Long coursId = Long.parseLong(coursIdStr);

        try {
            switch (action) {

                case "ajouterSection" -> {
                    String titreSection = req.getParameter("titreSection");
                    instructeurService.ajouterSection(coursId, titreSection, utilisateur.getId());
                    resp.sendRedirect(req.getContextPath() +
                            "/instructor/course/curriculum?coursId=" + coursId +
                            "&succes=Section+ajoutée+avec+succès");
                }

                case "ajouterLecon" -> {
                    Long sectionId = Long.parseLong(req.getParameter("sectionId"));
                    String titreLecon = req.getParameter("titreLecon");
                    String typeStr = req.getParameter("typeLecon");
                    String contenu = req.getParameter("contenu");
                    int duree = Integer.parseInt(req.getParameter("dureeMin"));
                    TypeLecon type = TypeLecon.valueOf(typeStr);

                    instructeurService.ajouterLecon(sectionId, titreLecon, type, contenu, duree);
                    resp.sendRedirect(req.getContextPath() +
                            "/instructor/course/curriculum?coursId=" + coursId +
                            "&succes=Leçon+ajoutée+avec+succès");
                }

                case "supprimerSection" -> {
                    Long sectionId = Long.parseLong(req.getParameter("sectionId"));
                    instructeurService.supprimerSection(sectionId);
                    resp.sendRedirect(req.getContextPath() +
                            "/instructor/course/curriculum?coursId=" + coursId);
                }

                case "supprimerLecon" -> {
                    Long leconId = Long.parseLong(req.getParameter("leconId"));
                    instructeurService.supprimerLecon(leconId);
                    resp.sendRedirect(req.getContextPath() +
                            "/instructor/course/curriculum?coursId=" + coursId);
                }

                case "soumettre" -> {
                    instructeurService.soumettrePourValidation(coursId, utilisateur.getId());
                    resp.sendRedirect(req.getContextPath() +
                            "/instructor/dashboard?succes=Cours+soumis+pour+validation");
                }

                default -> resp.sendRedirect(req.getContextPath() +
                        "/instructor/course/curriculum?coursId=" + coursId);
            }

        } catch (IllegalStateException e) {
            resp.sendRedirect(req.getContextPath() +
                    "/instructor/course/curriculum?coursId=" + coursId +
                    "&erreur=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() +
                    "/instructor/course/curriculum?coursId=" + coursId);
        }
    }
}