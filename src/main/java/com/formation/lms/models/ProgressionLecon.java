package com.formation.lms.models;

import java.time.LocalDateTime;

/**
 * Suivi de progression d'un apprenant sur une leçon.
 * Correspond à la table 'progression_lecons'.
 */
public class ProgressionLecon {

    private Long id;
    private Long inscriptionId;
    private Long leconId;
    private boolean estCompletee;
    private LocalDateTime dateCompletion;
    private int tempsPasseSec;

    public ProgressionLecon() {
    }

    public ProgressionLecon(Long inscriptionId, Long leconId) {
        this.inscriptionId = inscriptionId;
        this.leconId = leconId;
        this.estCompletee = false;
        this.tempsPasseSec = 0;
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

    public Long getLeconId() {
        return leconId;
    }

    public void setLeconId(Long leconId) {
        this.leconId = leconId;
    }

    public boolean isEstCompletee() {
        return estCompletee;
    }

    public void setEstCompletee(boolean estCompletee) {
        this.estCompletee = estCompletee;
    }

    public LocalDateTime getDateCompletion() {
        return dateCompletion;
    }

    public void setDateCompletion(LocalDateTime dateCompletion) {
        this.dateCompletion = dateCompletion;
    }

    public int getTempsPasseSec() {
        return tempsPasseSec;
    }

    public void setTempsPasseSec(int tempsPasseSec) {
        this.tempsPasseSec = tempsPasseSec;
    }

    @Override
    public String toString() {
        return "ProgressionLecon{" +
                "id=" + id +
                ", inscriptionId=" + inscriptionId +
                ", leconId=" + leconId +
                ", estCompletee=" + estCompletee +
                '}';
    }
}