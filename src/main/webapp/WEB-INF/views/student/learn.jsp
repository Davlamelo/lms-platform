<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${cours.titre} - LMS Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body { background-color: #1a1a2e; color: #eee; }

        /* Topbar */
        .topbar {
            background: #16213e;
            border-bottom: 1px solid #0f3460;
            padding: 0.75rem 1.5rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
            position: fixed;
            top: 0; left: 0; right: 0;
            z-index: 1000;
        }
        .topbar a { color: #ccc; text-decoration: none; }
        .topbar a:hover { color: white; }
        .topbar .titre-cours {
            font-size: 0.95rem;
            font-weight: 600;
            color: white;
            max-width: 400px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        /* Layout principal */
        .learn-container {
            display: flex;
            margin-top: 56px;
            height: calc(100vh - 56px);
        }

        /* Sidebar */
        .sidebar {
            width: 320px;
            min-width: 320px;
            background: #16213e;
            border-right: 1px solid #0f3460;
            overflow-y: auto;
            height: 100%;
        }
        .sidebar-title {
            padding: 1rem;
            font-size: 0.9rem;
            font-weight: 700;
            color: #aaa;
            text-transform: uppercase;
            letter-spacing: 1px;
            border-bottom: 1px solid #0f3460;
        }
        .section-header {
            padding: 0.75rem 1rem;
            background: #0f3460;
            font-size: 0.85rem;
            font-weight: 600;
            color: #ddd;
            cursor: pointer;
        }
        .lecon-item {
            display: flex;
            align-items: center;
            padding: 0.6rem 1rem 0.6rem 1.5rem;
            font-size: 0.85rem;
            color: #bbb;
            cursor: pointer;
            text-decoration: none;
            border-bottom: 1px solid rgba(255,255,255,0.05);
            transition: background 0.2s;
        }
        .lecon-item:hover { background: rgba(255,255,255,0.05); color: white; }
        .lecon-item.active { background: rgba(99, 102, 241, 0.2); color: white; border-left: 3px solid #6366f1; }
        .lecon-item.completee { color: #22c55e; }
        .lecon-item .icone { width: 20px; margin-right: 0.5rem; flex-shrink: 0; }
        .lecon-item .duree { margin-left: auto; font-size: 0.75rem; color: #888; }

        /* Barre de progression */
        .progress-bar-sidebar {
            padding: 1rem;
            border-bottom: 1px solid #0f3460;
        }
        .progress-bar-sidebar .pct {
            font-size: 0.85rem;
            color: #aaa;
            margin-bottom: 0.4rem;
        }

        /* Zone contenu */
        .content-area {
            flex: 1;
            overflow-y: auto;
            padding: 2rem;
        }
        .content-card {
            background: #16213e;
            border-radius: 12px;
            padding: 2rem;
            max-width: 900px;
            margin: 0 auto;
        }
        .content-card h2 { color: white; margin-bottom: 1.5rem; }
        .content-card .lecon-body {
            color: #ddd;
            line-height: 1.8;
            font-size: 1rem;
        }
        .content-card .lecon-body h2,
        .content-card .lecon-body h3 { color: white; }
        .content-card .lecon-body pre {
            background: #0f3460;
            padding: 1rem;
            border-radius: 8px;
            overflow-x: auto;
        }
        .content-card .lecon-body code { color: #6ee7b7; }

        /* Boutons navigation */
        .nav-buttons {
            display: flex;
            justify-content: space-between;
            margin-top: 2rem;
            padding-top: 1.5rem;
            border-top: 1px solid #0f3460;
        }
        .btn-complete {
            background: #22c55e;
            color: white;
            border: none;
            padding: 0.75rem 2rem;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.2s;
        }
        .btn-complete:hover { background: #16a34a; }
        .btn-complete.deja-complete {
            background: #166534;
            cursor: default;
        }
        /* Quiz - texte en blanc */
        .card.bg-dark {
            color: white !important;
        }
        .card.bg-dark .form-check-label {
            color: white !important;
        }
        .card.bg-dark .fw-bold {
            color: white !important;
        }
        .form-check-input {
            border-color: #6366f1;
        }
    </style>
</head>
<body>

<!-- Topbar -->
<div class="topbar">
    <a href="${pageContext.request.contextPath}/student/dashboard">
        <i class="bi bi-arrow-left"></i> Retour
    </a>
    <span class="titre-cours">${cours.titre}</span>
    <div class="d-flex align-items-center gap-3">
        <span class="text-muted small">
            <fmt:formatNumber value="${inscription.pourcentageProgression}" maxFractionDigits="0"/>% complété
        </span>
        <a href="${pageContext.request.contextPath}/logout" class="text-muted small">
            <i class="bi bi-box-arrow-right"></i>
        </a>
        <a href="${pageContext.request.contextPath}/qa?coursId=${cours.id}"
           class="text-muted small">
            <i class="bi bi-chat-dots"></i> Q&R
        </a>
    </div>
</div>

<div class="learn-container">

    <!-- Sidebar -->
    <div class="sidebar">
        <!-- Progression globale -->
        <div class="progress-bar-sidebar">
            <div class="pct">
                Progression : <fmt:formatNumber value="${inscription.pourcentageProgression}"
                                                maxFractionDigits="0"/>%
            </div>
            <div class="progress" style="height: 6px;">
                <div class="progress-bar bg-success" style="width: ${inscription.pourcentageProgression}%"></div>
            </div>
        </div>

        <div class="sidebar-title">Contenu du cours</div>

        <!-- Sections et leçons -->
        <c:forEach var="section" items="${sections}" varStatus="sIdx">
            <div class="section-header">
                <i class="bi bi-chevron-down"></i>
                Section ${sIdx.index + 1} : ${section.titre}
            </div>

            <c:forEach var="lecon" items="${leconsParSection[section.id]}" varStatus="lIdx">
                <c:set var="estActive" value="${leconActive != null && leconActive.id == lecon.id}"/>
                <c:set var="estCompletee" value="${leconsCompletees.contains(lecon.id)}"/>

                <a href="${pageContext.request.contextPath}/student/learn?coursId=${cours.id}&leconId=${lecon.id}"
                   class="lecon-item ${estActive ? 'active' : ''} ${estCompletee ? 'completee' : ''}">

                    <span class="icone">
                        <c:choose>
                            <c:when test="${estCompletee}">
                                <i class="bi bi-check-circle-fill text-success"></i>
                            </c:when>
                            <%-- Leçon de type VIDEO --%>
                            <%-- Leçon de type VIDEO --%>
                            <c:when test="${leconActive.typeLecon == 'VIDEO'}">
                                <div class="content-card">
                                    <h2>${leconActive.titre}</h2>
                                    <c:choose>
                                        <%-- YouTube ou Vimeo (iframe) --%>
                                        <c:when test="${not empty embedUrl}">
                                            <div class="ratio ratio-16x9 mb-4 rounded overflow-hidden">
                                                <iframe src="${embedUrl}"
                                                        title="${leconActive.titre}"
                                                        allowfullscreen
                                                        allow="accelerometer; autoplay; clipboard-write;
                                                               encrypted-media; gyroscope; picture-in-picture">
                                                </iframe>
                                            </div>
                                        </c:when>
                                        <%-- Fichier vidéo direct (MP4) --%>
                                        <c:when test="${not empty leconActive.videoUrl}">
                                            <video controls class="w-100 rounded mb-4"
                                                   style="max-height: 450px; background: #000;">
                                                <source src="${leconActive.videoUrl}" type="video/mp4">
                                                Votre navigateur ne supporte pas la lecture vidéo.
                                            </video>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="alert alert-secondary">
                                                <i class="bi bi-camera-video-off"></i> Vidéo non disponible.
                                            </div>
                                        </c:otherwise>
                                    </c:choose>

                                    <div class="nav-buttons">
                                        <div></div>
                                        <c:choose>
                                            <c:when test="${leconsCompletees.contains(leconActive.id)}">
                                                <button class="btn-complete deja-complete" disabled>
                                                    <i class="bi bi-check-circle-fill"></i> Leçon complétée
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
                            </c:when>
                            <c:when test="${lecon.typeLecon == 'QUIZ'}">
                                <i class="bi bi-question-circle"></i>
                            </c:when>
                            <%-- Leçon de type RESSOURCE --%>
                            <c:when test="${leconActive.typeLecon == 'RESSOURCE'}">
                                <div class="content-card">
                                    <h2>${leconActive.titre}</h2>
                                    <c:choose>
                                        <c:when test="${not empty leconActive.ressourceUrl}">
                                            <c:choose>
                                                <%-- Fichier PDF uploadé localement --%>
                                                <c:when test="${leconActive.ressourceUrl.startsWith('uploads/')}">
                                                    <div class="text-center py-4">
                                                        <i class="bi bi-file-pdf text-danger"
                                                           style="font-size: 5rem;"></i>
                                                        <p class="mt-3">Document PDF disponible</p>
                                                        <a href="${pageContext.request.contextPath}/${leconActive.ressourceUrl}"
                                                           class="btn btn-danger btn-lg" target="_blank">
                                                            <i class="bi bi-eye"></i> Voir le PDF
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/${leconActive.ressourceUrl}"
                                                           class="btn btn-outline-danger btn-lg ms-2" download>
                                                            <i class="bi bi-download"></i> Télécharger
                                                        </a>
                                                    </div>
                                                </c:when>
                                                <%-- URL externe --%>
                                                <c:otherwise>
                                                    <a href="${leconActive.ressourceUrl}"
                                                       class="btn btn-primary" target="_blank">
                                                        <i class="bi bi-box-arrow-up-right"></i>
                                                        Accéder à la ressource
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="alert alert-secondary">
                                                <i class="bi bi-file-earmark-x"></i> Ressource non disponible.
                                            </div>
                                        </c:otherwise>
                                    </c:choose>

                                    <div class="nav-buttons">
                                        <div></div>
                                        <c:choose>
                                            <c:when test="${leconsCompletees.contains(leconActive.id)}">
                                                <button class="btn-complete deja-complete" disabled>
                                                    <i class="bi bi-check-circle-fill"></i> Leçon complétée
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/student/complete-lesson">
                                                    <input type="hidden" name="inscriptionId" value="${inscription.id}">
                                                    <input type="hidden" name="leconId" value="${leconActive.id}">
                                                    <input type="hidden" name="coursId" value="${cours.id}">
                                                    <button type="submit" class="btn-complete">
                                                        <i class="bi bi-check-circle"></i> Marquer comme complétée
                                                    </button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <i class="bi bi-file-text"></i>
                            </c:otherwise>
                        </c:choose>
                    </span>

                    <span>${lecon.titre}</span>
                    <span class="duree">${lecon.dureeMin}min</span>
                </a>
            </c:forEach>
        </c:forEach>
    </div>

    <!-- Zone de contenu principal -->
    <div class="content-area">
        <c:choose>
            <%-- Pas de leçon sélectionnée --%>
            <c:when test="${empty leconActive}">
                <div class="content-card text-center py-5">
                    <i class="bi bi-play-circle display-1 text-primary"></i>
                    <h3 class="mt-3">Bienvenue dans le cours !</h3>
                    <p class="text-muted">Sélectionnez une leçon dans le menu à gauche pour commencer.</p>
                </div>
            </c:when>

            <%-- Leçon de type TEXTE --%>
            <c:when test="${leconActive.typeLecon == 'TEXTE'}">
                <div class="content-card">
                    <h2>${leconActive.titre}</h2>
                    <div class="lecon-body">
                        ${leconActive.contenuTexte}
                    </div>

                    <%-- Boutons navigation --%>
                    <div class="nav-buttons">
                        <div></div>
                        <%-- Bouton Marquer complétée --%>
                        <c:choose>
                            <c:when test="${leconsCompletees.contains(leconActive.id)}">
                                <button class="btn-complete deja-complete" disabled>
                                    <i class="bi bi-check-circle-fill"></i> Leçon complétée
                                </button>
                            </c:when>
                            <c:otherwise>
                                <form method="post" action="${pageContext.request.contextPath}/student/complete-lesson">
                                    <input type="hidden" name="inscriptionId" value="${inscription.id}">
                                    <input type="hidden" name="leconId" value="${leconActive.id}">
                                    <input type="hidden" name="coursId" value="${cours.id}">
                                    <button type="submit" class="btn-complete">
                                        <i class="bi bi-check-circle"></i> Marquer comme complétée
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:when>

            <%-- Leçon de type QUIZ --%>
            <c:when test="${leconActive.typeLecon == 'QUIZ'}">
                <div class="content-card">
                    <h2><i class="bi bi-question-circle"></i> ${leconActive.titre}</h2>

                    <c:choose>
                        <c:when test="${empty quizActif}">
                            <div class="alert alert-warning">Quiz non disponible.</div>
                        </c:when>
                        <c:otherwise>
                            <p class="text-muted mb-4">
                                Score minimum pour valider : <strong>${quizActif.scoreMinimum}%</strong>
                            </p>

                            <form method="post" action="${pageContext.request.contextPath}/student/submit-quiz">
                                <input type="hidden" name="quizId" value="${quizActif.id}">
                                <input type="hidden" name="inscriptionId" value="${inscription.id}">
                                <input type="hidden" name="leconId" value="${leconActive.id}">
                                <input type="hidden" name="coursId" value="${cours.id}">

                                <c:forEach var="question" items="${questions}" varStatus="qIdx">
                                    <div class="card bg-dark border-secondary mb-4">
                                        <div class="card-body">
                                            <p class="fw-bold mb-3">
                                                Q${qIdx.index + 1}. <c:out value="${question.enonce}"/>
                                                <span class="badge bg-secondary ms-2">${question.points} pt(s)</span>
                                            </p>

                                            <c:forEach var="reponse" items="${reponsesParQuestion[question.id]}">
                                                <div class="form-check mb-2">
                                                    <input class="form-check-input" type="radio"
                                                           name="question_${question.id}"
                                                           value="${reponse.id}"
                                                           id="rep_${reponse.id}" required>
                                                    <label class="form-check-label" for="rep_${reponse.id}">
                                                        <c:out value="${reponse.texte}"/>
                                                    </label>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </c:forEach>

                                <button type="submit" class="btn-complete">
                                    <i class="bi bi-send"></i> Soumettre le quiz
                                </button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:when>

            <%-- Leçon de type VIDEO --%>
            <c:when test="${leconActive.typeLecon == 'VIDEO'}">
                <div class="content-card">
                    <h2>${leconActive.titre}</h2>
                    <c:choose>
                        <c:when test="${not empty leconActive.videoUrl}">
                            <video controls class="w-100 rounded mb-4" style="max-height: 450px;">
                                <source src="${leconActive.videoUrl}" type="video/mp4">
                                Votre navigateur ne supporte pas la lecture vidéo.
                            </video>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-secondary">
                                <i class="bi bi-camera-video-off"></i> Vidéo non disponible.
                            </div>
                        </c:otherwise>
                    </c:choose>

                    <div class="nav-buttons">
                        <div></div>
                        <c:choose>
                            <c:when test="${leconsCompletees.contains(leconActive.id)}">
                                <button class="btn-complete deja-complete" disabled>
                                    <i class="bi bi-check-circle-fill"></i> Leçon complétée
                                </button>
                            </c:when>
                            <c:otherwise>
                                <form method="post" action="${pageContext.request.contextPath}/student/complete-lesson">
                                    <input type="hidden" name="inscriptionId" value="${inscription.id}">
                                    <input type="hidden" name="leconId" value="${leconActive.id}">
                                    <input type="hidden" name="coursId" value="${cours.id}">
                                    <button type="submit" class="btn-complete">
                                        <i class="bi bi-check-circle"></i> Marquer comme complétée
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:when>

            <%-- Leçon de type RESSOURCE --%>
            <c:when test="${leconActive.typeLecon == 'RESSOURCE'}">
                <div class="content-card">
                    <h2>${leconActive.titre}</h2>
                    <c:if test="${not empty leconActive.ressourceUrl}">
                        <a href="${leconActive.ressourceUrl}" class="btn btn-primary" download>
                            <i class="bi bi-download"></i> Télécharger la ressource
                        </a>
                    </c:if>

                    <div class="nav-buttons">
                        <div></div>
                        <c:choose>
                            <c:when test="${leconsCompletees.contains(leconActive.id)}">
                                <button class="btn-complete deja-complete" disabled>
                                    <i class="bi bi-check-circle-fill"></i> Leçon complétée
                                </button>
                            </c:when>
                            <c:otherwise>
                                <form method="post" action="${pageContext.request.contextPath}/student/complete-lesson">
                                    <input type="hidden" name="inscriptionId" value="${inscription.id}">
                                    <input type="hidden" name="leconId" value="${leconActive.id}">
                                    <input type="hidden" name="coursId" value="${cours.id}">
                                    <button type="submit" class="btn-complete">
                                        <i class="bi bi-check-circle"></i> Marquer comme complétée
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:when>
        </c:choose>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>