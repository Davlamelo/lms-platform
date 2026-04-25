package com.formation.lms.controllers.auth;

import com.formation.lms.models.Role;
import com.formation.lms.models.Utilisateur;
import com.formation.lms.services.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet de connexion (/login).
 * GET  → affiche le formulaire de connexion
 * POST → traite les identifiants
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() throws ServletException {
        this.authService = new AuthService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Si déjà connecté, rediriger
        if (req.getSession().getAttribute("utilisateur") != null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        // Vérifier si on vient de l'inscription (message de succès)
        String inscription = req.getParameter("inscription");
        if ("ok".equals(inscription)) {
            req.setAttribute("succes", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
        }

        req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String motDePasse = req.getParameter("motDePasse");

        try {
            // 1. Authentifier via le service
            Utilisateur utilisateur = authService.authentifier(email, motDePasse);

            // 2. Créer la session
            HttpSession session = req.getSession(true);
            session.setAttribute("utilisateur", utilisateur);
            session.setAttribute("role", utilisateur.getRole().name());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            // 3. Rediriger selon le rôle
            String redirection = switch (utilisateur.getRole()) {
                case ADMIN -> "/admin/dashboard";
                case INSTRUCTEUR -> "/instructor/dashboard";
                case APPRENANT -> "/student/dashboard";
            };

            resp.sendRedirect(req.getContextPath() + redirection);

        } catch (IllegalArgumentException e) {
            req.setAttribute("erreur", e.getMessage());
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("erreur", "Une erreur est survenue. Veuillez réessayer.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
        }
    }
}