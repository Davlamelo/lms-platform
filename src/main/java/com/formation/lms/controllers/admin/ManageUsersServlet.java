package com.formation.lms.controllers.admin;

import com.formation.lms.models.Utilisateur;
import com.formation.lms.services.AdminService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class ManageUsersServlet extends HttpServlet {

    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        this.adminService = new AdminService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Utilisateur> utilisateurs = adminService.getTousUtilisateurs();
            req.setAttribute("utilisateurs", utilisateurs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        String userIdStr = req.getParameter("userId");

        try {
            Long userId = Long.parseLong(userIdStr);

            switch (action) {
                case "toggleStatut" -> adminService.toggleStatutUtilisateur(userId);
                case "promouvoir" -> adminService.promouvoirInstructeur(userId);
            }

            resp.sendRedirect(req.getContextPath() +
                    "/admin/users?succes=Action+effectuée");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/users");
        }
    }
}