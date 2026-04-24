package com.formation.lms.models;

import java.time.LocalDateTime;

/**
 * Inscription d'un apprenant à un cours.
 * Table associative entre utilisateurs et cours.
 * Correspond à la table 'inscriptions'.
 */
public class Inscription {

    private Long id;
    private Long apprenantId;
    private Long coursId;
    private double pourcentageProgression;
    private StatutInscription statut;
    private LocalDateTime dateInscription;
    private LocalDateTime dateCompletion;
    private LocalDateTime derniereActivite;

    public Inscription() {
    }

    public Inscription(Long apprenantId, Long coursId) {
        this.apprenantId = apprenantId;
        this.coursId = coursId;
        this.pourcentageProgression = 0.0;
        this.statut = StatutInscription.EN_COURS;
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

    public double getPourcentageProgression() {
        return pourcentageProgression;
    }

    public void setPourcentageProgression(double pourcentageProgression) {
        this.pourcentageProgression = pourcentageProgression;
    }

    public StatutInscription getStatut() {
        return statut;
    }

    public void setStatut(StatutInscription statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }

    public LocalDateTime getDateCompletion() {
        return dateCompletion;
    }

    public void setDateCompletion(LocalDateTime dateCompletion) {
        this.dateCompletion = dateCompletion;
    }

    public LocalDateTime getDerniereActivite() {
        return derniereActivite;
    }

    public void setDerniereActivite(LocalDateTime derniereActivite) {
        this.derniereActivite = derniereActivite;
    }

    @Override
    public String toString() {
        return "Inscription{" +
                "id=" + id +
                ", apprenantId=" + apprenantId +
                ", coursId=" + coursId +
                ", progression=" + pourcentageProgression + "%" +
                ", statut=" + statut +
                '}';
    }
}