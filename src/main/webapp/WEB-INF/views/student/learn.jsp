<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${cours.titre} - LMS Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
          rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
          rel="stylesheet">
    <style>
        * { box-sizing: border-box; }

        body {
            background-color: #0f1117;
            color: #e2e8f0;
            font-family: 'Segoe UI', sans-serif;
            margin: 0;
            height: 100vh;
            overflow: hidden;
            display: flex;
            flex-direction: column;
        }

        /* ===== TOPBAR ===== */
        .topbar {
            background: #1a1f2e;
            border-bottom: 1px solid #2d3748;
            padding: 12px 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            flex-shrink: 0;
            z-index: 100;
        }
        .topbar .cours-titre {
            font-weight: 600;
            font-size: 1rem;
            color: #e2e8f0;
        }
        .topbar a {
            color: #a0aec0;
            text-decoration: none;
            font-size: 0.9rem;
        }
        .topbar a:hover { color: #e2e8f0; }

        /* ===== LAYOUT PRINCIPAL ===== */
        .learn-layout {
            display: flex;
            flex: 1;
            overflow: hidden;
        }

        /* ===== SIDEBAR ===== */
        .sidebar {
            width: 340px;
            min-width: 280px;
            background: #1a1f2e;
            border-right: 1px solid #2d3748;
            overflow-y: auto;
            flex-shrink: 0;
        }

        .sidebar-header {
            padding: 16px 20px;
            border-bottom: 1px solid #2d3748;
        }

        .progression-label {
            font-size: 0.8rem;
            color: #a0aec0;
            margin-bottom: 6px;
        }

        .progress-bar-custom {
            height: 6px;
            background: #2d3748;
            border-radius: 3px;
            overflow: hidden;
        }
        .progress-bar-custom .fill {
            height: 100%;
            background: linear-gradient(90deg, #5a2d82, #8e44ad);
            border-radius: 3px;
            transition: width 0.3s ease;
        }

        .sidebar-section-title {
            padding: 12px 20px 6px;
            font-size: 0.75rem;
            font-weight: 700;
            color: #a0aec0;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }

        .section-header {
            padding: 10px 20px;
            font-size: 0.85rem;
            font-weight: 600;
            color: #cbd5e0;
            background: #242938;
            border-top: 1px solid #2d3748;
            border-bottom: 1px solid #2d3748;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .lecon-item {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 10px 20px 10px 28px;
            cursor: pointer;
            text-decoration: none;
            color: #a0aec0;
            font-size: 0.85rem;
            border-left: 3px solid transparent;
            transition: all 0.15s;
        }
        .lecon-item:hover {
            background: #242938;
            color: #e2e8f0;
        }
        .lecon-item.active {
            background: #2d3748;
            border-left-color: #8e44ad;
            color: #e2e8f0;
        }
        .lecon-item.completee {
            color: #68d391;
        }
        .lecon-item .lecon-titre {
            flex: 1;
            line-height: 1.3;
        }
        .lecon-item .lecon-duree {
            font-size: 0.75rem;
            color: #718096;
            white-space: nowrap;
        }
        .lecon-item .lecon-icon {
            font-size: 1rem;
            width: 20px;
            text-align: center;
            flex-shrink: 0;
        }

        /* ===== CONTENU PRINCIPAL ===== */
        .main-content {
            flex: 1;
            overflow-y: auto;
            padding: 0;
        }

        .content-card {
            padding: 40px;
            max-width: 900px;
            margin: 0 auto;
        }

        .content-card h2 {
            font-size: 1.8rem;
            font-weight: 700;
            margin-bottom: 24px;
            color: #f7fafc;
        }

        .lecon-body {
            line-height: 1.8;
            color: #cbd5e0;
            font-size: 1rem;
        }
        .lecon-body h1, .lecon-body h2, .lecon-body h3 {
            color: #f7fafc;
            margin-top: 1.5em;
        }
        .lecon-body ul, .lecon-body ol {
            padding-left: 1.5em;
        }
        .lecon-body p { margin-bottom: 1em; }

        /* ===== BOUTON COMPLÉTER ===== */
        .nav-buttons {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            padding: 24px 0;
            border-top: 1px solid #2d3748;
            margin-top: 32px;
        }

        .btn-complete {
            background: linear-gradient(135deg, #48bb78, #38a169);
            color: white;
            border: none;
            padding: 12px 28px;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 8px;
            transition: opacity 0.2s;
        }
        .btn-complete:hover { opacity: 0.9; }
        .btn-complete.deja-complete {
            background: #2d3748;
            color: #68d391;
            cursor: default;
        }

        /* ===== QUIZ ===== */
        .quiz-card {
            background: #1e2535;
            border: 1px solid #2d3748;
            border-radius: 12px;
            padding: 24px;
            margin-bottom: 20px;
        }
        .quiz-card .enonce {
            font-size: 1.05rem;
            font-weight: 600;
            color: #f7fafc;
            margin-bottom: 16px;
        }
        .quiz-card .form-check-label {
            color: #cbd5e0;
            cursor: pointer;
        }
        .quiz-card .form-check-input:checked + .form-check-label {
            color: #fff;
            font-weight: 600;
        }
        .btn-submit-quiz {
            background: #8e44ad;
            color: white;
            border: none;
            padding: 14px 40px;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.2s;
        }
        .btn-submit-quiz:hover { background: #7d3c98; }

        /* ===== WELCOME ===== */
        .welcome-card {
            text-align: center;
            padding: 80px 40px;
        }
        .welcome-card .icon {
            font-size: 5rem;
            color: #8e44ad;
            margin-bottom: 24px;
        }
    </style>
</head>
<body>

<!-- TOPBAR -->
<div class="topbar">
    <a href="${pageContext.request.contextPath}/student/dashboard">
        <i class="bi bi-arrow-left"></i> Retour
    </a>
    <span class="cours-titre">${cours.titre}</span>
    <a href="${pageContext.request.contextPath}/qa?coursId=${cours.id}">
        <i class="bi bi-chat-dots"></i> Q&R
    </a>
</div>

<!-- LAYOUT PRINCIPAL -->
<div class="learn-layout">

    <!-- SIDEBAR -->
    <div class="sidebar">

        <!-- Progression -->
        <div class="sidebar-header">
            <div class="progression-label">
                Progression :
                <fmt:formatNumber value="${inscription.pourcentageProgression}"
                                  maxFractionDigits="0"/>%
            </div>
            <div class="progress-bar-custom">
                <div class="fill"
                     style="width: ${inscription.pourcentageProgression}%">
                </div>
            </div>
        </div>

        <div class="sidebar-section-title">Contenu du cours</div>

        <!-- Sections et leçons -->
        <c:forEach var="section" items="${sections}">

            <div class="section-header">
                <i class="bi bi-chevron-down" style="font-size: 0.75rem;"></i>
                ${section.titre}
            </div>

            <c:forEach var="lecon" items="${leconsParSection[section.id]}">
                <c:set var="estCompletee"
                       value="${leconsCompletees.contains(lecon.id)}"/>
                <c:set var="estActive"
                       value="${not empty leconActive && leconActive.id == lecon.id}"/>

                <a href="${pageContext.request.contextPath}/student/learn?coursId=${cours.id}&leconId=${lecon.id}"
                   class="lecon-item
                          ${estActive ? 'active' : ''}
                          ${estCompletee ? 'completee' : ''}">

                    <!-- Icône selon le type -->
                    <span class="lecon-icon">
                        <c:choose>
                            <c:when test="${estCompletee}">
                                <i class="bi bi-check-circle-fill text-success"></i>
                            </c:when>
                            <c:when test="${lecon.typeLecon == 'VIDEO'}">
                                <i class="bi bi-play-circle text-primary"></i>
                            </c:when>
                            <c:when test="${lecon.typeLecon == 'QUIZ'}">
                                <i class="bi bi-question-circle text-warning"></i>
                            </c:when>
                            <c:when test="${lecon.typeLecon == 'RESSOURCE'}">
                                <i class="bi bi-file-earmark text-info"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="bi bi-file-text text-secondary"></i>
                            </c:otherwise>
                        </c:choose>
                    </span>

                    <span class="lecon-titre">${lecon.titre}</span>
                    <span class="lecon-duree">${lecon.dureeMin}min</span>
                </a>
            </c:forEach>
        </c:forEach>
    </div>
    <!-- Fin sidebar -->

    <!-- CONTENU PRINCIPAL -->
    <div class="main-content">
        <c:choose>

            <%-- Aucune leçon disponible --%>
            <c:when test="${empty leconActive}">
                <div class="welcome-card">
                    <div class="icon">
                        <i class="bi bi-collection-play"></i>
                    </div>
                    <h3>Bienvenue dans ce cours !</h3>
                    <p class="text-muted">
                        Sélectionnez une leçon dans le menu de gauche
                        pour commencer.
                    </p>
                </div>
            </c:when>

            <%-- LEÇON QUIZ --%>
            <c:when test="${leconActive.typeLecon == 'QUIZ'}">
                <div class="content-card">
                    <h2>
                        <i class="bi bi-question-circle text-warning"></i>
                        ${leconActive.titre}
                    </h2>

                    <c:choose>

                        <%-- Quiz introuvable en BDD --%>
                        <c:when test="${empty quizActif}">
                            <div class="alert alert-warning">
                                <i class="bi bi-exclamation-triangle"></i>
                                Quiz non configuré. L'instructeur doit créer
                                le quiz depuis l'éditeur de curriculum.
                            </div>
                        </c:when>

                        <%-- Quiz sans questions --%>
                        <c:when test="${not empty quizActif && empty questions}">
                            <div class="alert alert-warning">
                                <i class="bi bi-exclamation-triangle"></i>
                                Ce quiz n'a pas encore de questions.
                                L'instructeur doit les ajouter.
                            </div>
                        </c:when>

                        <%-- Quiz avec questions → formulaire --%>
                        <c:otherwise>
                            <p class="text-muted mb-4">
                                <i class="bi bi-info-circle"></i>
                                Score minimum pour valider :
                                <strong>${quizActif.scoreMinimum}%</strong>
                                •
                                <strong>${questions.size()}</strong> question(s)
                            </p>

                            <form method="post"
                                  action="${pageContext.request.contextPath}/student/submit-quiz">
                                <input type="hidden" name="quizId"
                                       value="${quizActif.id}">
                                <input type="hidden" name="inscriptionId"
                                       value="${inscription.id}">
                                <input type="hidden" name="leconId"
                                       value="${leconActive.id}">
                                <input type="hidden" name="coursId"
                                       value="${cours.id}">

                                <c:forEach var="question" items="${questions}"
                                           varStatus="qIdx">
                                    <div class="quiz-card">
                                        <div class="enonce">
                                            Q${qIdx.index + 1}.
                                            <c:out value="${question.enonce}"/>
                                            <span class="badge bg-secondary ms-2">
                                                ${question.points} pt(s)
                                            </span>
                                        </div>
                                        <c:forEach var="reponse"
                                                   items="${reponsesParQuestion[question.id]}">
                                            <div class="form-check mb-2">
                                                <input class="form-check-input"
                                                       type="radio"
                                                       name="question_${question.id}"
                                                       value="${reponse.id}"
                                                       id="rep_${reponse.id}"
                                                       required>
                                                <label class="form-check-label"
                                                       for="rep_${reponse.id}">
                                                    <c:out value="${reponse.texte}"/>
                                                </label>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:forEach>

                                <button type="submit" class="btn-submit-quiz">
                                    <i class="bi bi-send"></i>
                                    Soumettre le quiz
                                </button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:when>

            <%-- TOUTES LES AUTRES LEÇONS (multi-contenus) --%>
            <c:otherwise>
                <div class="content-card">
                    <h2>${leconActive.titre}</h2>

                    <%-- 1. VIDÉO (si disponible) --%>
                    <c:if test="${not empty leconActive.videoUrl}">
                        <div class="mb-4">
                            <c:choose>
                                <%-- YouTube ou Vimeo → iframe --%>
                                <c:when test="${not empty embedUrl}">
                                    <div class="ratio ratio-16x9 rounded overflow-hidden">
                                        <iframe src="${embedUrl}"
                                                title="${leconActive.titre}"
                                                allowfullscreen
                                                allow="accelerometer; autoplay;
                                                       clipboard-write; encrypted-media;
                                                       gyroscope; picture-in-picture">
                                        </iframe>
                                    </div>
                                </c:when>
                                <%-- Fichier MP4/WebM direct --%>
                                <c:when test="${estVideoDirecte}">
                                    <video controls class="w-100 rounded"
                                           style="max-height: 450px; background: #000;">
                                        <source src="${leconActive.videoUrl}"
                                                type="video/mp4">
                                        Votre navigateur ne supporte pas la
                                        lecture vidéo.
                                    </video>
                                </c:when>
                                <%-- URL non reconnue --%>
                                <c:otherwise>
                                    <div class="alert alert-warning">
                                        <i class="bi bi-exclamation-triangle"></i>
                                        <strong>URL vidéo non reconnue.</strong><br>
                                        Utilisez une URL YouTube, Vimeo, ou un
                                        fichier MP4 direct.<br>
                                        <small>URL actuelle :
                                            <code>${leconActive.videoUrl}</code>
                                        </small>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>

                    <%-- 2. CONTENU TEXTE (si disponible) --%>
                    <c:if test="${not empty leconActive.contenuTexte}">
                        <div class="lecon-body mb-4">
                            ${leconActive.contenuTexte}
                        </div>
                    </c:if>

                    <%-- 3. RESSOURCE (si disponible) --%>
                    <c:if test="${not empty leconActive.ressourceUrl}">
                        <div class="card mb-4"
                             style="background: rgba(255,255,255,0.05);
                                    border: 1px solid #2d3748;">
                            <div class="card-body d-flex align-items-center gap-3">
                                <i class="bi bi-file-earmark-arrow-down
                                          fs-1 text-info"></i>
                                <div class="flex-grow-1">
                                    <h6 class="text-white mb-1">
                                        Ressource à télécharger
                                    </h6>
                                    <small class="text-muted">
                                        Fichier complémentaire pour cette leçon
                                    </small>
                                </div>
                                <c:choose>
                                    <c:when test="${leconActive.ressourceUrl.startsWith('uploads/')}">
                                        <a href="${pageContext.request.contextPath}/${leconActive.ressourceUrl}"
                                           class="btn btn-info" target="_blank">
                                            <i class="bi bi-eye"></i> Voir
                                        </a>
                                        <a href="${pageContext.request.contextPath}/${leconActive.ressourceUrl}"
                                           class="btn btn-outline-info" download>
                                            <i class="bi bi-download"></i>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${leconActive.ressourceUrl}"
                                           class="btn btn-info" target="_blank">
                                            <i class="bi bi-box-arrow-up-right"></i>
                                            Ouvrir
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </c:if>

                    <%-- Aucun contenu --%>
                    <c:if test="${empty leconActive.contenuTexte
                                  && empty leconActive.videoUrl
                                  && empty leconActive.ressourceUrl}">
                        <div class="alert alert-secondary">
                            <i class="bi bi-info-circle"></i>
                            Contenu de cette leçon en cours de préparation.
                        </div>
                    </c:if>

                    <%-- Bouton marquer complétée --%>
                    <div class="nav-buttons">
                        <c:choose>
                            <c:when test="${leconsCompletees.contains(leconActive.id)}">
                                <button class="btn-complete deja-complete"
                                        disabled>
                                    <i class="bi bi-check-circle-fill"></i>
                                    Leçon complétée
                                </button>
                            </c:when>
                            <c:otherwise>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/student/complete-lesson">
                                    <input type="hidden" name="inscriptionId"
                                           value="${inscription.id}">
                                    <input type="hidden" name="leconId"
                                           value="${leconActive.id}">
                                    <input type="hidden" name="coursId"
                                           value="${cours.id}">
                                    <button type="submit" class="btn-complete">
                                        <i class="bi bi-check-circle"></i>
                                        Marquer comme complétée
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:otherwise>

        </c:choose>
    </div>
    <!-- Fin contenu principal -->

</div>
<!-- Fin layout -->

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>