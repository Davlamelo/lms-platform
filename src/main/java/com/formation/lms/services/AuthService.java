package com.formation.lms.services;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.UtilisateurDAO;
import com.formation.lms.models.Role;
import com.formation.lms.models.Utilisateur;
import com.formation.lms.utils.PasswordUtil;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Service d'authentification.
 * Gère l'inscription, la connexion et la validation des données.
 *
 * LOGIQUE MÉTIER : ce code est spécifique à ton projet.
 * Le Service appelle le DAO (jamais de SQL ici).
 */
public class AuthService {

    private final UtilisateurDAO utilisateurDAO;

    public AuthService() {
        this.utilisateurDAO = DAOFactory.getUtilisateurDAO();
    }

    /**
     * Inscrit un nouvel utilisateur sur la plateforme.
     *
     * Règles métier :
     * - L'email ne doit pas déjà exister
     * - Le mot de passe doit faire au moins 6 caractères
     * - Le prénom et nom sont obligatoires
     * - Le rôle par défaut est APPRENANT
     * - Le mot de passe est hashé avant stockage
     *
     * @return l'utilisateur créé avec son ID
     * @throws IllegalArgumentException si les données sont invalides
     * @throws SQLException si erreur BDD
     */
    public Utilisateur inscrire(String prenom, String nom, String email,
                                String motDePasse) throws SQLException {

        // --- Validation des données ---
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est obligatoire.");
        }
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire.");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Format d'email invalide.");
        }
        if (motDePasse == null || motDePasse.length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères.");
        }

        // --- Vérifier unicité email ---
        if (utilisateurDAO.existsByEmail(email.trim())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }

        // --- Hasher le mot de passe ---
        String motDePasseHash = PasswordUtil.hasher(motDePasse);

        // --- Créer et sauvegarder l'utilisateur ---
        Utilisateur nouvelUtilisateur = new Utilisateur(
                email.trim().toLowerCase(),
                motDePasseHash,
                prenom.trim(),
                nom.trim(),
                Role.APPRENANT
        );

        return utilisateurDAO.save(nouvelUtilisateur);
    }

    /**
     * Authentifie un utilisateur par email et mot de passe.
     *
     * Règles métier :
     * - L'utilisateur doit exister
     * - Le mot de passe doit correspondre au hash en BDD
     * - Le compte doit être actif (non suspendu)
     *
     * @return l'utilisateur authentifié
     * @throws IllegalArgumentException si les identifiants sont invalides
     * @throws SQLException si erreur BDD
     */
    public Utilisateur authentifier(String email, String motDePasse) throws SQLException {

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire.");
        }
        if (motDePasse == null || motDePasse.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire.");
        }

        // --- Chercher l'utilisateur par email ---
        Optional<Utilisateur> optUtilisateur = utilisateurDAO.findByEmail(email.trim().toLowerCase());

        if (optUtilisateur.isEmpty()) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect.");
        }

        Utilisateur utilisateur = optUtilisateur.get();

        // --- Vérifier le mot de passe ---
        if (!PasswordUtil.verifier(motDePasse, utilisateur.getMotDePasse())) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect.");
        }

        // --- Vérifier que le compte est actif ---
        if (!utilisateur.isActif()) {
            throw new IllegalArgumentException("Votre compte a été suspendu. Contactez l'administrateur.");
        }

        return utilisateur;
    }
}