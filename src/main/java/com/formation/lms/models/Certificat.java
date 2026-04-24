package com.formation.lms.models;

import java.time.LocalDateTime;

/**
 * Certificat généré quand un apprenant termine un cours à 100%.
 * Correspond à la table 'certificats'.
 */
public class Certificat {

    private Long id;
    private Long inscriptionId;
    private String codeVerification;
    private String fichierPdfUrl;
    private LocalDateTime dateEmission;

    public Certificat() {
    }

    public Certificat(Long inscriptionId, String codeVerification, String fichierPdfUrl) {
        this.inscriptionId = inscriptionId;
        this.codeVerification = codeVerification;
        this.fichierPdfUrl = fichierPdfUrl;
    }

    // === Getters & Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(Long inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

    public String getCodeVerification() {
        return codeVerification;
    }

    public void setCodeVerification(String codeVerification) {
        this.codeVerification = codeVerification;
    }

    public String getFichierPdfUrl() {
        return fichierPdfUrl;
    }

    public void setFichierPdfUrl(String fichierPdfUrl) {
        this.fichierPdfUrl = fichierPdfUrl;
    }

    public LocalDateTime getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDateTime dateEmission) {
        this.dateEmission = dateEmission;
    }

    @Override
    public String toString() {
        return "Certificat{" +
                "id=" + id +
                ", inscriptionId=" + inscriptionId +
                ", codeVerification='" + codeVerification + '\'' +
                '}';
    }
}