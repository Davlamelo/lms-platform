package com.formation.lms.services;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.*;
import com.formation.lms.models.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service métier pour le module administrateur.
 * Centralise les opérations de modération et gestion de la plateforme.
 */
public class AdminService {

    private final CoursDAO coursDAO;
    private final UtilisateurDAO utilisateurDAO;
    private final CategorieDAO categorieDAO;
    private final InscriptionDAO inscriptionDAO;

    public AdminService() {
        this.coursDAO = DAOFactory.getCoursDAO();
        this.utilisateurDAO = DAOFactory.getUtilisateurDAO();
        this.categorieDAO = DAOFactory.getCategorieDAO();
        this.inscriptionDAO = DAOFactory.getInscriptionDAO();
    }

    // === Statistiques globales ===

    /**
     * Récupère les statistiques globales de la plateforme.
     */
    public Map<String, Object> getStatsGlobales() throws SQLException {
        Map<String, Object> stats = new HashMap<>();

        List<Utilisateur> tousUtilisateurs = utilisateurDAO.findAll();
        long totalApprenants = tousUtilisateurs.stream()
                .filter(u -> u.getRole() == Role.APPRENANT).count();
        long totalInstructeurs = tousUtilisateurs.stream()
                .filter(u -> u.getRole() == Role.INSTRUCTEUR).count();

        long totalCours = coursDAO.count();
        long coursPublies = coursDAO.findPublies().size();
        long coursEnAttente = coursDAO.findByStatut(StatutCours.EN_ATTENTE_VALIDATION).size();
        long totalInscriptions = inscriptionDAO.count();

        stats.put("totalUtilisateurs", tousUtilisateurs.size());
        stats.put("totalApprenants", totalApprenants);
        stats.put("totalInstructeurs", totalInstructeurs);
        stats.put("totalCours", totalCours);
        stats.put("coursPublies", coursPublies);
        stats.put("coursEnAttente", coursEnAttente);
        stats.put("totalInscriptions", totalInscriptions);

        return stats;
    }

    // === Modération des cours ===

    /**
     * Récupère les cours en attente de validation.
     */
    public List<Cours> getCoursEnAttente() throws SQLException {
        return coursDAO.findByStatut(StatutCours.EN_ATTENTE_VALIDATION);
    }

    /**
     * Récupère tous les cours (toutes statuts) pour l'admin.
     */
    public List<Cours> getTousCours() throws SQLException {
        return coursDAO.findAll();
    }

    /**
     * Valide un cours → le publie.
     */
    public boolean validerCours(Long coursId) throws SQLException {
        return coursDAO.updateStatut(coursId, StatutCours.PUBLIE);
    }

    /**
     * Rejette un cours → retour en REJETE.
     */
    public boolean rejeterCours(Long coursId) throws SQLException {
        return coursDAO.updateStatut(coursId, StatutCours.REJETE);
    }

    /**
     * Archive un cours publié.
     */
    public boolean archiverCours(Long coursId) throws SQLException {
        return coursDAO.updateStatut(coursId, StatutCours.ARCHIVE);
    }

    // === Gestion des utilisateurs ===

    /**
     * Récupère tous les utilisateurs.
     */
    public List<Utilisateur> getTousUtilisateurs() throws SQLException {
        return utilisateurDAO.findAll();
    }

    /**
     * Récupère les utilisateurs par rôle.
     */
    public List<Utilisateur> getUtilisateursByRole(Role role) throws SQLException {
        return utilisateurDAO.findByRole(role);
    }

    /**
     * Active ou suspend un compte utilisateur.
     */
    public boolean toggleStatutUtilisateur(Long userId) throws SQLException {
        return utilisateurDAO.findById(userId).map(u -> {
            try {
                return utilisateurDAO.updateStatutActif(userId, !u.isActif());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).orElse(false);
    }

    /**
     * Promeut un apprenant au rang d'instructeur.
     */
    public boolean promouvoirInstructeur(Long userId) throws SQLException {
        return utilisateurDAO.findById(userId).map(u -> {
            try {
                u.setRole(Role.INSTRUCTEUR);
                return utilisateurDAO.update(u);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).orElse(false);
    }

    // === Gestion des catégories ===

    /**
     * Récupère toutes les catégories.
     */
    public List<Categorie> getToutesCategories() throws SQLException {
        return categorieDAO.findAll();
    }

    /**
     * Crée une nouvelle catégorie.
     */
    public Categorie creerCategorie(String nom, String description,
                                    String icone) throws SQLException {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la catégorie est obligatoire.");
        }
        if (categorieDAO.existsByNom(nom.trim())) {
            throw new IllegalArgumentException("Une catégorie avec ce nom existe déjà.");
        }

        // Générer le slug
        String slug = nom.toLowerCase()
                .replaceAll("[àâä]", "a").replaceAll("[éèêë]", "e")
                .replaceAll("[îï]", "i").replaceAll("[ôö]", "o")
                .replaceAll("[ùûü]", "u").replaceAll("[ç]", "c")
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-").trim();

        Categorie categorie = new Categorie(nom.trim(), slug, description);
        categorie.setIcone(icone != null ? icone : "bi-book");
        return categorieDAO.save(categorie);
    }

    /**
     * Supprime une catégorie (si elle n'a pas de cours associés).
     */
    public boolean supprimerCategorie(Long categorieId) throws SQLException {
        long nbCours = coursDAO.countByCategorie(categorieId);
        if (nbCours > 0) {
            throw new IllegalStateException(
                    "Impossible de supprimer : cette catégorie contient " + nbCours + " cours.");
        }
        return categorieDAO.delete(categorieId);
    }
}