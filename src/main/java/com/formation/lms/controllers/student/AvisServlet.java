package com.formation.lms.controllers.student;

import com.formation.lms.models.Utilisateur;
import com.formation.lms.services.AvisService;
import com.formation.lms.services.CoursService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * Gère la soumission et suppression d'avis sur les cours.
 * POST /student/avis → soumettre/modifier un avis
 */
@WebServlet("/student/avis")
public class AvisServlet extends HttpServlet {

    private AvisService avisService;
    private CoursService coursService;

    @Override
    public void init() throws ServletException {
        this.avisService = new AvisService();
        this.coursService = new CoursService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");
        String action = req.getParameter("action");
        String coursIdStr = req.getParameter("coursId");
        String slug = req.getParameter("slug");

        try {
            Long coursId = Long.parseLong(coursIdStr);

            if ("supprimer".equals(action)) {
                avisService.supprimerAvis(utilisateur.getId(), coursId);
                resp.sendRedirect(req.getContextPath() +
                        "/course?slug=" + slug + "&succes=avis_supprime");
                return;
            }

            // Action "soumettre" (création ou modification)
            String noteStr = req.getParameter("note");
            String commentaire = req.getParameter("commentaire");

            if (noteStr == null || noteStr.isEmpty()) {
                resp.sendRedirect(req.getContextPath() +
                        "/course?slug=" + slug + "&erreur=Veuillez+sélectionner+une+note");
                return;
            }

            int note = Integer.parseInt(noteStr);
            avisService.soumettreAvis(utilisateur.getId(), coursId, note, commentaire);
            resp.sendRedirect(req.getContextPath() +
                    "/course?slug=" + slug + "&succes=avis_soumis");

        } catch (IllegalStateException | IllegalArgumentException e) {
            resp.sendRedirect(req.getContextPath() +
                    "/course?slug=" + slug + "&erreur=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() +
                    "/course?slug=" + slug + "&erreur=erreur_technique");
        }
    }
}