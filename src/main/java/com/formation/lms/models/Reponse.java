package com.formation.lms.models;

/**
 * Réponse possible à une question de quiz.
 * Correspond à la table 'reponses'.
 */
public class Reponse {

    private Long id;
    private Long questionId;
    private String texte;
    private boolean estCorrecte;
    private int ordre;

    public Reponse() {
    }

    public Reponse(Long questionId, String texte, boolean estCorrecte, int ordre) {
        this.questionId = questionId;
        this.texte = texte;
        this.estCorrecte = estCorrecte;
        this.ordre = ordre;
    }

    // === Getters & Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public boolean isEstCorrecte() {
        return estCorrecte;
    }

    public void setEstCorrecte(boolean estCorrecte) {
        this.estCorrecte = estCorrecte;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", texte='" + texte + '\'' +
                ", estCorrecte=" + estCorrecte +
                '}';
    }
}