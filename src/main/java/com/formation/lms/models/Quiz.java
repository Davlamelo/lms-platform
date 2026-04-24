package com.formation.lms.models;

import java.time.LocalDateTime;

/**
 * Quiz associé à une leçon de type QUIZ (relation 1-1).
 * Correspond à la table 'quiz'.
 */
public class Quiz {

    private Long id;
    private Long leconId;
    private String titre;
    private String description;
    private int scoreMinimum;
    private Integer dureeLimiteMin; // Integer car nullable (null = pas de limite)
    private LocalDateTime dateCreation;

    public Quiz() {
    }

    public Quiz(Long leconId, String titre, int scoreMinimum) {
        this.leconId = leconId;
        this.titre = titre;
        this.scoreMinimum = scoreMinimum;
    }

    // === Getters & Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLeconId() {
        return leconId;
    }

    public void setLeconId(Long leconId) {
        this.leconId = leconId;
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

    public int getScoreMinimum() {
        return scoreMinimum;
    }

    public void setScoreMinimum(int scoreMinimum) {
        this.scoreMinimum = scoreMinimum;
    }

    public Integer getDureeLimiteMin() {
        return dureeLimiteMin;
    }

    public void setDureeLimiteMin(Integer dureeLimiteMin) {
        this.dureeLimiteMin = dureeLimiteMin;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", leconId=" + leconId +
                ", titre='" + titre + '\'' +
                ", scoreMinimum=" + scoreMinimum +
                '}';
    }
}