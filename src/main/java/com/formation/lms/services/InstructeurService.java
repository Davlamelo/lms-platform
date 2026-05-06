package com.formation.lms.services;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.*;
import com.formation.lms.models.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service métier pour le module instructeur.
 * Gère la création/édition de cours et les statistiques.
 */
public class InstructeurService {

    private final CoursDAO coursDAO;
    private final SectionDAO sectionDAO;
    private final LeconDAO leconDAO;
    private final InscriptionDAO inscriptionDAO;
    private final AvisDAO avisDAO;

    public InstructeurService() {
        this.coursDAO = DAOFactory.getCoursDAO();
        this.sectionDAO = DAOFactory.getSectionDAO();
        this.leconDAO = DAOFactory.getLeconDAO();
        this.inscriptionDAO = DAOFactory.getInscriptionDAO();
        this.avisDAO = DAOFactory.getAvisDAO();
    }

    // === Dashboard ===

    /**
     * Récupère tous les cours d'un instructeur.
     */
    public List<Cours> getCoursInstructeur(Long instructeurId) throws SQLException {
        return coursDAO.findByInstructeur(instructeurId);
    }

    /**
     * Calcule les statistiques globales d'un instructeur.
     * Retourne : totalCours, totalInscrits, noteMoyenne, totalPublies
     */
    public Map<String, Object> getStatistiquesInstructeur(Long instructeurId) throws SQLException {
        List<Cours> cours = coursDAO.findByInstructeur(instructeurId);

        long totalInscrits = 0;
        double totalNotes = 0;
        long totalAvis = 0;
        long totalPublies = 0;

        for (Cours c : cours) {
            totalInscrits += inscriptionDAO.countByCoursId(c.getId());
            long nbAvis = avisDAO.countByCoursId(c.getId());
            if (nbAvis > 0) {
                totalNotes += avisDAO.getNoteMoyenne(c.getId()) * nbAvis;
                totalAvis += nbAvis;
            }
            if (c.getStatut() == StatutCours.PUBLIE) {
                totalPublies++;
            }
        }

        double noteMoyenneGlobale = totalAvis > 0 ? totalNotes / totalAvis : 0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCours", cours.size());
        stats.put("totalInscrits", totalInscrits);
        stats.put("noteMoyenne", Math.round(noteMoyenneGlobale * 10.0) / 10.0);
        stats.put("totalPublies", totalPublies);
        return stats;
    }

    /**
     * Calcule les stats d'un cours spécifique.
     */
    public Map<String, Object> getStatsCours(Long coursId) throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        stats.put("inscrits", inscriptionDAO.countByCoursId(coursId));
        stats.put("noteMoyenne", avisDAO.getNoteMoyenne(coursId));
        stats.put("nbAvis", avisDAO.countByCoursId(coursId));
        stats.put("nbLecons", leconDAO.countByCoursId(coursId));
        stats.put("nbSections", sectionDAO.countByCoursId(coursId));
        return stats;
    }

    // === Gestion des cours ===

    /**
     * Crée un nouveau cours en statut BROUILLON.
     */
    public Cours creerCours(String titre, String descriptionCourte, String descriptionLongue,
                            Long categorieId, NiveauCours niveau, Long instructeurId)
            throws SQLException {

        if (titre == null || titre.trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre du cours est obligatoire.");
        }
        if (categorieId == null) {
            throw new IllegalArgumentException("La catégorie est obligatoire.");
        }

        String slug = genererSlug(titre);

        Cours cours = new Cours(titre.trim(), slug, descriptionCourte, niveau,
                instructeurId, categorieId);
        cours.setDescriptionLongue(descriptionLongue);
        cours.setStatut(StatutCours.BROUILLON);

        return coursDAO.save(cours);
    }

    /**
     * Met à jour les informations générales d'un cours.
     */
    public boolean mettreAJourCours(Cours cours) throws SQLException {
        return coursDAO.update(cours);
    }

    /**
     * Soumet un cours pour validation par l'admin.
     */
    public boolean soumettrePourValidation(Long coursId, Long instructeurId) throws SQLException {
        Optional<Cours> optCours = coursDAO.findById(coursId);
        if (optCours.isEmpty()) {
            throw new IllegalArgumentException("Cours introuvable.");
        }

        Cours cours = optCours.get();

        if (!cours.getInstructeurId().equals(instructeurId)) {
            throw new SecurityException("Vous n'êtes pas l'instructeur de ce cours.");
        }

        long nbSections = sectionDAO.countByCoursId(coursId);
        if (nbSections == 0) {
            throw new IllegalStateException("Ajoutez au moins une section avant de soumettre.");
        }

        return coursDAO.updateStatut(coursId, StatutCours.EN_ATTENTE_VALIDATION);
    }

    // === Gestion des sections ===

    /**
     * Ajoute une section à un cours.
     */
    public Section ajouterSection(Long coursId, String titre, Long instructeurId)
            throws SQLException {

        verifierProprietaire(coursId, instructeurId);

        long nbSections = sectionDAO.countByCoursId(coursId);
        int ordre = (int) nbSections + 1;

        Section section = new Section(coursId, titre.trim(), ordre);
        return sectionDAO.save(section);
    }

    /**
     * Récupère les sections d'un cours.
     */
    public List<Section> getSections(Long coursId) throws SQLException {
        return sectionDAO.findByCoursId(coursId);
    }

    /**
     * Supprime une section.
     */
    public boolean supprimerSection(Long sectionId) throws SQLException {
        return sectionDAO.delete(sectionId);
    }

    // === Gestion des leçons ===

    /**
     * Ajoute une leçon à une section et recalcule la durée du cours.
     * MODIFIÉ : ajout du paramètre coursId pour le recalcul automatique.
     */
    public Lecon ajouterLecon(Long sectionId, String titre, TypeLecon typeLecon,
                              String contenu, int dureeMin, Long coursId) throws SQLException {

        List<Lecon> lecons = leconDAO.findBySectionId(sectionId);
        int ordre = lecons.size() + 1;

        Lecon lecon = new Lecon(sectionId, titre.trim(), typeLecon, ordre);
        lecon.setDureeMin(dureeMin);

        if (typeLecon == TypeLecon.TEXTE) {
            lecon.setContenuTexte(contenu);
        } else if (typeLecon == TypeLecon.VIDEO) {
            lecon.setVideoUrl(contenu);
        } else if (typeLecon == TypeLecon.RESSOURCE) {
            lecon.setRessourceUrl(contenu);
        }

        Lecon leconSauvee = leconDAO.save(lecon);

        // AJOUTÉ : recalcul automatique de la durée totale du cours
        recalculerDureeCours(coursId);

        return leconSauvee;
    }

    /**
     * Récupère les leçons d'une section.
     */
    public List<Lecon> getLecons(Long sectionId) throws SQLException {
        return leconDAO.findBySectionId(sectionId);
    }

    /**
     * Supprime une leçon et recalcule la durée du cours.
     * MODIFIÉ : ajout du paramètre coursId pour le recalcul automatique.
     */
    public boolean supprimerLecon(Long leconId, Long coursId) throws SQLException {
        boolean result = leconDAO.delete(leconId);

        // AJOUTÉ : recalcul automatique de la durée totale du cours
        recalculerDureeCours(coursId);

        return result;
    }

    // === Durée ===

    /**
     * Recalcule et met à jour la durée totale d'un cours
     * en additionnant les durées de toutes ses leçons.
     * À appeler après chaque ajout, modification ou suppression de leçon.
     */
    public void recalculerDureeCours(Long coursId) throws SQLException {
        List<Section> sections = sectionDAO.findByCoursId(coursId);
        int total = 0;
        for (Section section : sections) {
            for (Lecon lecon : leconDAO.findBySectionId(section.getId())) {
                total += lecon.getDureeMin();
            }
        }
        Cours cours = coursDAO.findById(coursId)
                .orElseThrow(() -> new IllegalArgumentException("Cours introuvable."));
        cours.setDureeTotaleMin(total);
        coursDAO.update(cours);
    }

    // === Utilitaires ===

    /**
     * Vérifie que le cours appartient bien à l'instructeur.
     */
    private void verifierProprietaire(Long coursId, Long instructeurId) throws SQLException {
        Optional<Cours> optCours = coursDAO.findById(coursId);
        if (optCours.isEmpty()) {
            throw new IllegalArgumentException("Cours introuvable.");
        }
        if (!optCours.get().getInstructeurId().equals(instructeurId)) {
            throw new SecurityException("Vous n'avez pas les droits sur ce cours.");
        }
    }

    /**
     * Génère un slug URL-friendly depuis un titre.
     * Exemple : "Python pour la Data Science" → "python-pour-la-data-science"
     */
    public String genererSlug(String titre) throws SQLException {
        String slug = titre.toLowerCase()
                .replaceAll("[àâä]", "a")
                .replaceAll("[éèêë]", "e")
                .replaceAll("[îï]", "i")
                .replaceAll("[ôö]", "o")
                .replaceAll("[ùûü]", "u")
                .replaceAll("[ç]", "c")
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();

        String slugFinal = slug;
        int compteur = 1;
        while (coursDAO.findBySlug(slugFinal).isPresent()) {
            slugFinal = slug + "-" + compteur;
            compteur++;
        }
        return slugFinal;
    }
}