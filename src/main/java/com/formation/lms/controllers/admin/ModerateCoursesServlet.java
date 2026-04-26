package com.formation.lms.controllers.admin;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.models.Cours;
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
import java.util.Optional;

@WebServlet("/admin/courses")
public class ModerateCoursesServlet extends HttpServlet {

    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        this.adminService = new AdminService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Cours> tousCours = adminService.getTousCours();

            // Enrichir avec le nom de l'instructeur
            Map<Long, String> nomsInstructeurs = new HashMap<>();
            for (Cours c : tousCours) {
                Optional<Utilisateur> inst = DAOFactory.getUtilisateurDAO()
                        .findById(c.getInstructeurId());
                inst.ifPresent(u -> nomsInstructeurs.put(c.getId(), u.getNomComplet()));
            }

            req.setAttribute("tousCours", tousCours);
            req.setAttribute("nomsInstructeurs", nomsInstructeurs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/WEB-INF/views/admin/courses.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        String coursIdStr = req.getParameter("coursId");

        try {
            Long coursId = Long.parseLong(coursIdStr);

            switch (action) {
                case "valider" -> adminService.validerCours(coursId);
                case "rejeter" -> adminService.rejeterCours(coursId);
                case "archiver" -> adminService.archiverCours(coursId);
            }

            resp.sendRedirect(req.getContextPath() +
                    "/admin/courses?succes=Action+effectuée+avec+succès");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/courses?erreur=Erreur+technique");
        }
    }
}