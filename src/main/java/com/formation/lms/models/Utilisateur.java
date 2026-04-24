package com.formation.lms.models;

import java.time.LocalDateTime;

/**
 * Représente un utilisateur de la plateforme (apprenant, instructeur, admin).
 * Correspond à la table 'utilisateurs'.
 */
public class Utilisateur {

    private Long id;
    private String email;
    private String motDePasse;
    private String prenom;
    private String nom;
    private String avatarUrl;
    private String biographie;
    private Role role;
    private boolean actif;
    private LocalDateTime dateCreation;
    private LocalDateTime dateMaj;

    // === Constructeur vide (obligatoire pour les frameworks et JDBC) ===
    public Utilisateur() {
    }

    // === Constructeur pour inscription (sans id, sans dates) ===
    public Utilisateur(String email, String motDePasse, String prenom, String nom, Role role) {
        this.email = email;
        this.motDePasse = motDePasse;
        this.prenom = prenom;
        this.nom = nom;
        this.role = role;
        this.actif = true;
    }

    // === Méthodes métier ===
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    // === Getters & Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBiographie() {
        return biographie;
    }

    public void setBiographie(String biographie) {
        this.biographie = biographie;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
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
        return "Utilisateur{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nom='" + getNomComplet() + '\'' +
                ", role=" + role +
                ", actif=" + actif +
                '}';
    }
}