package com.formation.lms.services;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.AvisDAO;
import com.formation.lms.dao.interfaces.InscriptionDAO;
import com.formation.lms.models.Avis;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des avis sur les cours.
 */
public class AvisService {

    private final AvisDAO avisDAO;
    private final InscriptionDAO inscriptionDAO;

    public AvisService() {
        this.avisDAO = DAOFactory.getAvisDAO();
        this.inscriptionDAO = DAOFactory.getInscriptionDAO();
    }

    /**
     * Soumet ou met à jour un avis.
     * Règles :
     * - L'apprenant doit être inscrit au cours
     * - Note entre 1 et 5
     * - Si avis existant → mise à jour, sinon → création
     */
    public Avis soumettreAvis(Long apprenantId, Long coursId,
                              int note, String commentaire) throws SQLException {

        // Vérifier que l'apprenant est inscrit
        if (!inscriptionDAO.existeInscription(apprenantId, coursId)) {
            throw new IllegalStateException(
                    "Vous devez être inscrit au cours pour laisser un avis.");
        }

        // Valider la note
        if (note < 1 || note > 5) {
            throw new IllegalArgumentException("La note doit être entre 1 et 5.");
        }

        // Vérifier si un avis existe déjà
        Optional<Avis> avisExistant = avisDAO.findByApprenantEtCours(apprenantId, coursId);

        if (avisExistant.isPresent()) {
            // Mettre à jour l'avis existant
            Avis avis = avisExistant.get();
            avis.setNote(note);
            avis.setCommentaire(commentaire);
            avisDAO.update(avis);
            return avis;
        } else {
            // Créer un nouvel avis
            Avis nouvelAvis = new Avis(apprenantId, coursId, note,
                    commentaire != null ? commentaire.trim() : "");
            return avisDAO.save(nouvelAvis);
        }
    }

    /**
     * Récupère l'avis d'un apprenant sur un cours.
     */
    public Optional<Avis> getAvisApprenant(Long apprenantId, Long coursId)
            throws SQLException {
        return avisDAO.findByApprenantEtCours(apprenantId, coursId);
    }

    /**
     * Récupère tous les avis d'un cours.
     */
    public List<Avis> getAvisCours(Long coursId) throws SQLException {
        return avisDAO.findByCoursId(coursId);
    }

    /**
     * Calcule la note moyenne d'un cours.
     */
    public double getNoteMoyenne(Long coursId) throws SQLException {
        return avisDAO.getNoteMoyenne(coursId);
    }

    /**
     * Supprime l'avis d'un apprenant sur un cours.
     */
    public boolean supprimerAvis(Long apprenantId, Long coursId) throws SQLException {
        Optional<Avis> avis = avisDAO.findByApprenantEtCours(apprenantId, coursId);
        if (avis.isPresent()) {
            return avisDAO.delete(avis.get().getId());
        }
        return false;
    }
}