package com.formation.lms.controllers.instructor;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.models.Cours;
import com.formation.lms.models.StatutCours;
import com.formation.lms.models.Utilisateur;
import com.formation.lms.services.InstructeurService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/instructor/dashboard")
public class InstructorDashboardServlet extends HttpServlet {

    private InstructeurService instructeurService;

    @Override
    public void init() throws ServletException {
        this.instructeurService = new InstructeurService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");

        try {
            // Récupérer les cours de l'instructeur
            List<Cours> cours = instructeurService.getCoursInstructeur(utilisateur.getId());

            // Stats globales
            Map<String, Object> stats = instructeurService
                    .getStatistiquesInstructeur(utilisateur.getId());

            // Stats par cours
            Map<Long, Map<String, Object>> statsParCours = new HashMap<>();
            for (Cours c : cours) {
                statsParCours.put(c.getId(), instructeurService.getStatsCours(c.getId()));
            }

            // Catégories pour le formulaire de création
            List<com.formation.lms.models.Categorie> categories =
                    DAOFactory.getCategorieDAO().findAll();

            req.setAttribute("cours", cours);
            req.setAttribute("stats", stats);
            req.setAttribute("statsParCours", statsParCours);
            req.setAttribute("categories", categories);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erreur", "Erreur lors du chargement du dashboard.");
        }

        req.getRequestDispatcher("/WEB-INF/views/instructor/dashboard.jsp").forward(req, resp);
    }

    // AJOUTÉ : gestion des actions retirer et supprimer
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");
        String action = req.getParameter("action");
        String coursIdStr = req.getParameter("coursId");

        if (coursIdStr == null || coursIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/instructor/dashboard");
            return;
        }

        Long coursId = Long.parseLong(coursIdStr);

        try {
            Optional<Cours> optCours = DAOFactory.getCoursDAO().findById(coursId);
            if (optCours.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/instructor/dashboard?erreur="
                        + enc("Cours introuvable"));
                return;
            }

            Cours cours = optCours.get();

            // Vérification que l'instructeur est bien le propriétaire
            if (!cours.getInstructeurId().equals(utilisateur.getId())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            switch (action != null ? action : "") {

                case "retirer" -> {
                    // Repasse le cours en BROUILLON (retire du catalogue)
                    DAOFactory.getCoursDAO().updateStatut(coursId, StatutCours.BROUILLON);
                    resp.sendRedirect(req.getContextPath() + "/instructor/dashboard?succes="
                            + enc("Cours retiré du catalogue avec succès"));
                }

                case "supprimer" -> {
                    // Suppression définitive
                    DAOFactory.getCoursDAO().delete(coursId);
                    resp.sendRedirect(req.getContextPath() + "/instructor/dashboard?succes="
                            + enc("Cours supprimé définitivement"));
                }

                default -> resp.sendRedirect(req.getContextPath() + "/instructor/dashboard");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/instructor/dashboard?erreur="
                    + enc("Erreur technique"));
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}