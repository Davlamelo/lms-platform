package com.formation.lms.controllers.qa;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.models.*;
import com.formation.lms.services.QRService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

/**
 * Affiche un fil de discussion et gère les actions :
 * GET  /qa/thread?filId=X         → affiche le fil + messages
 * POST /qa/thread (action=creer)  → crée un nouveau fil
 * POST /qa/thread (action=repondre) → ajoute un message
 * POST /qa/thread (action=resoudre) → marque comme résolu
 * POST /qa/thread (action=officiel) → marque réponse officielle
 */
@WebServlet("/qa/thread")
public class ThreadServlet extends HttpServlet {

    private QRService qrService;

    @Override
    public void init() throws ServletException {
        this.qrService = new QRService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String filIdStr = req.getParameter("filId");
        if (filIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/catalog");
            return;
        }

        try {
            Long filId = Long.parseLong(filIdStr);

            // Récupérer le fil
            FilDiscussion fil = qrService.getFil(filId).orElse(null);
            if (fil == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Récupérer les messages
            List<MessageDiscussion> messages = qrService.getMessages(filId);

            // Enrichir avec les noms des auteurs
            Map<Long, String> nomsAuteurs = new HashMap<>();

            // Auteur du fil
            DAOFactory.getUtilisateurDAO().findById(fil.getAuteurId())
                    .ifPresent(u -> nomsAuteurs.put(fil.getAuteurId(), u.getNomComplet()));

            // Auteurs des messages
            for (MessageDiscussion msg : messages) {
                if (!nomsAuteurs.containsKey(msg.getAuteurId())) {
                    DAOFactory.getUtilisateurDAO().findById(msg.getAuteurId())
                            .ifPresent(u -> nomsAuteurs.put(msg.getAuteurId(),
                                    u.getNomComplet()));
                }
            }

            // Récupérer le cours
            com.formation.lms.models.Cours cours = DAOFactory.getCoursDAO()
                    .findById(fil.getCoursId()).orElse(null);

            // Vérifier le rôle de l'utilisateur
            Utilisateur utilisateur = (Utilisateur) req.getSession()
                    .getAttribute("utilisateur");
            boolean estInstructeur = utilisateur != null && cours != null
                    && utilisateur.getRole() == Role.INSTRUCTEUR
                    && utilisateur.getId().equals(cours.getInstructeurId());
            boolean estAuteurFil = utilisateur != null
                    && utilisateur.getId().equals(fil.getAuteurId());

            req.setAttribute("fil", fil);
            req.setAttribute("messages", messages);
            req.setAttribute("nomsAuteurs", nomsAuteurs);
            req.setAttribute("cours", cours);
            req.setAttribute("estInstructeur", estInstructeur);
            req.setAttribute("estAuteurFil", estAuteurFil);

            if (req.getParameter("succes") != null) {
                req.setAttribute("succes", req.getParameter("succes"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/WEB-INF/views/qa/thread.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession()
                .getAttribute("utilisateur");
        String action = req.getParameter("action");
        String coursIdStr = req.getParameter("coursId");

        try {
            switch (action) {

                case "creer" -> {
                    Long coursId = Long.parseLong(coursIdStr);
                    String titre = req.getParameter("titre");
                    String contenu = req.getParameter("contenu");
                    FilDiscussion fil = qrService.creerFil(
                            coursId, utilisateur.getId(), titre, contenu);
                    resp.sendRedirect(req.getContextPath() +
                            "/qa/thread?filId=" + fil.getId() +
                            "&succes=Question+posée+avec+succès");
                }

                case "repondre" -> {
                    Long filId = Long.parseLong(req.getParameter("filId"));
                    Long coursId = Long.parseLong(coursIdStr);
                    String contenu = req.getParameter("contenu");
                    qrService.ajouterMessage(filId, utilisateur.getId(),
                            contenu, coursId);
                    resp.sendRedirect(req.getContextPath() +
                            "/qa/thread?filId=" + filId +
                            "&succes=Réponse+ajoutée");
                }

                case "resoudre" -> {
                    Long filId = Long.parseLong(req.getParameter("filId"));
                    qrService.marquerResolu(filId, utilisateur.getId());
                    resp.sendRedirect(req.getContextPath() +
                            "/qa/thread?filId=" + filId +
                            "&succes=Fil+marqué+comme+résolu");
                }

                case "officiel" -> {
                    Long messageId = Long.parseLong(req.getParameter("messageId"));
                    Long filId = Long.parseLong(req.getParameter("filId"));
                    Long coursId = Long.parseLong(coursIdStr);
                    qrService.marquerReponseOfficielle(
                            messageId, utilisateur.getId(), coursId);
                    resp.sendRedirect(req.getContextPath() +
                            "/qa/thread?filId=" + filId +
                            "&succes=Réponse+officielle+marquée");
                }

                default -> resp.sendRedirect(req.getContextPath() + "/catalog");
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            resp.sendRedirect(req.getContextPath() +
                    "/qa?coursId=" + coursIdStr + "&erreur=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/catalog");
        }
    }
}