package com.formation.lms.filters;

import com.formation.lms.models.Utilisateur;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filtre d'authentification.
 * Protège les routes /student/*, /instructor/*, /admin/* et /dashboard.
 * Redirige vers /login si l'utilisateur n'est pas connecté.
 * Vérifie que l'utilisateur a le bon rôle pour accéder à la section.
 *
 * PATTERN RÉCURRENT : tout projet web a un filtre d'auth similaire.
 */
@WebFilter(urlPatterns = {"/student/*", "/instructor/*", "/admin/*", "/dashboard"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 1. Vérifier si l'utilisateur est connecté
        HttpSession session = req.getSession(false);
        Utilisateur utilisateur = null;

        if (session != null) {
            utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        }

        if (utilisateur == null) {
            // Non connecté → rediriger vers login
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 2. Vérifier les droits d'accès selon l'URL
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String chemin = uri.substring(contextPath.length());

        boolean autorise = verifierAcces(chemin, utilisateur);

        if (!autorise) {
            // Pas le bon rôle → erreur 403 (interdit)
            resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Vous n'avez pas les droits pour accéder à cette page.");
            return;
        }

        // 3. Tout est OK → laisser passer la requête au Servlet
        chain.doFilter(request, response);
    }

    /**
     * Vérifie si un utilisateur a le droit d'accéder à un chemin donné.
     */
    private boolean verifierAcces(String chemin, Utilisateur utilisateur) {
        String role = utilisateur.getRole().name();

        if (chemin.startsWith("/admin/")) {
            return "ADMIN".equals(role);
        }
        if (chemin.startsWith("/instructor/")) {
            return "INSTRUCTEUR".equals(role) || "ADMIN".equals(role);
        }
        if (chemin.startsWith("/student/")) {
            return "APPRENANT".equals(role);
        }
        if (chemin.equals("/dashboard")) {
            return true; // Tous les connectés peuvent accéder au dashboard
        }

        return true;
    }
}