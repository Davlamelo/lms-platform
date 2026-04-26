package com.formation.lms.controllers.instructor;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.models.*;
import com.formation.lms.services.InstructeurService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/instructor/course/create")
public class CreateCourseServlet extends HttpServlet {

    private InstructeurService instructeurService;

    @Override
    public void init() throws ServletException {
        this.instructeurService = new InstructeurService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Categorie> categories = DAOFactory.getCategorieDAO().findAll();
            req.setAttribute("categories", categories);
        } catch (Exception e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("/WEB-INF/views/instructor/create-course.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");

        String titre = req.getParameter("titre");
        String descriptionCourte = req.getParameter("descriptionCourte");
        String descriptionLongue = req.getParameter("descriptionLongue");
        String categorieIdStr = req.getParameter("categorieId");
        String niveauStr = req.getParameter("niveau");

        try {
            Long categorieId = Long.parseLong(categorieIdStr);
            NiveauCours niveau = NiveauCours.valueOf(niveauStr);

            Cours nouveauCours = instructeurService.creerCours(
                    titre, descriptionCourte, descriptionLongue,
                    categorieId, niveau, utilisateur.getId());

            // Rediriger vers l'éditeur de curriculum
            resp.sendRedirect(req.getContextPath() +
                    "/instructor/course/curriculum?coursId=" + nouveauCours.getId());

        } catch (IllegalArgumentException e) {
            req.setAttribute("erreur", e.getMessage());
            try {
                req.setAttribute("categories", DAOFactory.getCategorieDAO().findAll());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            req.setAttribute("titre", titre);
            req.setAttribute("descriptionCourte", descriptionCourte);
            req.getRequestDispatcher("/WEB-INF/views/instructor/create-course.jsp")
                    .forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erreur", "Erreur lors de la création du cours.");
            req.getRequestDispatcher("/WEB-INF/views/instructor/create-course.jsp")
                    .forward(req, resp);
        }
    }
}