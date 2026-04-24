package com.formation.lms.models;

import java.time.LocalDateTime;

/**
 * Fil de discussion (Q&R) sur un cours.
 * Correspond à la table 'fils_discussion'.
 */
public class FilDiscussion {

    private Long id;
    private Long coursId;
    private Long auteurId;
    private String titre;
    private String contenu;
    private boolean estResolu;
    private LocalDateTime dateCreation;

    public FilDiscussion() {
    }

    public FilDiscussion(Long coursId, Long auteurId, String titre, String contenu) {
        this.coursId = coursId;
        this.auteurId = auteurId;
        this.titre = titre;
        this.contenu = contenu;
        this.estResolu = false;
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

    public Long getAuteurId() {
        return auteurId;
    }

    public void setAuteurId(Long auteurId) {
        this.auteurId = auteurId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public boolean isEstResolu() {
        return estResolu;
    }

    public void setEstResolu(boolean estResolu) {
        this.estResolu = estResolu;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "FilDiscussion{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", estResolu=" + estResolu +
                '}';
    }
}