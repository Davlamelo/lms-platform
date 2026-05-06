package com.formation.lms.controllers;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.UtilisateurDAO;
import com.formation.lms.models.CandidatureInstructeur;
import com.formation.lms.models.Utilisateur;
import com.formation.lms.utils.FileUploadUtil;
import com.formation.lms.utils.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.List;

/**
 * Gère la consultation et modification du profil utilisateur.
 * GET  /profile → affiche le profil
 * POST /profile → met à jour le profil
 *
 * @MultipartConfig : obligatoire pour recevoir des fichiers uploadés
 */
@WebServlet("/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1 Mo en mémoire avant d'écrire sur disque
        maxFileSize = 5 * 1024 * 1024,         // 5 Mo max par fichier
        maxRequestSize = 10 * 1024 * 1024      // 10 Mo max par requête
)
public class ProfileServlet extends HttpServlet {

    private UtilisateurDAO utilisateurDAO;

    @Override
    public void init() throws ServletException {
        this.utilisateurDAO = DAOFactory.getUtilisateurDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // L'utilisateur est déjà dans la session
        // On recharge depuis la BDD pour avoir les données fraîches
        Utilisateur utilisateur = (Utilisateur) req.getSession()
                .getAttribute("utilisateur");

        try {
            Utilisateur frais = utilisateurDAO.findById(utilisateur.getId())
                    .orElse(utilisateur);
            req.setAttribute("profil", frais);

            // AJOUTÉ : charger la dernière candidature pour les APPRENANTS
            // (utilisée dans la sidebar de profile.jsp)
            if (frais.getRole().name().equals("APPRENANT")) {
                List<CandidatureInstructeur> candidatures = DAOFactory
                        .getCandidatureInstructeurDAO()
                        .findByUtilisateurId(frais.getId());
                if (!candidatures.isEmpty()) {
                    req.setAttribute("derniereCandidature", candidatures.get(0));
                }
            }

            // Messages
            if (req.getParameter("succes") != null) {
                req.setAttribute("succes", req.getParameter("succes"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/WEB-INF/views/profile/edit.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession()
                .getAttribute("utilisateur");
        String action = req.getParameter("action");
        String cheminRacine = getServletContext().getRealPath("/");

        try {
            if ("modifierProfil".equals(action)) {
                modifierProfil(req, utilisateur, cheminRacine);
                // Recharger l'utilisateur en session
                utilisateurDAO.findById(utilisateur.getId())
                        .ifPresent(u -> req.getSession().setAttribute("utilisateur", u));
                resp.sendRedirect(req.getContextPath() +
                        "/profile?succes=Profil+mis+à+jour+avec+succès");

            } else if ("changerMotDePasse".equals(action)) {
                changerMotDePasse(req, utilisateur);
                resp.sendRedirect(req.getContextPath() +
                        "/profile?succes=Mot+de+passe+modifié+avec+succès");
            }

        } catch (IllegalArgumentException e) {
            req.setAttribute("erreur", e.getMessage());
            try {
                req.setAttribute("profil",
                        utilisateurDAO.findById(utilisateur.getId()).orElse(utilisateur));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            req.getRequestDispatcher("/WEB-INF/views/profile/edit.jsp")
                    .forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() +
                    "/profile?erreur=Erreur+technique");
        }
    }

    /**
     * Met à jour les informations du profil (prénom, nom, bio, avatar).
     */
    private void modifierProfil(HttpServletRequest req,
                                Utilisateur utilisateur,
                                String cheminRacine) throws Exception {

        String prenom = req.getParameter("prenom");
        String nom = req.getParameter("nom");
        String biographie = req.getParameter("biographie");

        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est obligatoire.");
        }
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire.");
        }

        utilisateur.setPrenom(prenom.trim());
        utilisateur.setNom(nom.trim());
        utilisateur.setBiographie(biographie != null ? biographie.trim() : "");

        // Gestion de l'avatar (upload image)
        Part partAvatar = req.getPart("avatar");
        if (partAvatar != null && partAvatar.getSize() > 0) {
            // Supprimer l'ancien avatar si existant
            if (utilisateur.getAvatarUrl() != null) {
                FileUploadUtil.supprimerFichier(cheminRacine, utilisateur.getAvatarUrl());
            }
            // Sauvegarder le nouvel avatar
            String cheminAvatar = FileUploadUtil.sauvegarderFichier(
                    partAvatar, cheminRacine, "uploads/avatars/");
            if (cheminAvatar != null) {
                utilisateur.setAvatarUrl(cheminAvatar);
            }
        }

        utilisateurDAO.update(utilisateur);
    }

    /**
     * Change le mot de passe de l'utilisateur.
     */
    private void changerMotDePasse(HttpServletRequest req,
                                   Utilisateur utilisateur) throws Exception {

        String ancienMdp = req.getParameter("ancienMotDePasse");
        String nouveauMdp = req.getParameter("nouveauMotDePasse");
        String confirmMdp = req.getParameter("confirmationMotDePasse");

        if (!PasswordUtil.verifier(ancienMdp, utilisateur.getMotDePasse())) {
            throw new IllegalArgumentException("L'ancien mot de passe est incorrect.");
        }
        if (nouveauMdp == null || nouveauMdp.length() < 6) {
            throw new IllegalArgumentException(
                    "Le nouveau mot de passe doit contenir au moins 6 caractères.");
        }
        if (!nouveauMdp.equals(confirmMdp)) {
            throw new IllegalArgumentException(
                    "Les mots de passe ne correspondent pas.");
        }

        utilisateur.setMotDePasse(PasswordUtil.hasher(nouveauMdp));
        utilisateurDAO.update(utilisateur);
    }
}