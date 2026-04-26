package com.formation.lms.controllers.student;

import com.formation.lms.models.Cours;
import com.formation.lms.models.Inscription;
import com.formation.lms.models.Utilisateur;
import com.formation.lms.services.CoursService;
import com.formation.lms.services.InscriptionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends HttpServlet {

    private InscriptionService inscriptionService;
    private CoursService coursService;

    @Override
    public void init() throws ServletException {
        this.inscriptionService = new InscriptionService();
        this.coursService = new CoursService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");

        try {
            List<Inscription> inscriptions = inscriptionService
                    .getInscriptionsApprenant(utilisateur.getId());

            // Map inscription → cours correspondant
            Map<Inscription, Cours> inscriptionsAvecCours = new LinkedHashMap<>();
            for (Inscription insc : inscriptions) {
                Optional<Cours> cours = coursService.getCoursById(insc.getCoursId());
                cours.ifPresent(c -> inscriptionsAvecCours.put(insc, c));
            }

            req.setAttribute("inscriptions", inscriptions);
            req.setAttribute("inscriptionsAvecCours", inscriptionsAvecCours);

        } catch (Exception e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/WEB-INF/views/student/dashboard.jsp").forward(req, resp);
    }
}