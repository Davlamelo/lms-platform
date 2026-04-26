package com.formation.lms.controllers.admin;

import com.formation.lms.models.Categorie;
import com.formation.lms.services.AdminService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/categories")
public class ManageCategoriesServlet extends HttpServlet {

    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        this.adminService = new AdminService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Categorie> categories = adminService.getToutesCategories();
            req.setAttribute("categories", categories);
        } catch (Exception e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/categories.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try {
            switch (action) {
                case "creer" -> {
                    String nom = req.getParameter("nom");
                    String description = req.getParameter("description");
                    String icone = req.getParameter("icone");
                    adminService.creerCategorie(nom, description, icone);
                    resp.sendRedirect(req.getContextPath() +
                            "/admin/categories?succes=Catégorie+créée");
                }
                case "supprimer" -> {
                    Long categorieId = Long.parseLong(req.getParameter("categorieId"));
                    adminService.supprimerCategorie(categorieId);
                    resp.sendRedirect(req.getContextPath() +
                            "/admin/categories?succes=Catégorie+supprimée");
                }
            }

        } catch (IllegalStateException | IllegalArgumentException e) {
            req.setAttribute("erreur", e.getMessage());
            try {
                req.setAttribute("categories", adminService.getToutesCategories());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            req.getRequestDispatcher("/WEB-INF/views/admin/categories.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        }
    }
}