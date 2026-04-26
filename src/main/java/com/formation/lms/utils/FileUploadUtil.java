package com.formation.lms.utils;

import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

/**
 * Utilitaire pour la gestion des uploads de fichiers.
 *
 * BOILERPLATE RÉUTILISABLE : identique dans tous les projets Java web.
 * Seuls les dossiers de destination changent.
 */
public class FileUploadUtil {

    // Extensions autorisées pour les images
    private static final String[] EXTENSIONS_IMAGES = {
            ".jpg", ".jpeg", ".png", ".gif", ".webp"
    };

    // Taille max : 5 Mo pour les images
    private static final long TAILLE_MAX_IMAGE = 5 * 1024 * 1024;

    /**
     * Sauvegarde un fichier uploadé sur le disque.
     *
     * @param part          le fichier reçu depuis le formulaire
     * @param cheminRacine  chemin absolu du dossier webapp
     * @param sousDossier   sous-dossier de destination (ex: "uploads/avatars/")
     * @return le chemin relatif du fichier sauvegardé (pour stocker en BDD)
     */
    public static String sauvegarderFichier(Part part,
                                            String cheminRacine,
                                            String sousDossier)
            throws IOException {

        if (part == null || part.getSize() == 0) {
            return null;
        }

        // Récupérer le nom original du fichier
        String nomOriginal = extraireNomFichier(part);
        if (nomOriginal == null || nomOriginal.isEmpty()) {
            return null;
        }

        // Récupérer l'extension
        String extension = extraireExtension(nomOriginal).toLowerCase();

        // Vérifier que c'est bien une image
        if (!estExtensionAutorisee(extension, EXTENSIONS_IMAGES)) {
            throw new IllegalArgumentException(
                    "Format non autorisé. Utilisez JPG, PNG, GIF ou WEBP.");
        }

        // Vérifier la taille
        if (part.getSize() > TAILLE_MAX_IMAGE) {
            throw new IllegalArgumentException(
                    "Fichier trop volumineux. Maximum 5 Mo.");
        }

        // Générer un nom unique pour éviter les conflits
        String nomFichier = UUID.randomUUID().toString() + extension;
        String cheminRelatif = sousDossier + nomFichier;
        String cheminAbsolu = cheminRacine + File.separator + cheminRelatif;

        // Créer le dossier si nécessaire
        File dossier = new File(cheminRacine + File.separator + sousDossier);
        if (!dossier.exists()) {
            dossier.mkdirs();
        }

        // Sauvegarder le fichier
        try (InputStream is = part.getInputStream();
             OutputStream os = new FileOutputStream(cheminAbsolu)) {
            byte[] buffer = new byte[4096];
            int lu;
            while ((lu = is.read(buffer)) != -1) {
                os.write(buffer, 0, lu);
            }
        }

        return cheminRelatif;
    }

    /**
     * Supprime un fichier existant (pour remplacer un avatar ou une miniature).
     */
    public static void supprimerFichier(String cheminRacine, String cheminRelatif) {
        if (cheminRelatif == null || cheminRelatif.isEmpty()) return;
        File fichier = new File(cheminRacine + File.separator + cheminRelatif);
        if (fichier.exists()) {
            fichier.delete();
        }
    }

    /**
     * Extrait le nom du fichier depuis les headers multipart.
     */
    public static String extraireNomFichier(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition == null) return null;
        for (String element : contentDisposition.split(";")) {
            if (element.trim().startsWith("filename")) {
                String nom = element.substring(element.indexOf('=') + 1)
                        .trim().replace("\"", "");
                // Garder uniquement le nom sans le chemin (compatibilité IE)
                return Paths.get(nom).getFileName().toString();
            }
        }
        return null;
    }

    /**
     * Extrait l'extension d'un nom de fichier.
     */
    public static String extraireExtension(String nomFichier) {
        int idx = nomFichier.lastIndexOf('.');
        return idx >= 0 ? nomFichier.substring(idx) : "";
    }

    /**
     * Vérifie si l'extension est dans la liste autorisée.
     */
    public static boolean estExtensionAutorisee(String extension,
                                                String[] extensionsAutorisees) {
        for (String ext : extensionsAutorisees) {
            if (ext.equalsIgnoreCase(extension)) return true;
        }
        return false;
    }
}