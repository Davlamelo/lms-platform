package com.formation.lms.models;

import java.time.LocalDateTime;

/**
 * Section (chapitre) d'un cours.
 * Correspond à la table 'sections'.
 */
public class Section {

    private Long id;
    private Long coursId;
    private String titre;
    private String description;
    private int ordre;
    private LocalDateTime dateCreation;

    public Section() {
    }

    public Section(Long coursId, String titre, int ordre) {
        this.coursId = coursId;
        this.titre = titre;
        this.ordre = ordre;
    }

    // === Getters & Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCoursId() {
        return coursId;
    }

    public void setCoursId(Long coursId) {
        this.coursId = coursId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", coursId=" + coursId +
                ", titre='" + titre + '\'' +
                ", ordre=" + ordre +
                '}';
    }
}