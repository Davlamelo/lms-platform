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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 20 * 1024 * 1024,
        maxRequestSize = 25 * 1024 * 1024
)
@WebServlet("/instructor/course/curriculum")
public class CurriculumServlet extends HttpServlet {

    private InstructeurService instructeurService;

    @Override
    public void init() throws ServletException {
        this.instructeurService = new InstructeurService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession()
                .getAttribute("utilisateur");
        String coursIdStr = req.getParameter("coursId");

        if (coursIdStr == null || coursIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/instructor/dashboard");
            return;
        }

        try {
            Long coursId = Long.parseLong(coursIdStr);

            Optional<Cours> optCours = DAOFactory.getCoursDAO().findById(coursId);
            if (optCours.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Cours cours = optCours.get();

            if (!cours.getInstructeurId().equals(utilisateur.getId())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            List<Section> sections = instructeurService.getSections(coursId);
            Map<Long, List<Lecon>> leconsParSection = new LinkedHashMap<>();
            for (Section section : sections) {
                leconsParSection.put(section.getId(),
                        instructeurService.getLecons(section.getId()));
            }

            req.setAttribute("cours", cours);
            req.setAttribute("sections", sections);
            req.setAttribute("leconsParSection", leconsParSection);

            if (req.getParameter("succes") != null) {
                req.setAttribute("succes", req.getParameter("succes"));
            }
            if (req.getParameter("erreur") != null) {
                req.setAttribute("erreur", req.getParameter("erreur"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erreur", "Erreur lors du chargement.");
        }

        req.getRequestDispatcher("/WEB-INF/views/instructor/curriculum.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession()
                .getAttribute("utilisateur");
        String action = req.getParameter("action");
        String coursIdStr = req.getParameter("coursId");

        // Sécurité : coursId obligatoire
        if (coursIdStr == null || coursIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/instructor/dashboard");
            return;
        }

        Long coursId = Long.parseLong(coursIdStr);

        try {
            switch (action != null ? action : "") {

                case "ajouterSection" -> {
                    String titreSection = req.getParameter("titreSection");
                    if (titreSection == null || titreSection.trim().isEmpty()) {
                        resp.sendRedirect(req.getContextPath() +
                                "/instructor/course/curriculum?coursId=" + coursId +
                                "&erreur=Le+titre+de+la+section+est+obligatoire");
                        return;
                    }
                    instructeurService.ajouterSection(
                            coursId, titreSection, utilisateur.getId());
                    resp.sendRedirect(req.getContextPath() +
                            "/instructor/course/curriculum?coursId=" + coursId +
                            "&succes=Section+ajoutée+avec+succès");
                }

                case "ajouterLecon" -> {
                    Long sectionId = Long.parseLong(req.getParameter("sectionId"));
                    String titreLecon = req.getParameter("titreLecon");
                    String typeStr = req.getParameter("typeLecon");
                    String dureeStr = req.getParameter("dureeMin");
                    int duree = (dureeStr != null && !dureeStr.isEmpty())
                            ? Integer.parseInt(dureeStr) : 15;
                    TypeLecon type = TypeLecon.valueOf(typeStr);
                    String contenu = req.getParameter("contenu");
                    String cheminRacine = getServletContext().getRealPath("/");

                    // Si RESSOURCE → tenter upload fichier
                    if (type == TypeLecon.RESSOURCE) {
                        try {
                            Part partFichier = req.getPart("fichierRessource");
                            if (partFichier != null && partFichier.getSize() > 0) {
                                String nomOriginal = FileUploadUtil
                                        .extraireNomFichier(partFichier);
                                String extension = FileUploadUtil.extraireExtension(
                                                nomOriginal != null ? nomOriginal : "")
                                        .toLowerCase();
                                String[] extsAutorisees = {
                                        ".pdf", ".zip", ".docx", ".xlsx", ".pptx"};
                                if (FileUploadUtil.estExtensionAutorisee(
                                        extension, extsAutorisees)) {
                                    String cheminFichier = FileUploadUtil
                                            .sauvegarderFichier(partFichier,
                                                    cheminRacine, "uploads/ressources/");
                                    if (cheminFichier != null) {
                                        contenu = cheminFichier;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Upload ignoré : " + e.getMessage());
                        }
                    }

                    instructeurService.ajouterLecon(
                            sectionId, titreLecon, type, contenu, duree);
                    resp.sendRedirect(req.getContextPath() +
                            "/instructor/course/curriculum?coursId=" + coursId +
                            "&succes=Leçon+ajoutée+avec+succès");
                }

                case "modifierLecon" -> {
                    Long leconId = Long.parseLong(req.getParameter("leconId"));
                    String titreLecon = req.getParameter("titreLecon");
                    String typeStr = req.getParameter("typeLecon");
                    String dureeStr = req.getParameter("dureeMin");
                    int duree = (dureeStr != null && !dureeStr.isEmpty())
                            ? Integer.parseInt(dureeStr) : 15;
                    boolean estGratuite = "on".equals(
                            req.getParameter("estGratuite"));
                    String contenu = req.getParameter("contenu");

                    Lecon lecon = DAOFactory.getLeconDAO().findById(leconId)
                            .orElseThrow(() -> new Exception("Leçon introuvable"));

                    lecon.setTitre(titreLecon != null ? titreLecon.trim() : "");
                    lecon.setTypeLecon(TypeLecon.valueOf(typeStr));
                    lecon.setDureeMin(duree);
                    lecon.setEstGratuite(estGratuite);

                    // Réinitialiser tous les champs de contenu
                    lecon.setContenuTexte(null);
                    lecon.setVideoUrl(null);
                    lecon.setRessourceUrl(null);

                    // Mettre le bon champ selon le type
                    switch (typeStr) {
                        case "VIDEO" -> lecon.setVideoUrl(
                                contenu != null ? contenu.trim() : null);
                        case "RESSOURCE" -> lecon.setRessourceUrl(
                                contenu != null ? contenu.trim() : null);
                        default -> lecon.setContenuTexte(
                                contenu != null ? contenu.trim() : null);
                    }

                    DAOFactory.getLeconDAO().update(lecon);
                    resp.sendRedirect(req.getContextPath() +
                            "/instructor/course/curriculum?coursId=" + coursId +
                            "&succes=Leçon+modifiée+avec+succès");
                }

                case "supprimerSection" -> {
                    Long sectionId = Long.parseLong(req.getParameter("sectionId"));
                    instructeurService.supprimerSection(sectionId);
                    resp.sendRedirect(req.getContextPath() +
                            "/instructor/course/curriculum?coursId=" + coursId +
                            "&succes=Section+supprimée");
                }

                case "supprimerLecon" -> {
                    Long leconId = Long.parseLong(req.getParameter("leconId"));
                    instructeurService.supprimerLecon(leconId);
                    resp.sendRedirect(req.getContextPath() +
                            "/instructor/course/curriculum?coursId=" + coursId +
                            "&succes=Leçon+supprimée");
                }

                case "soumettre" -> {
                    instructeurService.soumettrePourValidation(
                            coursId, utilisateur.getId());
                    resp.sendRedirect(req.getContextPath() +
                            "/instructor/dashboard?succes=Cours+soumis+pour+validation");
                }

                default -> resp.sendRedirect(req.getContextPath() +
                        "/instructor/course/curriculum?coursId=" + coursId);
            }

        } catch (IllegalStateException e) {
            resp.sendRedirect(req.getContextPath() +
                    "/instructor/course/curriculum?coursId=" + coursId +
                    "&erreur=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() +
                    "/instructor/course/curriculum?coursId=" + coursId +
                    "&erreur=Erreur+technique");
        }
    }
}