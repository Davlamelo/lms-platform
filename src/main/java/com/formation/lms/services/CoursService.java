package com.formation.lms.services;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.*;
import com.formation.lms.models.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des cours.
 * Centralise les opérations liées aux cours, sections, leçons.
 */
public class CoursService {

    private final CoursDAO coursDAO;
    private final CategorieDAO categorieDAO;
    private final SectionDAO sectionDAO;
    private final LeconDAO leconDAO;
    private final AvisDAO avisDAO;
    private final InscriptionDAO inscriptionDAO;

    public CoursService() {
        this.coursDAO = DAOFactory.getCoursDAO();
        this.categorieDAO = DAOFactory.getCategorieDAO();
        this.sectionDAO = DAOFactory.getSectionDAO();
        this.leconDAO = DAOFactory.getLeconDAO();
        this.avisDAO = DAOFactory.getAvisDAO();
        this.inscriptionDAO = DAOFactory.getInscriptionDAO();
    }

    // === Catalogue ===

    /**
     * Récupère tous les cours publiés pour le catalogue.
     */
    public List<Cours> getCoursPublies() throws SQLException {
        return coursDAO.findPublies();
    }

    /**
     * Récupère les cours publiés avec pagination.
     */
    public List<Cours> getCoursPubliesPagines(int page, int taillePage) throws SQLException {
        return coursDAO.findPubliesPagines(page, taillePage);
    }

    /**
     * Récupère les cours publiés d'une catégorie.
     */
    public List<Cours> getCoursParCategorie(Long categorieId) throws SQLException {
        return coursDAO.findByCategorie(categorieId);
    }

    /**
     * Recherche de cours par mot-clé (FULLTEXT).
     */
    public List<Cours> rechercherCours(String motCle) throws SQLException {
        if (motCle == null || motCle.trim().isEmpty()) {
            return coursDAO.findPublies();
        }
        return coursDAO.rechercher(motCle.trim());
    }

    // === Détail d'un cours ===

    /**
     * Récupère un cours par son ID.
     */
    public Optional<Cours> getCoursById(Long id) throws SQLException {
        return coursDAO.findById(id);
    }

    /**
     * Récupère un cours par son slug (pour les URL).
     */
    public Optional<Cours> getCoursParSlug(String slug) throws SQLException {
        return coursDAO.findBySlug(slug);
    }

    /**
     * Récupère les sections d'un cours (triées par ordre).
     */
    public List<Section> getSections(Long coursId) throws SQLException {
        return sectionDAO.findByCoursId(coursId);
    }

    /**
     * Récupère les leçons d'une section (triées par ordre).
     */
    public List<Lecon> getLecons(Long sectionId) throws SQLException {
        return leconDAO.findBySectionId(sectionId);
    }

    /**
     * Récupère toutes les leçons d'un cours (toutes sections).
     */
    public List<Lecon> getToutesLeconsParCours(Long coursId) throws SQLException {
        return leconDAO.findByCoursId(coursId);
    }

    /**
     * Compte le nombre total de leçons d'un cours.
     */
    public long getNombreLecons(Long coursId) throws SQLException {
        return leconDAO.countByCoursId(coursId);
    }

    // === Statistiques d'un cours ===

    /**
     * Calcule la note moyenne d'un cours.
     */
    public double getNoteMoyenne(Long coursId) throws SQLException {
        return avisDAO.getNoteMoyenne(coursId);
    }

    /**
     * Compte le nombre d'avis d'un cours.
     */
    public long getNombreAvis(Long coursId) throws SQLException {
        return avisDAO.countByCoursId(coursId);
    }

    /**
     * Compte le nombre d'inscrits à un cours.
     */
    public long getNombreInscrits(Long coursId) throws SQLException {
        return inscriptionDAO.countByCoursId(coursId);
    }

    // === Catégories ===

    /**
     * Récupère toutes les catégories.
     */
    public List<Categorie> getToutesCategories() throws SQLException {
        return categorieDAO.findAll();
    }

    /**
     * Récupère une catégorie par son slug.
     */
    public Optional<Categorie> getCategorieParSlug(String slug) throws SQLException {
        return categorieDAO.findBySlug(slug);
    }
}