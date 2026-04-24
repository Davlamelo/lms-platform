-- ============================================================================
-- LMS Platform - Schéma de base de données
-- Auteur : Tassembedo Ulrich David
-- ============================================================================

USE db_lms;

-- Désactive temporairement les contraintes pour permettre un drop propre
SET FOREIGN_KEY_CHECKS = 0;

-- Suppression des tables si elles existent (utile pour reset en dev)
DROP TABLE IF EXISTS certificats;
DROP TABLE IF EXISTS messages_discussion;
DROP TABLE IF EXISTS fils_discussion;
DROP TABLE IF EXISTS avis;
DROP TABLE IF EXISTS tentatives_quiz;
DROP TABLE IF EXISTS progression_lecons;
DROP TABLE IF EXISTS inscriptions;
DROP TABLE IF EXISTS reponses;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS quiz;
DROP TABLE IF EXISTS lecons;
DROP TABLE IF EXISTS sections;
DROP TABLE IF EXISTS cours;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS candidatures_instructeur;
DROP TABLE IF EXISTS utilisateurs;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- 1. UTILISATEURS
-- ============================================================================
-- Table centrale : tous les acteurs de la plateforme (apprenants, instructeurs, admins)
-- Le rôle est un ENUM car les valeurs sont fixes et peu nombreuses

CREATE TABLE utilisateurs (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    email           VARCHAR(150) NOT NULL UNIQUE,
    mot_de_passe    VARCHAR(255) NOT NULL,              -- hash BCrypt (60 chars mais on prévoit)
    prenom          VARCHAR(100) NOT NULL,
    nom             VARCHAR(100) NOT NULL,
    avatar_url      VARCHAR(500),                        -- chemin vers photo de profil
    biographie      TEXT,                                -- description de l'utilisateur
    role            ENUM('APPRENANT', 'INSTRUCTEUR', 'ADMIN') NOT NULL DEFAULT 'APPRENANT',
    actif           BOOLEAN NOT NULL DEFAULT TRUE,       -- false = compte suspendu par admin
    date_creation   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_maj        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_email (email),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 2. CANDIDATURES INSTRUCTEUR
-- ============================================================================
-- Un apprenant peut postuler pour devenir instructeur, l'admin valide/rejette

CREATE TABLE candidatures_instructeur (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id  BIGINT NOT NULL,
    motivation      TEXT NOT NULL,                       -- texte de candidature
    expertise       VARCHAR(255) NOT NULL,               -- domaines d'expertise
    statut          ENUM('EN_ATTENTE', 'APPROUVEE', 'REJETEE') NOT NULL DEFAULT 'EN_ATTENTE',
    commentaire_admin TEXT,                              -- feedback de l'admin
    date_soumission DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_traitement DATETIME,                            -- NULL tant que pas traitée

    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_statut (statut)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 3. CATEGORIES
-- ============================================================================
-- Catégories de cours (Data Science, Web Dev, etc.)

CREATE TABLE categories (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom             VARCHAR(100) NOT NULL UNIQUE,
    slug            VARCHAR(120) NOT NULL UNIQUE,        -- version URL-friendly (ex: "data-science")
    description     TEXT,
    icone           VARCHAR(100),                         -- nom d'icône FontAwesome par exemple
    date_creation   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 4. COURS
-- ============================================================================
-- Un cours est créé par un instructeur et appartient à une catégorie

CREATE TABLE cours (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    titre               VARCHAR(200) NOT NULL,
    slug                VARCHAR(220) NOT NULL UNIQUE,
    description_courte  VARCHAR(500),                    -- tagline du cours
    description_longue  TEXT,                            -- description complète
    miniature_url       VARCHAR(500),                    -- image de couverture
    video_promo_url     VARCHAR(500),                    -- vidéo de présentation
    niveau              ENUM('DEBUTANT', 'INTERMEDIAIRE', 'AVANCE') NOT NULL DEFAULT 'DEBUTANT',
    langue              VARCHAR(10) NOT NULL DEFAULT 'fr',
    duree_totale_min    INT NOT NULL DEFAULT 0,          -- calculé depuis les leçons
    statut              ENUM('BROUILLON', 'EN_ATTENTE_VALIDATION', 'PUBLIE', 'REJETE', 'ARCHIVE')
                        NOT NULL DEFAULT 'BROUILLON',

    instructeur_id      BIGINT NOT NULL,
    categorie_id        BIGINT NOT NULL,

    date_creation       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_publication    DATETIME,                        -- NULL tant que non publié
    date_maj            DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (instructeur_id) REFERENCES utilisateurs(id) ON DELETE RESTRICT,
    FOREIGN KEY (categorie_id) REFERENCES categories(id) ON DELETE RESTRICT,

    INDEX idx_statut (statut),
    INDEX idx_instructeur (instructeur_id),
    INDEX idx_categorie (categorie_id),
    FULLTEXT INDEX idx_recherche (titre, description_courte, description_longue)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 5. SECTIONS
-- ============================================================================
-- Un cours est divisé en sections (chapitres)

CREATE TABLE sections (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    cours_id        BIGINT NOT NULL,
    titre           VARCHAR(200) NOT NULL,
    description     TEXT,
    ordre           INT NOT NULL DEFAULT 0,              -- ordre d'affichage dans le cours
    date_creation   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE,
    INDEX idx_cours_ordre (cours_id, ordre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 6. LECONS
-- ============================================================================
-- Une section contient plusieurs leçons de différents types

CREATE TABLE lecons (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    section_id      BIGINT NOT NULL,
    titre           VARCHAR(200) NOT NULL,
    type_lecon      ENUM('VIDEO', 'TEXTE', 'QUIZ', 'RESSOURCE') NOT NULL,
    contenu_texte   LONGTEXT,                            -- pour leçons TEXTE (markdown/HTML)
    video_url       VARCHAR(500),                         -- pour leçons VIDEO
    ressource_url   VARCHAR(500),                         -- pour leçons RESSOURCE (PDF, ZIP...)
    duree_min       INT NOT NULL DEFAULT 0,              -- durée estimée en minutes
    ordre           INT NOT NULL DEFAULT 0,
    est_gratuite    BOOLEAN NOT NULL DEFAULT FALSE,      -- prévisible sans inscription
    date_creation   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (section_id) REFERENCES sections(id) ON DELETE CASCADE,
    INDEX idx_section_ordre (section_id, ordre),
    INDEX idx_type (type_lecon)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 7. QUIZ
-- ============================================================================
-- Un quiz est lié à une leçon de type QUIZ (relation 1-1)

CREATE TABLE quiz (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    lecon_id            BIGINT NOT NULL UNIQUE,          -- UNIQUE = relation 1-1
    titre               VARCHAR(200) NOT NULL,
    description         TEXT,
    score_minimum       INT NOT NULL DEFAULT 70,         -- % pour valider le quiz
    duree_limite_min    INT,                             -- NULL = pas de limite
    date_creation       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (lecon_id) REFERENCES lecons(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 8. QUESTIONS
-- ============================================================================

CREATE TABLE questions (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    quiz_id         BIGINT NOT NULL,
    enonce          TEXT NOT NULL,
    type_question   ENUM('QCM_UNIQUE', 'QCM_MULTIPLE', 'VRAI_FAUX') NOT NULL DEFAULT 'QCM_UNIQUE',
    points          INT NOT NULL DEFAULT 1,
    ordre           INT NOT NULL DEFAULT 0,

    FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE,
    INDEX idx_quiz_ordre (quiz_id, ordre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 9. REPONSES
-- ============================================================================
-- Réponses possibles pour une question

CREATE TABLE reponses (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id     BIGINT NOT NULL,
    texte           TEXT NOT NULL,
    est_correcte    BOOLEAN NOT NULL DEFAULT FALSE,
    ordre           INT NOT NULL DEFAULT 0,

    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    INDEX idx_question (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 10. INSCRIPTIONS
-- ============================================================================
-- Relation N-N entre utilisateurs et cours, avec attributs propres

CREATE TABLE inscriptions (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    apprenant_id        BIGINT NOT NULL,
    cours_id            BIGINT NOT NULL,
    pourcentage_progression DECIMAL(5,2) NOT NULL DEFAULT 0.00,  -- 0.00 à 100.00
    statut              ENUM('EN_COURS', 'TERMINE', 'ABANDONNE') NOT NULL DEFAULT 'EN_COURS',
    date_inscription    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_completion     DATETIME,                        -- NULL tant que pas terminé
    derniere_activite   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (apprenant_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE,

    UNIQUE KEY uk_apprenant_cours (apprenant_id, cours_id),  -- pas de double inscription
    INDEX idx_apprenant (apprenant_id),
    INDEX idx_cours (cours_id),
    INDEX idx_statut (statut)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 11. PROGRESSION_LECONS
-- ============================================================================
-- Trace les leçons complétées par un apprenant dans une inscription

CREATE TABLE progression_lecons (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    inscription_id  BIGINT NOT NULL,
    lecon_id        BIGINT NOT NULL,
    est_completee   BOOLEAN NOT NULL DEFAULT FALSE,
    date_completion DATETIME,
    temps_passe_sec INT NOT NULL DEFAULT 0,              -- temps réel passé sur la leçon

    FOREIGN KEY (inscription_id) REFERENCES inscriptions(id) ON DELETE CASCADE,
    FOREIGN KEY (lecon_id) REFERENCES lecons(id) ON DELETE CASCADE,

    UNIQUE KEY uk_inscription_lecon (inscription_id, lecon_id),
    INDEX idx_inscription (inscription_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 12. TENTATIVES_QUIZ
-- ============================================================================
-- Chaque passage de quiz par un apprenant

CREATE TABLE tentatives_quiz (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    inscription_id  BIGINT NOT NULL,
    quiz_id         BIGINT NOT NULL,
    score           DECIMAL(5,2) NOT NULL DEFAULT 0.00,  -- score en %
    points_obtenus  INT NOT NULL DEFAULT 0,
    points_total    INT NOT NULL DEFAULT 0,
    est_reussi      BOOLEAN NOT NULL DEFAULT FALSE,
    date_tentative  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (inscription_id) REFERENCES inscriptions(id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE,

    INDEX idx_inscription (inscription_id),
    INDEX idx_quiz (quiz_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 13. AVIS
-- ============================================================================
-- Notes et commentaires sur les cours

CREATE TABLE avis (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    apprenant_id    BIGINT NOT NULL,
    cours_id        BIGINT NOT NULL,
    note            TINYINT NOT NULL CHECK (note BETWEEN 1 AND 5),  -- 1 à 5 étoiles
    commentaire     TEXT,
    date_creation   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_maj        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (apprenant_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE,

    UNIQUE KEY uk_apprenant_cours (apprenant_id, cours_id),  -- 1 seul avis par cours
    INDEX idx_cours (cours_id),
    INDEX idx_note (note)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 14. FILS_DISCUSSION (Q&R)
-- ============================================================================
-- Threads de discussion par cours

CREATE TABLE fils_discussion (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    cours_id        BIGINT NOT NULL,
    auteur_id       BIGINT NOT NULL,
    titre           VARCHAR(300) NOT NULL,
    contenu         TEXT NOT NULL,
    est_resolu      BOOLEAN NOT NULL DEFAULT FALSE,
    date_creation   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE,
    FOREIGN KEY (auteur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_cours (cours_id),
    INDEX idx_auteur (auteur_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 15. MESSAGES_DISCUSSION
-- ============================================================================
-- Réponses dans un fil de discussion

CREATE TABLE messages_discussion (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    fil_id              BIGINT NOT NULL,
    auteur_id           BIGINT NOT NULL,
    contenu             TEXT NOT NULL,
    est_reponse_officielle BOOLEAN NOT NULL DEFAULT FALSE,  -- marquée par l'instructeur
    date_creation       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (fil_id) REFERENCES fils_discussion(id) ON DELETE CASCADE,
    FOREIGN KEY (auteur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_fil (fil_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 16. CERTIFICATS
-- ============================================================================
-- Certificats générés quand inscription à 100%

CREATE TABLE certificats (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    inscription_id      BIGINT NOT NULL UNIQUE,          -- 1 certificat par inscription
    code_verification   VARCHAR(50) NOT NULL UNIQUE,     -- code unique pour vérifier l'authenticité
    fichier_pdf_url     VARCHAR(500) NOT NULL,            -- chemin sur disque
    date_emission       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (inscription_id) REFERENCES inscriptions(id) ON DELETE CASCADE,
    INDEX idx_code (code_verification)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- Fin du schéma
-- ============================================================================