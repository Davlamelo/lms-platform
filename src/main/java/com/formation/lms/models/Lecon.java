package com.formation.lms.models;

import java.time.LocalDateTime;

/**
 * Leçon d'une section (vidéo, texte, quiz, ressource).
 * Correspond à la table 'lecons'.
 */
public class Lecon {

    private Long id;
    private Long sectionId;
    private String titre;
    private TypeLecon typeLecon;
    private String contenuTexte;
    private String videoUrl;
    private String ressourceUrl;
    private int dureeMin;
    private int ordre;
    private boolean estGratuite;
    private LocalDateTime dateCreation;

    public Lecon() {
    }

    public Lecon(Long sectionId, String titre, TypeLecon typeLecon, int ordre) {
        this.sectionId = sectionId;
        this.titre = titre;
        this.typeLecon = typeLecon;
        this.ordre = ordre;
        this.dureeMin = 0;
        this.estGratuite = false;
    }

    // === Getters & Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public TypeLecon getTypeLecon() {
        return typeLecon;
    }

    public void setTypeLecon(TypeLecon typeLecon) {
        this.typeLecon = typeLecon;
    }

    public String getContenuTexte() {
        return contenuTexte;
    }

    public void setContenuTexte(String contenuTexte) {
        this.contenuTexte = contenuTexte;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getRessourceUrl() {
        return ressourceUrl;
    }

    public void setRessourceUrl(String ressourceUrl) {
        this.ressourceUrl = ressourceUrl;
    }

    public int getDureeMin() {
        return dureeMin;
    }

    public void setDureeMin(int dureeMin) {
        this.dureeMin = dureeMin;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public boolean isEstGratuite() {
        return estGratuite;
    }

    public void setEstGratuite(boolean estGratuite) {
        this.estGratuite = estGratuite;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "Lecon{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", typeLecon=" + typeLecon +
                ", ordre=" + ordre +
                '}';
    }
}