package com.formation.lms.models;

import java.time.LocalDateTime;

/**
 * Candidature d'un apprenant pour devenir instructeur.
 * Correspond à la table 'candidatures_instructeur'.
 */
public class CandidatureInstructeur {

    private Long id;
    private Long utilisateurId;
    private String motivation;
    private String expertise;
    private String cvUrl;           // AJOUTÉ : chemin vers le fichier CV/portfolio uploadé
    private StatutCandidature statut;
    private String commentaireAdmin;
    private LocalDateTime dateSoumission;
    private LocalDateTime dateTraitement;

    public CandidatureInstructeur() {
    }

    public CandidatureInstructeur(Long utilisateurId, String motivation, String expertise) {
        this.utilisateurId = utilisateurId;
        this.motivation = motivation;
        this.expertise = expertise;
        this.statut = StatutCandidature.EN_ATTENTE;
    }

    // === Getters & Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    // AJOUTÉ
    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public StatutCandidature getStatut() {
        return statut;
    }

    public void setStatut(StatutCandidature statut) {
        this.statut = statut;
    }

    public String getCommentaireAdmin() {
        return commentaireAdmin;
    }

    public void setCommentaireAdmin(String commentaireAdmin) {
        this.commentaireAdmin = commentaireAdmin;
    }

    public LocalDateTime getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(LocalDateTime dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public LocalDateTime getDateTraitement() {
        return dateTraitement;
    }

    public void setDateTraitement(LocalDateTime dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    @Override
    public String toString() {
        return "CandidatureInstructeur{" +
                "id=" + id +
                ", utilisateurId=" + utilisateurId +
                ", statut=" + statut +
                '}';
    }
}
