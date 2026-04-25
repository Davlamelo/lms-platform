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
import java.util.Optional;

@WebServlet("/catalog")
public class CatalogServlet extends HttpServlet {

    private CoursService coursService;

    @Override
    public void init() throws ServletException {
        this.coursService = new CoursService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Récupérer les paramètres de filtre
            String categorieSlug = req.getParameter("categorie");
            String recherche = req.getParameter("q");

            List<Cours> cours;
            Categorie categorieActive = null;

            if (recherche != null && !recherche.trim().isEmpty()) {
                // Recherche par mot-clé
                cours = coursService.rechercherCours(recherche);
                req.setAttribute("recherche", recherche);

            } else if (categorieSlug != null && !categorieSlug.isEmpty()) {
                // Filtrage par catégorie
                Optional<Categorie> optCat = coursService.getCategorieParSlug(categorieSlug);
                if (optCat.isPresent()) {
                    categorieActive = optCat.get();
                    cours = coursService.getCoursParCategorie(categorieActive.getId());
                } else {
                    cours = coursService.getCoursPublies();
                }

            } else {
                // Tous les cours publiés
                cours = coursService.getCoursPublies();
            }

            List<Categorie> categories = coursService.getToutesCategories();

            req.setAttribute("cours", cours);
            req.setAttribute("categories", categories);
            req.setAttribute("categorieActive", categorieActive);

        } catch (Exception e) {
            req.setAttribute("erreur", "Erreur lors du chargement du catalogue.");
            e.printStackTrace();
        }

        req.getRequestDispatcher("/WEB-INF/views/public/catalog.jsp").forward(req, resp);
    }
}