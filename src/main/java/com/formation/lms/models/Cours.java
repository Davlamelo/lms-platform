package com.formation.lms.models;

import java.time.LocalDateTime;

/**
 * Représente un cours de la plateforme.
 * Correspond à la table 'cours'.
 */
public class Cours {

    private Long id;
    private String titre;
    private String slug;
    private String descriptionCourte;
    private String descriptionLongue;
    private String miniatureUrl;
    private String videoPromoUrl;
    private NiveauCours niveau;
    private String langue;
    private int dureeTotaleMin;
    private StatutCours statut;

    // Clés étrangères (on stocke l'ID, pas l'objet entier)
    private Long instructeurId;
    private Long categorieId;

    private LocalDateTime dateCreation;
    private LocalDateTime datePublication;
    private LocalDateTime dateMaj;

    public Cours() {
    }

    public Cours(String titre, String slug, String descriptionCourte,
                 NiveauCours niveau, Long instructeurId, Long categorieId) {
        this.titre = titre;
        this.slug = slug;
        this.descriptionCourte = descriptionCourte;
        this.niveau = niveau;
        this.instructeurId = instructeurId;
        this.categorieId = categorieId;
        this.statut = StatutCours.BROUILLON;
        this.langue = "fr";
        this.dureeTotaleMin = 0;
    }

    // === Getters & Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescriptionCourte() {
        return descriptionCourte;
    }

    public void setDescriptionCourte(String descriptionCourte) {
        this.descriptionCourte = descriptionCourte;
    }

    public String getDescriptionLongue() {
        return descriptionLongue;
    }

    public void setDescriptionLongue(String descriptionLongue) {
        this.descriptionLongue = descriptionLongue;
    }

    public String getMiniatureUrl() {
        return miniatureUrl;
    }

    public void setMiniatureUrl(String miniatureUrl) {
        this.miniatureUrl = miniatureUrl;
    }

    public String getVideoPromoUrl() {
        return videoPromoUrl;
    }

    public void setVideoPromoUrl(String videoPromoUrl) {
        this.videoPromoUrl = videoPromoUrl;
    }

    public NiveauCours getNiveau() {
        return niveau;
    }

    public void setNiveau(NiveauCours niveau) {
        this.niveau = niveau;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public int getDureeTotaleMin() {
        return dureeTotaleMin;
    }

    public void setDureeTotaleMin(int dureeTotaleMin) {
        this.dureeTotaleMin = dureeTotaleMin;
    }

    public StatutCours getStatut() {
        return statut;
    }

    public void setStatut(StatutCours statut) {
        this.statut = statut;
    }

    public Long getInstructeurId() {
        return instructeurId;
    }

    public void setInstructeurId(Long instructeurId) {
        this.instructeurId = instructeurId;
    }

    public Long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDateTime datePublication) {
        this.datePublication = datePublication;
    }

    public LocalDateTime getDateMaj() {
        return dateMaj;
    }

    public void setDateMaj(LocalDateTime dateMaj) {
        this.dateMaj = dateMaj;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", niveau=" + niveau +
                ", statut=" + statut +
                ", instructeurId=" + instructeurId +
                '}';
    }
}