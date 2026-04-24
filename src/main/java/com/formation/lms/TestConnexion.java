package com.formation.lms;

import com.formation.lms.config.DBConnection;
import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.CategorieDAO;
import com.formation.lms.dao.interfaces.UtilisateurDAO;
import com.formation.lms.models.Categorie;
import com.formation.lms.models.Role;
import com.formation.lms.models.Utilisateur;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Classe de test pour valider :
 * 1. La connexion à la BDD (HikariCP)
 * 2. Le CRUD via les DAO
 *
 * À exécuter avec : clic droit → Run 'TestConnexion.main()'
 * À supprimer avant la mise en production.
 */
public class TestConnexion {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  TEST DE CONNEXION À LA BASE DE DONNÉES  ");
        System.out.println("===========================================\n");

        // ===== TEST 1 : Connexion brute =====
        System.out.println("--- Test 1 : Connexion HikariCP ---");
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("✅ Connexion réussie !");
            System.out.println("   Base : " + conn.getCatalog());
            System.out.println("   URL  : " + conn.getMetaData().getURL());
        } catch (Exception e) {
            System.out.println("❌ Échec de connexion : " + e.getMessage());
            e.printStackTrace();
            return; // Pas la peine de continuer si la connexion échoue
        }

        // ===== TEST 2 : INSERT + SELECT sur Categorie =====
        System.out.println("\n--- Test 2 : CRUD CategorieDAO ---");
        try {
            CategorieDAO categorieDAO = DAOFactory.getCategorieDAO();

            // INSERT
            Categorie nouvelleCat = new Categorie("Data Science", "data-science",
                    "Analyse de données, ML, statistiques");
            Categorie catSauvee = categorieDAO.save(nouvelleCat);
            System.out.println("✅ INSERT réussi : " + catSauvee);
            System.out.println("   ID généré : " + catSauvee.getId());

            // SELECT by ID
            Optional<Categorie> catTrouvee = categorieDAO.findById(catSauvee.getId());
            if (catTrouvee.isPresent()) {
                System.out.println("✅ SELECT by ID réussi : " + catTrouvee.get());
            } else {
                System.out.println("❌ SELECT by ID échoué : catégorie non trouvée");
            }

            // SELECT by slug
            Optional<Categorie> catParSlug = categorieDAO.findBySlug("data-science");
            if (catParSlug.isPresent()) {
                System.out.println("✅ SELECT by slug réussi : " + catParSlug.get());
            } else {
                System.out.println("❌ SELECT by slug échoué");
            }

            // COUNT
            long nbCategories = categorieDAO.count();
            System.out.println("✅ COUNT : " + nbCategories + " catégorie(s) en BDD");

            // DELETE (nettoyage)
            boolean supprime = categorieDAO.delete(catSauvee.getId());
            System.out.println("✅ DELETE réussi : " + supprime);

        } catch (Exception e) {
            System.out.println("❌ Erreur CRUD Categorie : " + e.getMessage());
            e.printStackTrace();
        }

        // ===== TEST 3 : INSERT + SELECT sur Utilisateur =====
        System.out.println("\n--- Test 3 : CRUD UtilisateurDAO ---");
        try {
            UtilisateurDAO utilisateurDAO = DAOFactory.getUtilisateurDAO();

            // INSERT
            Utilisateur nouveauUser = new Utilisateur(
                    "test@lms-platform.com", "motdepasse_hash_123",
                    "David", "Test", Role.APPRENANT);
            Utilisateur userSauve = utilisateurDAO.save(nouveauUser);
            System.out.println("✅ INSERT réussi : " + userSauve);

            // SELECT by email
            Optional<Utilisateur> userParEmail = utilisateurDAO.findByEmail("test@lms-platform.com");
            if (userParEmail.isPresent()) {
                System.out.println("✅ SELECT by email réussi : " + userParEmail.get());
                System.out.println("   Nom complet : " + userParEmail.get().getNomComplet());
                System.out.println("   Rôle : " + userParEmail.get().getRole());
            } else {
                System.out.println("❌ SELECT by email échoué");
            }

            // EXISTS
            boolean existe = utilisateurDAO.existsByEmail("test@lms-platform.com");
            System.out.println("✅ EXISTS : " + existe);

            // FIND BY ROLE
            List<Utilisateur> apprenants = utilisateurDAO.findByRole(Role.APPRENANT);
            System.out.println("✅ FIND BY ROLE (APPRENANT) : " + apprenants.size() + " résultat(s)");

            // DELETE (nettoyage)
            utilisateurDAO.delete(userSauve.getId());
            System.out.println("✅ DELETE réussi");

        } catch (Exception e) {
            System.out.println("❌ Erreur CRUD Utilisateur : " + e.getMessage());
            e.printStackTrace();
        }

        // ===== FIN =====
        System.out.println("\n===========================================");
        System.out.println("  TOUS LES TESTS TERMINÉS");
        System.out.println("===========================================");

        // Fermer le pool proprement
        DBConnection.fermerPool();
    }
}