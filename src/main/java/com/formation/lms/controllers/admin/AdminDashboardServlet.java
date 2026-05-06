package com.formation.lms.controllers.admin;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.models.Cours;
import com.formation.lms.models.CandidatureInstructeur;
import com.formation.lms.models.StatutCandidature;
import com.formation.lms.models.Utilisateur;
import com.formation.lms.services.AdminService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        this.adminService = new AdminService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Map<String, Object> stats = adminService.getStatsGlobales();
            List<Cours> coursEnAttente = adminService.getCoursEnAttente();

            // AJOUTÉ : candidatures instructeur en attente
            List<CandidatureInstructeur> candidaturesEnAttente = DAOFactory
                    .getCandidatureInstructeurDAO()
                    .findByStatut(StatutCandidature.EN_ATTENTE);

            // AJOUTÉ : infos des candidats pour affichage dans le dashboard
            Map<Long, Utilisateur> candidats = new HashMap<>();
            for (CandidatureInstructeur c : candidaturesEnAttente) {
                DAOFactory.getUtilisateurDAO()
                        .findById(c.getUtilisateurId())
                        .ifPresent(u -> candidats.put(u.getId(), u));
            }

            req.setAttribute("stats", stats);
            req.setAttribute("coursEnAttente", coursEnAttente);
            req.setAttribute("candidaturesEnAttente", candidaturesEnAttente);  // AJOUTÉ
            req.setAttribute("candidats", candidats);                           // AJOUTÉ

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erreur", "Erreur lors du chargement.");
        }

        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }
}
