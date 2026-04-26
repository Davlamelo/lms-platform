package com.formation.lms.services;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.dao.interfaces.*;
import com.formation.lms.models.*;
import com.formation.lms.utils.PDFGeneratorUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service de génération et gestion des certificats.
 */
public class CertificatService {

    private final CertificatDAO certificatDAO;
    private final InscriptionDAO inscriptionDAO;
    private final CoursDAO coursDAO;
    private final UtilisateurDAO utilisateurDAO;

    // Dossier de stockage des certificats
    private static final String DOSSIER_CERTIFICATS = "uploads/certificats/";

    public CertificatService() {
        this.certificatDAO = DAOFactory.getCertificatDAO();
        this.inscriptionDAO = DAOFactory.getInscriptionDAO();
        this.coursDAO = DAOFactory.getCoursDAO();
        this.utilisateurDAO = DAOFactory.getUtilisateurDAO();
    }

    /**
     * Génère un certificat si le cours est complété à 100%.
     * Si un certificat existe déjà, retourne l'existant.
     *
     * @param inscriptionId l'inscription de l'apprenant
     * @param cheminRacine  chemin absolu du dossier webapp (pour stocker le PDF)
     * @return le certificat généré ou existant
     */
    public Certificat genererOuRecupererCertificat(Long inscriptionId,
                                                   String cheminRacine)
            throws Exception {

        // Vérifier si un certificat existe déjà
        Optional<Certificat> certExistant = certificatDAO.findByInscriptionId(inscriptionId);
        if (certExistant.isPresent()) {
            return certExistant.get();
        }

        // Récupérer l'inscription
        Inscription inscription = inscriptionDAO.findById(inscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Inscription introuvable."));

        // Vérifier que le cours est bien complété à 100%
        if (inscription.getPourcentageProgression() < 100.0) {
            throw new IllegalStateException(
                    "Le cours doit être complété à 100% pour obtenir un certificat.");
        }

        // Récupérer les données
        Cours cours = coursDAO.findById(inscription.getCoursId())
                .orElseThrow(() -> new IllegalArgumentException("Cours introuvable."));

        Utilisateur apprenant = utilisateurDAO.findById(inscription.getApprenantId())
                .orElseThrow(() -> new IllegalArgumentException("Apprenant introuvable."));

        Utilisateur instructeur = utilisateurDAO.findById(cours.getInstructeurId())
                .orElse(null);

        String nomInstructeur = instructeur != null
                ? instructeur.getNomComplet() : "LMS Platform";

        // Générer un code unique
        String codeVerification = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        // Générer le PDF
        byte[] pdfBytes = PDFGeneratorUtil.genererCertificat(
                apprenant.getNomComplet(),
                cours.getTitre(),
                nomInstructeur,
                LocalDateTime.now(),
                codeVerification
        );

        // Sauvegarder le fichier PDF sur disque
        String nomFichier = "certificat_" + inscriptionId + "_" + codeVerification + ".pdf";
        String cheminRelatif = DOSSIER_CERTIFICATS + nomFichier;
        String cheminAbsolu = cheminRacine + File.separator + cheminRelatif;

        // Créer le dossier si nécessaire
        File dossier = new File(cheminRacine + File.separator + DOSSIER_CERTIFICATS);
        if (!dossier.exists()) {
            dossier.mkdirs();
        }

        // Écrire le PDF
        try (FileOutputStream fos = new FileOutputStream(cheminAbsolu)) {
            fos.write(pdfBytes);
        }

        // Enregistrer en BDD
        Certificat certificat = new Certificat(inscriptionId, codeVerification, cheminRelatif);
        certificat.setDateEmission(LocalDateTime.now());
        return certificatDAO.save(certificat);
    }

    /**
     * Récupère le certificat d'une inscription.
     */
    public Optional<Certificat> getCertificat(Long inscriptionId) throws SQLException {
        return certificatDAO.findByInscriptionId(inscriptionId);
    }

    /**
     * Vérifie l'authenticité d'un certificat par son code.
     */
    public Optional<Certificat> verifierCertificat(String code) throws SQLException {
        return certificatDAO.findByCodeVerification(code);
    }
}