package com.formation.lms.models;

import java.time.LocalDateTime;

/**
 * Message dans un fil de discussion.
 * Correspond à la table 'messages_discussion'.
 */
public class MessageDiscussion {

    private Long id;
    private Long filId;
    private Long auteurId;
    private String contenu;
    private boolean estReponseOfficielle;
    private LocalDateTime dateCreation;

    public MessageDiscussion() {
    }

    public MessageDiscussion(Long filId, Long auteurId, String contenu) {
        this.filId = filId;
        this.auteurId = auteurId;
        this.contenu = contenu;
        this.estReponseOfficielle = false;
    }

    // === Getters & Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFilId() {
        return filId;
    }

    public void setFilId(Long filId) {
        this.filId = filId;
    }

    public Long getAuteurId() {
        return auteurId;
    }

    public void setAuteurId(Long auteurId) {
        this.auteurId = auteurId;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public boolean isEstReponseOfficielle() {
        return estReponseOfficielle;
    }

    public void setEstReponseOfficielle(boolean estReponseOfficielle) {
        this.estReponseOfficielle = estReponseOfficielle;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "MessageDiscussion{" +
                "id=" + id +
                ", filId=" + filId +
                ", estReponseOfficielle=" + estReponseOfficielle +
                '}';
    }
}