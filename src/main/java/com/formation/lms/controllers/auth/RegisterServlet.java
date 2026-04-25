package com.formation.lms.controllers.auth;

import com.formation.lms.models.Utilisateur;
import com.formation.lms.services.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet d'inscription (/register).
 * GET  → affiche le formulaire d'inscription
 * POST → traite la soumission du formulaire
 *
 * PATTERN RÉCURRENT : structure doGet/doPost identique pour tous les Servlets.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() throws ServletException {
        this.authService = new AuthService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Si l'utilisateur est déjà connecté, le rediriger vers le dashboard
        if (req.getSession().getAttribute("utilisateur") != null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        // Afficher le formulaire d'inscription
        req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. Récupérer les données du formulaire
        String prenom = req.getParameter("prenom");
        String nom = req.getParameter("nom");
        String email = req.getParameter("email");
        String motDePasse = req.getParameter("motDePasse");
        String confirmationMdp = req.getParameter("confirmationMdp");

        // 2. Vérifier la confirmation du mot de passe
        if (motDePasse == null || !motDePasse.equals(confirmationMdp)) {
            req.setAttribute("erreur", "Les mots de passe ne correspondent pas.");
            req.setAttribute("prenom", prenom);
            req.setAttribute("nom", nom);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
            return;
        }

        try {
            // 3. Appeler le service d'inscription
            Utilisateur utilisateur = authService.inscrire(prenom, nom, email, motDePasse);

            // 4. Succès → rediriger vers la page de connexion avec message
            resp.sendRedirect(req.getContextPath() + "/login?inscription=ok");

        } catch (IllegalArgumentException e) {
            // Erreur de validation → ré-afficher le formulaire avec l'erreur
            req.setAttribute("erreur", e.getMessage());
            req.setAttribute("prenom", prenom);
            req.setAttribute("nom", nom);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);

        } catch (Exception e) {
            // Erreur technique → ré-afficher avec message générique
            req.setAttribute("erreur", "Une erreur est survenue. Veuillez réessayer.");
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
        }
    }
}