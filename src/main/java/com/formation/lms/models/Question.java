package com.formation.lms.models;

/**
 * Question d'un quiz.
 * Correspond à la table 'questions'.
 */
public class Question {

    private Long id;
    private Long quizId;
    private String enonce;
    private TypeQuestion typeQuestion;
    private int points;
    private int ordre;

    public Question() {
    }

    public Question(Long quizId, String enonce, TypeQuestion typeQuestion, int points, int ordre) {
        this.quizId = quizId;
        this.enonce = enonce;
        this.typeQuestion = typeQuestion;
        this.points = points;
        this.ordre = ordre;
    }

    // === Getters & Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getEnonce() {
        return enonce;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public TypeQuestion getTypeQuestion() {
        return typeQuestion;
    }

    public void setTypeQuestion(TypeQuestion typeQuestion) {
        this.typeQuestion = typeQuestion;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", enonce='" + enonce + '\'' +
                ", typeQuestion=" + typeQuestion +
                ", points=" + points +
                '}';
    }
}