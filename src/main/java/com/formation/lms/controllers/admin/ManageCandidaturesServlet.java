package com.formation.lms.controllers.admin;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.models.CandidatureInstructeur;
import com.formation.lms.models.Role;
import com.formation.lms.models.StatutCandidature;
import com.formation.lms.models.Utilisateur;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/candidatures")
public class ManageCandidaturesServlet extends HttpServlet {

    private static final String SEPARATEUR = "|";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            String filtreStatut = req.getParameter("statut");

            List<CandidatureInstructeur> candidatures;
            if (filtreStatut != null && !filtreStatut.isEmpty()) {
                candidatures = DAOFactory.getCandidatureInstructeurDAO()
                        .findByStatut(StatutCandidature.valueOf(filtreStatut));
            } else {
                candidatures = DAOFactory.getCandidatureInstructeurDAO().findAll();
            }

            // AJOUTÉ : normaliser les anciennes données (\n → |) pour chaque candidature
            for (CandidatureInstructeur c : candidatures) {
                if (c.getCvUrl() != null) {
                    c.setCvUrl(normaliserSeparateurs(c.getCvUrl()));
                }
            }

            Map<Long, Utilisateur> candidats = new HashMap<>();
            for (CandidatureInstructeur c : candidatures) {
                if (!candidats.containsKey(c.getUtilisateurId())) {
                    DAOFactory.getUtilisateurDAO()
                            .findById(c.getUtilisateurId())
                            .ifPresent(u -> candidats.put(u.getId(), u));
                }
            }

            long nbEnAttente = DAOFactory.getCandidatureInstructeurDAO()
                    .findByStatut(StatutCandidature.EN_ATTENTE).size();

            req.setAttribute("candidatures", candidatures);
            req.setAttribute("candidats", candidats);
            req.setAttribute("nbEnAttente", nbEnAttente);
            req.setAttribute("filtreStatut", filtreStatut);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erreur", "Erreur lors du chargement des candidatures.");
        }

        req.getRequestDispatcher("/WEB-INF/views/admin/candidatures.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action           = req.getParameter("action");
        String candidatureIdStr = req.getParameter("candidatureId");
        String commentaire      = req.getParameter("commentaire");

        if (candidatureIdStr == null || candidatureIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/candidatures");
            return;
        }

        Long candidatureId = Long.parseLong(candidatureIdStr);

        try {
            CandidatureInstructeur candidature = DAOFactory
                    .getCandidatureInstructeurDAO()
                    .findById(candidatureId)
                    .orElseThrow(() -> new Exception("Candidature introuvable"));

            switch (action != null ? action : "") {

                case "approuver" -> {
                    DAOFactory.getCandidatureInstructeurDAO().updateStatut(
                            candidatureId,
                            StatutCandidature.APPROUVEE,
                            commentaire != null ? commentaire.trim() : null
                    );

                    Utilisateur utilisateur = DAOFactory.getUtilisateurDAO()
                            .findById(candidature.getUtilisateurId())
                            .orElseThrow(() -> new Exception("Utilisateur introuvable"));

                    utilisateur.setRole(Role.INSTRUCTEUR);
                    DAOFactory.getUtilisateurDAO().update(utilisateur);

                    resp.sendRedirect(req.getContextPath() + "/admin/candidatures?succes="
                            + enc("Candidature approuvée. L'utilisateur est maintenant instructeur."));
                }

                case "rejeter" -> {
                    DAOFactory.getCandidatureInstructeurDAO().updateStatut(
                            candidatureId,
                            StatutCandidature.REJETEE,
                            commentaire != null ? commentaire.trim() : null
                    );
                    resp.sendRedirect(req.getContextPath() + "/admin/candidatures?succes="
                            + enc("Candidature refusée."));
                }

                case "retrograder" -> {
                    Utilisateur utilisateur = DAOFactory.getUtilisateurDAO()
                            .findById(candidature.getUtilisateurId())
                            .orElseThrow(() -> new Exception("Utilisateur introuvable"));

                    utilisateur.setRole(Role.APPRENANT);
                    DAOFactory.getUtilisateurDAO().update(utilisateur);

                    DAOFactory.getCandidatureInstructeurDAO().updateStatut(
                            candidatureId,
                            StatutCandidature.REJETEE,
                            "Rétrogradé en apprenant par l'administrateur."
                    );

                    resp.sendRedirect(req.getContextPath() + "/admin/candidatures?succes="
                            + enc("Instructeur rétrogradé en apprenant avec succès."));
                }

                default -> resp.sendRedirect(req.getContextPath() + "/admin/candidatures");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/candidatures?erreur="
                    + enc("Erreur technique."));
        }
    }

    /**
     * Normalise les anciennes données stockées avec \n ou \r\n
     * vers le nouveau format avec | comme séparateur.
     */
    private String normaliserSeparateurs(String cvUrl) {
        if (cvUrl == null) return null;
        return cvUrl
                .replace("\r\n", SEPARATEUR)
                .replace("\r", SEPARATEUR)
                .replace("\n", SEPARATEUR);
    }

    private String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}