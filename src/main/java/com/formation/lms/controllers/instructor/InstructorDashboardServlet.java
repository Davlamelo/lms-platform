package com.formation.lms.controllers.instructor;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.models.Cours;
import com.formation.lms.models.Utilisateur;
import com.formation.lms.services.InstructeurService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}