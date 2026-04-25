package com.formation.lms.services;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.InscriptionDAO;
import com.formation.lms.dao.interfaces.LeconDAO;
import com.formation.lms.dao.interfaces.ProgressionLeconDAO;
import com.formation.lms.models.Inscription;
import com.formation.lms.models.Lecon;
import com.formation.lms.models.ProgressionLecon;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour les inscriptions aux cours.
 */
public class InscriptionService {

    private final InscriptionDAO inscriptionDAO;
    private final LeconDAO leconDAO;
    private final ProgressionLeconDAO progressionDAO;

    public InscriptionService() {
        this.inscriptionDAO = DAOFactory.getInscriptionDAO();
        this.leconDAO = DAOFactory.getLeconDAO();
        this.progressionDAO = DAOFactory.getProgressionLeconDAO();
    }

    /**
     * Inscrit un apprenant à un cours.
     * Crée aussi les lignes de progression pour chaque leçon.
     *
     * @throws IllegalStateException si déjà inscrit
     */
    public Inscription inscrireApprenant(Long apprenantId, Long coursId) throws SQLException {

        // Vérifier si déjà inscrit
        if (inscriptionDAO.existeInscription(apprenantId, coursId)) {
            throw new IllegalStateException("Vous êtes déjà inscrit à ce cours.");
        }

        // Créer l'inscription
        Inscription inscription = new Inscription(apprenantId, coursId);
        inscription = inscriptionDAO.save(inscription);

        // Initialiser la progression pour chaque leçon du cours
        List<Lecon> lecons = leconDAO.findByCoursId(coursId);
        for (Lecon lecon : lecons) {
            ProgressionLecon progression = new ProgressionLecon(inscription.getId(), lecon.getId());
            progressionDAO.save(progression);
        }

        return inscription;
    }

    /**
     * Vérifie si un apprenant est inscrit à un cours.
     */
    public boolean estInscrit(Long apprenantId, Long coursId) throws SQLException {
        return inscriptionDAO.existeInscription(apprenantId, coursId);
    }

    /**
     * Récupère l'inscription d'un apprenant à un cours.
     */
    public Optional<Inscription> getInscription(Long apprenantId, Long coursId) throws SQLException {
        return inscriptionDAO.findByApprenantEtCours(apprenantId, coursId);
    }

    /**
     * Récupère toutes les inscriptions d'un apprenant.
     */
    public List<Inscription> getInscriptionsApprenant(Long apprenantId) throws SQLException {
        return inscriptionDAO.findByApprenantId(apprenantId);
    }

    /**
     * Marque une leçon comme complétée et recalcule la progression.
     */
    public void completerLecon(Long inscriptionId, Long leconId, Long coursId) throws SQLException {

        // Marquer la leçon complétée
        progressionDAO.marquerCompletee(inscriptionId, leconId);

        // Recalculer le pourcentage
        long totalLecons = leconDAO.countByCoursId(coursId);
        long leconsCompletees = progressionDAO.countCompletees(inscriptionId);

        double pourcentage = 0;
        if (totalLecons > 0) {
            pourcentage = (double) leconsCompletees / totalLecons * 100;
        }

        // Mettre à jour l'inscription
        inscriptionDAO.updateProgression(inscriptionId, pourcentage);
    }

    /**
     * Récupère la progression de toutes les leçons pour une inscription.
     */
    public List<ProgressionLecon> getProgression(Long inscriptionId) throws SQLException {
        return progressionDAO.findByInscriptionId(inscriptionId);
    }
}