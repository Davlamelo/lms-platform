package com.formation.lms.controllers;

import com.formation.lms.models.Utilisateur;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet de redirection vers le bon dashboard selon le rôle.
 * URL : /dashboard
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");

        if (utilisateur == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String redirection = switch (utilisateur.getRole()) {
            case ADMIN -> "/admin/dashboard";
            case INSTRUCTEUR -> "/instructor/dashboard";
            case APPRENANT -> "/student/dashboard";
        };

        resp.sendRedirect(req.getContextPath() + redirection);
    }
}