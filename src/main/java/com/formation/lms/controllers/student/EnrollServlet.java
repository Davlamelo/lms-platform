package com.formation.lms.controllers.student;

import com.formation.lms.models.Utilisateur;
import com.formation.lms.services.InscriptionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/student/enroll")
public class EnrollServlet extends HttpServlet {

    private InscriptionService inscriptionService;

    @Override
    public void init() throws ServletException {
        this.inscriptionService = new InscriptionService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");
        String coursIdStr = req.getParameter("coursId");
        String slug = req.getParameter("slug");

        if (coursIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/catalog");
            return;
        }

        try {
            Long coursId = Long.parseLong(coursIdStr);
            inscriptionService.inscrireApprenant(utilisateur.getId(), coursId);

            // Rediriger vers le lecteur de cours
            resp.sendRedirect(req.getContextPath() + "/student/learn?coursId=" + coursId);

        } catch (IllegalStateException e) {
            // Déjà inscrit
            resp.sendRedirect(req.getContextPath() + "/course?slug=" + slug + "&erreur=deja_inscrit");

        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/course?slug=" + slug + "&erreur=technique");
            e.printStackTrace();
        }
    }
}