package com.formation.lms.services;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.*;
import com.formation.lms.models.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour le système de Questions & Réponses.
 */
public class QRService {

    private final FilDiscussionDAO filDAO;
    private final MessageDiscussionDAO messageDAO;
    private final InscriptionDAO inscriptionDAO;

    public QRService() {
        this.filDAO = DAOFactory.getFilDiscussionDAO();
        this.messageDAO = DAOFactory.getMessageDiscussionDAO();
        this.inscriptionDAO = DAOFactory.getInscriptionDAO();
    }

    // === Fils de discussion ===

    /**
     * Récupère tous les fils d'un cours.
     */
    public List<FilDiscussion> getFilsCours(Long coursId) throws SQLException {
        return filDAO.findByCoursId(coursId);
    }

    /**
     * Récupère un fil par son ID.
     */
    public Optional<FilDiscussion> getFil(Long filId) throws SQLException {
        return filDAO.findById(filId);
    }

    /**
     * Crée un nouveau fil de discussion.
     * Règle : l'auteur doit être inscrit au cours.
     */
    public FilDiscussion creerFil(Long coursId, Long auteurId,
                                  String titre, String contenu) throws SQLException {

        if (titre == null || titre.trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre est obligatoire.");
        }
        if (contenu == null || contenu.trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu est obligatoire.");
        }

        // Vérifier que l'auteur est inscrit au cours
        if (!inscriptionDAO.existeInscription(auteurId, coursId)) {
            throw new IllegalStateException(
                    "Vous devez être inscrit au cours pour poser une question.");
        }

        FilDiscussion fil = new FilDiscussion(coursId, auteurId,
                titre.trim(), contenu.trim());
        return filDAO.save(fil);
    }

    /**
     * Marque un fil comme résolu.
     */
    public boolean marquerResolu(Long filId, Long utilisateurId) throws SQLException {
        Optional<FilDiscussion> optFil = filDAO.findById(filId);
        if (optFil.isEmpty()) {
            throw new IllegalArgumentException("Fil introuvable.");
        }
        // Seul l'auteur peut marquer comme résolu
        if (!optFil.get().getAuteurId().equals(utilisateurId)) {
            throw new SecurityException("Seul l'auteur peut marquer ce fil comme résolu.");
        }
        return filDAO.marquerResolu(filId);
    }

    // === Messages ===

    /**
     * Récupère tous les messages d'un fil.
     */
    public List<MessageDiscussion> getMessages(Long filId) throws SQLException {
        return messageDAO.findByFilId(filId);
    }

    /**
     * Ajoute un message dans un fil.
     * Règle : l'auteur doit être inscrit au cours.
     */
    public MessageDiscussion ajouterMessage(Long filId, Long auteurId,
                                            String contenu,
                                            Long coursId) throws SQLException {

        if (contenu == null || contenu.trim().isEmpty()) {
            throw new IllegalArgumentException("Le message ne peut pas être vide.");
        }

        // Vérifier inscription (sauf si instructeur)
        if (!inscriptionDAO.existeInscription(auteurId, coursId)) {
            // Vérifier si c'est l'instructeur du cours
            com.formation.lms.models.Cours cours =
                    DAOFactory.getCoursDAO().findById(coursId).orElse(null);
            if (cours == null || !cours.getInstructeurId().equals(auteurId)) {
                throw new IllegalStateException(
                        "Vous devez être inscrit pour répondre.");
            }
        }

        MessageDiscussion message = new MessageDiscussion(filId, auteurId, contenu.trim());
        return messageDAO.save(message);
    }

    /**
     * Marque un message comme réponse officielle (instructeur uniquement).
     */
    public boolean marquerReponseOfficielle(Long messageId,
                                            Long instructeurId,
                                            Long coursId) throws SQLException {
        com.formation.lms.models.Cours cours =
                DAOFactory.getCoursDAO().findById(coursId).orElse(null);
        if (cours == null || !cours.getInstructeurId().equals(instructeurId)) {
            throw new SecurityException(
                    "Seul l'instructeur peut marquer une réponse officielle.");
        }
        return messageDAO.marquerReponseOfficielle(messageId);
    }
}