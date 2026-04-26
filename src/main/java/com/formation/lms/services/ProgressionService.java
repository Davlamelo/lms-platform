package com.formation.lms.services;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.*;
import com.formation.lms.models.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service gérant la progression de l'apprenant dans un cours.
 *
 * Concept transférable : Service de tracking d'état — on suit l'avancement
 * d'une entité à travers une séquence d'étapes ordonnées.
 */
public class ProgressionService {

    // --- Boilerplate : injection via DAOFactory ---
    private final InscriptionDAO inscriptionDAO;
    private final ProgressionLeconDAO progressionLeconDAO;
    private final LeconDAO leconDAO;

    public ProgressionService() {
        this.inscriptionDAO = DAOFactory.getInscriptionDAO();
        this.progressionLeconDAO = DAOFactory.getProgressionLeconDAO();
        this.leconDAO = DAOFactory.getLeconDAO();
    }

    /**
     * Marque une leçon comme complétée et recalcule le pourcentage global.
     *
     * Logique métier :
     * 1. Délègue à marquerCompletee() du DAO (idempotent)
     * 2. Recompte les leçons complétées / total leçons du cours
     * 3. Met à jour l'inscription (pourcentage + statut si 100%)
     *
     * Concept transférable : orchestration transactionnelle — un service
     * coordonne plusieurs DAOs pour maintenir la cohérence des données.
     *
     * @return le nouveau pourcentage (0-100)
     */
    public int completerLecon(Long inscriptionId, Long leconId, Long coursId)
            throws SQLException {

        // 1. Marquer la leçon comme complétée (idempotent dans le DAO)
        progressionLeconDAO.marquerCompletee(inscriptionId, leconId);

        // 2. Recalculer le pourcentage
        int pourcentage = calculerPourcentage(inscriptionId, coursId);

        // 3. Mettre à jour l'inscription
        Optional<Inscription> optInsc = inscriptionDAO.findById(inscriptionId);
        if (optInsc.isPresent()) {
            Inscription inscription = optInsc.get();
            inscription.setPourcentageProgression(pourcentage);

            if (pourcentage >= 100) {
                inscription.setStatut(StatutInscription.TERMINE);
                inscription.setDateCompletion(LocalDateTime.now());
            }

            inscription.setDerniereActivite(LocalDateTime.now());
            inscriptionDAO.update(inscription);
        }

        return pourcentage;
    }

    /**
     * Calcule le pourcentage de progression sur l'ensemble du cours.
     *
     * Logique métier : (leçons complétées / total leçons du cours) * 100
     * On utilise findByCoursId() qui traverse déjà toutes les sections.
     */
    public int calculerPourcentage(Long inscriptionId, Long coursId)
            throws SQLException {

        // LeconDAO.findByCoursId() retourne toutes les leçons (toutes sections)
        long totalLecons = leconDAO.countByCoursId(coursId);
        if (totalLecons == 0) return 0;

        long leconsCompletees = progressionLeconDAO.countCompletees(inscriptionId);
        return (int) Math.round((double) leconsCompletees / totalLecons * 100);
    }

    /**
     * Détermine la prochaine leçon non complétée pour le bouton "Continuer".
     *
     * Logique métier : parcourt les leçons dans l'ordre du cours,
     * retourne la première non complétée.
     * Utilise findByCoursId() qui trie déjà par section_ordre puis lecon_ordre.
     */
    public Optional<Lecon> getProchaineLecon(Long inscriptionId, Long coursId)
            throws SQLException {

        // Toutes les leçons du cours, déjà triées par ordre
        List<Lecon> lecons = leconDAO.findByCoursId(coursId);

        for (Lecon lecon : lecons) {
            Optional<ProgressionLecon> prog = progressionLeconDAO
                    .findByInscriptionEtLecon(inscriptionId, lecon.getId());

            // Non commencée ou non complétée → c'est la prochaine
            if (prog.isEmpty() || !prog.get().isEstCompletee()) {
                return Optional.of(lecon);
            }
        }

        return Optional.empty(); // Tout est complété
    }

    /**
     * Vérifie si une leçon est complétée pour une inscription donnée.
     * Pratique pour la sidebar sans recharger toute la liste.
     */
    public boolean estLeconCompletee(Long inscriptionId, Long leconId)
            throws SQLException {

        Optional<ProgressionLecon> prog = progressionLeconDAO
                .findByInscriptionEtLecon(inscriptionId, leconId);
        return prog.isPresent() && prog.get().isEstCompletee();
    }
}