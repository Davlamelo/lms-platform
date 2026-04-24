package com.formation.lms.models;

import java.time.LocalDateTime;

/**
 * Avis (note + commentaire) d'un apprenant sur un cours.
 * Correspond à la table 'avis'.
 */
public class Avis {

    private Long id;
    private Long apprenantId;
    private Long coursId;
    private int note; // 1 à 5
    private String commentaire;
    private LocalDateTime dateCreation;
    private LocalDateTime dateMaj;

    public Avis() {
    }

    public Avis(Long apprenantId, Long coursId, int note, String commentaire) {
        this.apprenantId = apprenantId;
        this.coursId = coursId;
        this.note = note;
        this.commentaire = commentaire;
    }

    // === Getters & Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApprenantId() {
        return apprenantId;
    }

    public void setApprenantId(Long apprenantId) {
        this.apprenantId = apprenantId;
    }

    public Long getCoursId() {
        return coursId;
    }

    public void setCoursId(Long coursId) {
        this.coursId = coursId;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateMaj() {
        return dateMaj;
    }

    public void setDateMaj(LocalDateTime dateMaj) {
        this.dateMaj = dateMaj;
    }

    @Override
    public String toString() {
        return "Avis{" +
                "id=" + id +
                ", coursId=" + coursId +
                ", note=" + note +
                '}';
    }
}