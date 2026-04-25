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
        System.out.println("Hash de password123 : " + com.formation.lms.utils.PasswordUtil.hasher("password123"));
    }
}