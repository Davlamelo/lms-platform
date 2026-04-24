package com.formation.lms.models;

import java.time.LocalDateTime;

/**
 * Catégorie de cours (Data Science, Web Dev, etc.).
 * Correspond à la table 'categories'.
 */
public class Categorie {

    private Long id;
    private String nom;
    private String slug;
    private String description;
    private String icone;
    private LocalDateTime dateCreation;

    public Categorie() {
    }

    public Categorie(String nom, String slug, String description) {
        this.nom = nom;
        this.slug = slug;
        this.description = description;
    }

    // === Getters & Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}