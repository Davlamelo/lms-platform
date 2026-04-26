package com.formation.lms.controllers.instructor;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.models.*;
import com.formation.lms.services.InstructeurService;
import com.formation.lms.utils.FileUploadUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.List;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024
)
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
        req.getRequestDispatcher("/WEB-INF/views/instructor/create-course.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession()
                .getAttribute("utilisateur");

        String titre = req.getParameter("titre");
        String descriptionCourte = req.getParameter("descriptionCourte");
        String descriptionLongue = req.getParameter("descriptionLongue");
        String categorieIdStr = req.getParameter("categorieId");
        String niveauStr = req.getParameter("niveau");

        try {
            Long categorieId = Long.parseLong(categorieIdStr);
            NiveauCours niveau = NiveauCours.valueOf(niveauStr);

            // 1. Créer le cours D'ABORD
            Cours nouveauCours = instructeurService.creerCours(
                    titre, descriptionCourte, descriptionLongue,
                    categorieId, niveau, utilisateur.getId());

            // 2. Gestion de la miniature APRÈS la création
            String cheminRacine = getServletContext().getRealPath("/");
            Part partMiniature = req.getPart("miniature");
            if (partMiniature != null && partMiniature.getSize() > 0) {
                try {
                    String cheminMiniature = FileUploadUtil.sauvegarderFichier(
                            partMiniature, cheminRacine, "uploads/miniatures/");
                    if (cheminMiniature != null) {
                        nouveauCours.setMiniatureUrl(cheminMiniature);
                        DAOFactory.getCoursDAO().update(nouveauCours);
                    }
                } catch (IllegalArgumentException e) {
                    // Miniature invalide → on continue sans miniature
                    // Le cours est déjà créé
                    System.out.println("Miniature ignorée : " + e.getMessage());
                }
            }

            // 3. Rediriger vers l'éditeur de curriculum
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
            try {
                req.setAttribute("categories", DAOFactory.getCategorieDAO().findAll());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            req.getRequestDispatcher("/WEB-INF/views/instructor/create-course.jsp")
                    .forward(req, resp);
        }
    }
}