package com.formation.lms.controllers.auth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet de déconnexion (/logout).
 * Détruit la session et redirige vers la page de connexion.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // Invalider la session (supprime toutes les données)
        HttpSession session = req.getSession(false); // false = ne pas créer si n'existe pas
        if (session != null) {
            session.invalidate();
        }

        // Rediriger vers login
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}