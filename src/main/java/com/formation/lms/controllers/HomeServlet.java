package com.formation.lms.controllers;

import com.formation.lms.models.Categorie;
import com.formation.lms.models.Cours;
import com.formation.lms.services.CoursService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("")
public class HomeServlet extends HttpServlet {

    private CoursService coursService;

    @Override
    public void init() throws ServletException {
        this.coursService = new CoursService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            List<Cours> coursMisEnAvant = coursService.getCoursPubliesPagines(1, 6);
            List<Categorie> categories = coursService.getToutesCategories();

            req.setAttribute("coursMisEnAvant", coursMisEnAvant);
            req.setAttribute("categories", categories);

        } catch (Exception e) {
            req.setAttribute("erreur", "Erreur lors du chargement de la page.");
            e.printStackTrace();
        }

        req.getRequestDispatcher("/WEB-INF/views/public/home.jsp").forward(req, resp);
    }
}