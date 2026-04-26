package com.formation.lms.controllers.student;

import com.formation.lms.models.Utilisateur;
import com.formation.lms.services.InscriptionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Marque une leçon comme complétée et recalcule la progression.
 * Appelé en POST depuis le lecteur de cours.
 */
@WebServlet("/student/complete-lesson")
public class CompleteLessonServlet extends HttpServlet {

    private InscriptionService inscriptionService;

    @Override
    public void init() throws ServletException {
        this.inscriptionService = new InscriptionService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");

        String inscriptionIdStr = req.getParameter("inscriptionId");
        String leconIdStr = req.getParameter("leconId");
        String coursIdStr = req.getParameter("coursId");
        String prochainLeconIdStr = req.getParameter("prochainLeconId");

        try {
            Long inscriptionId = Long.parseLong(inscriptionIdStr);
            Long leconId = Long.parseLong(leconIdStr);
            Long coursId = Long.parseLong(coursIdStr);

            // Marquer la leçon complétée + recalculer progression
            inscriptionService.completerLecon(inscriptionId, leconId, coursId);

            // Rediriger vers la prochaine leçon ou rester sur la même
            String redirection = req.getContextPath() + "/student/learn?coursId=" + coursId;
            if (prochainLeconIdStr != null && !prochainLeconIdStr.isEmpty()) {
                redirection += "&leconId=" + prochainLeconIdStr;
            } else {
                redirection += "&leconId=" + leconId;
            }

            resp.sendRedirect(redirection);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/student/learn?coursId=" + coursIdStr);
        }
    }
}