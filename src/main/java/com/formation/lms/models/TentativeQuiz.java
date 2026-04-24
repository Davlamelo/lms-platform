package com.formation.lms.models;

import java.time.LocalDateTime;

/**
 * Tentative de passage d'un quiz par un apprenant.
 * Correspond à la table 'tentatives_quiz'.
 */
public class TentativeQuiz {

    private Long id;
    private Long inscriptionId;
    private Long quizId;
    private double score;
    private int pointsObtenus;
    private int pointsTotal;
    private boolean estReussi;
    private LocalDateTime dateTentative;

    public TentativeQuiz() {
    }

    public TentativeQuiz(Long inscriptionId, Long quizId) {
        this.inscriptionId = inscriptionId;
        this.quizId = quizId;
        this.score = 0.0;
        this.pointsObtenus = 0;
        this.pointsTotal = 0;
        this.estReussi = false;
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

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getPointsObtenus() {
        return pointsObtenus;
    }

    public void setPointsObtenus(int pointsObtenus) {
        this.pointsObtenus = pointsObtenus;
    }

    public int getPointsTotal() {
        return pointsTotal;
    }

    public void setPointsTotal(int pointsTotal) {
        this.pointsTotal = pointsTotal;
    }

    public boolean isEstReussi() {
        return estReussi;
    }

    public void setEstReussi(boolean estReussi) {
        this.estReussi = estReussi;
    }

    public LocalDateTime getDateTentative() {
        return dateTentative;
    }

    public void setDateTentative(LocalDateTime dateTentative) {
        this.dateTentative = dateTentative;
    }

    @Override
    public String toString() {
        return "TentativeQuiz{" +
                "id=" + id +
                ", quizId=" + quizId +
                ", score=" + score + "%" +
                ", estReussi=" + estReussi +
                '}';
    }
}