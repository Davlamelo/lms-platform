package com.formation.lms.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilitaire pour le hashage sécurisé des mots de passe avec BCrypt.
 *
 * BOILERPLATE SÉCURITÉ : réutilisable dans tous tes projets.
 * Ne jamais stocker un mot de passe en clair !
 *
 * Usage :
 *   String hash = PasswordUtil.hasher("monMotDePasse");
 *   boolean ok = PasswordUtil.verifier("monMotDePasse", hash);
 */
public class PasswordUtil {

    // Facteur de coût BCrypt (12 = bon compromis sécurité/performance)
    // Plus c'est élevé, plus c'est lent (et sécurisé)
    // 10 = ~100ms, 12 = ~250ms, 14 = ~1s
    private static final int COUT_BCRYPT = 12;

    /**
     * Hash un mot de passe en clair avec BCrypt.
     * @param motDePasseEnClair le mot de passe saisi par l'utilisateur
     * @return le hash BCrypt (60 caractères, commence par $2a$)
     */
    public static String hasher(String motDePasseEnClair) {
        return BCrypt.hashpw(motDePasseEnClair, BCrypt.gensalt(COUT_BCRYPT));
    }

    /**
     * Vérifie si un mot de passe en clair correspond à un hash BCrypt.
     * @param motDePasseEnClair le mot de passe saisi par l'utilisateur
     * @param hashBDD le hash stocké en base de données
     * @return true si le mot de passe correspond
     */
    public static boolean verifier(String motDePasseEnClair, String hashBDD) {
        return BCrypt.checkpw(motDePasseEnClair, hashBDD);
    }

    private PasswordUtil() {
    }
}