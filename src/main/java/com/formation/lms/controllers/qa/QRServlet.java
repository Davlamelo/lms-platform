package com.formation.lms.controllers.qa;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.models.*;
import com.formation.lms.services.CoursService;
import com.formation.lms.services.InscriptionService;
import com.formation.lms.services.QRService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

/**
 * Affiche la liste des fils de discussion d'un cours.
 * GET /qa?coursId=X
 */
@WebServlet("/qa")
public class QRServlet extends HttpServlet {

    private QRService qrService;
    private CoursService coursService;
    private InscriptionService inscriptionService;

    @Override
    public void init() throws ServletException {
        this.qrService = new QRService();
        this.coursService = new CoursService();
        this.inscriptionService = new InscriptionService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String coursIdStr = req.getParameter("coursId");
        if (coursIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/catalog");
            return;
        }

        try {
            Long coursId = Long.parseLong(coursIdStr);

            // Récupérer le cours
            Cours cours = coursService.getCoursById(coursId).orElse(null);
            if (cours == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Récupérer les fils de discussion
            List<FilDiscussion> fils = qrService.getFilsCours(coursId);

            // Enrichir avec le nombre de messages par fil
            Map<Long, Integer> nbMessagesParFil = new HashMap<>();
            Map<Long, String> nomsAuteurs = new HashMap<>();

            for (FilDiscussion fil : fils) {
                nbMessagesParFil.put(fil.getId(),
                        qrService.getMessages(fil.getId()).size());

                // Nom de l'auteur
                DAOFactory.getUtilisateurDAO().findById(fil.getAuteurId())
                        .ifPresent(u -> nomsAuteurs.put(fil.getId(), u.getNomComplet()));
            }

            // Vérifier si l'utilisateur est inscrit
            boolean estInscrit = false;
            boolean estInstructeur = false;
            Utilisateur utilisateur = (Utilisateur) req.getSession()
                    .getAttribute("utilisateur");

            if (utilisateur != null) {
                estInscrit = inscriptionService.estInscrit(utilisateur.getId(), coursId);
                estInstructeur = utilisateur.getRole() == Role.INSTRUCTEUR
                        && utilisateur.getId().equals(cours.getInstructeurId());
            }

            req.setAttribute("cours", cours);
            req.setAttribute("fils", fils);
            req.setAttribute("nbMessagesParFil", nbMessagesParFil);
            req.setAttribute("nomsAuteurs", nomsAuteurs);
            req.setAttribute("estInscrit", estInscrit);
            req.setAttribute("estInstructeur", estInstructeur);

            // Message succès
            if (req.getParameter("succes") != null) {
                req.setAttribute("succes", req.getParameter("succes"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/WEB-INF/views/qa/liste.jsp").forward(req, resp);
    }
}