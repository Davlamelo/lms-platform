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
        /* Étoiles interactives */
        .etoile-input { display: none; }
        .etoile-label {
            font-size: 2.5rem;
            color: #ccc;
            cursor: pointer;
            transition: color 0.15s;
            padding: 0 2px;
            line-height: 1;
        }
        .etoile-label.active { color: #f59e0b; }
        .etoile-label:hover { color: #f59e0b; }

        /* Navbar */
        .navbar-brand { font-weight: 700; }
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<!-- En-tête du cours -->
<div class="text-white py-5"
     style="background: linear-gradient(135deg, #2c3e50, #3498db);">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-lg-8">
                <span class="badge bg-warning text-dark mb-2">${cours.niveau}</span>
                <h1 class="mb-3">${cours.titre}</h1>
                <p class="lead mb-3">${cours.descriptionCourte}</p>
                <div class="d-flex flex-wrap gap-4 text-white-50 mb-3">
                    <span>
                        <i class="bi bi-people"></i> ${nombreInscrits} inscrits
                    </span>
                    <span>
                        <i class="bi bi-book"></i> ${nombreLecons} leçons
                    </span>
                    <span>
                        <i class="bi bi-clock"></i> ${cours.dureeTotaleMin} min
                    </span>
                    <c:if test="${nombreAvis > 0}">
                        <span>
                            <i class="bi bi-star-fill text-warning"></i>
                            <fmt:formatNumber value="${noteMoyenne}" maxFractionDigits="1"/>
                            (${nombreAvis} avis)
                        </span>
                    </c:if>
                </div>
                <c:if test="${instructeur != null}">
                    <p class="mb-0">
                        <i class="bi bi-person-badge"></i>
                        Créé par
                        <strong>${instructeur.prenom} ${instructeur.nom}</strong>
                    </p>
                </c:if>
            </div>

            <!-- Bouton d'action selon le rôle -->
            <div class="col-lg-4 text-center mt-4 mt-lg-0">
                <c:choose>

                    <%-- Admin : aperçu sans inscription --%>
                    <c:when test="${estAdmin}">
                        <div class="card border-0 p-3"
                             style="background: rgba(255,255,255,0.1);">
                            <p class="text-warning fw-bold mb-2">
                                <i class="bi bi-shield-check"></i> Mode aperçu admin
                            </p>
                            <p class="text-white-50 small mb-3">
                                Statut : <strong>${cours.statut}</strong>
                            </p>
                            <c:if test="${cours.statut == 'EN_ATTENTE_VALIDATION'}">
                                <form method="post"
                                      action="${pageContext.request.contextPath}/admin/courses"
                                      class="mb-2">
                                    <input type="hidden" name="coursId" value="${cours.id}">
                                    <input type="hidden" name="action" value="valider">
                                    <button type="submit" class="btn btn-success w-100">
                                        <i class="bi bi-check-circle"></i> Valider ce cours
                                    </button>
                                </form>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/admin/courses">
                                    <input type="hidden" name="coursId" value="${cours.id}">
                                    <input type="hidden" name="action" value="rejeter">
                                    <button type="submit" class="btn btn-danger w-100">
                                        <i class="bi bi-x-circle"></i> Rejeter ce cours
                                    </button>
                                </form>
                            </c:if>
                            <a href="${pageContext.request.contextPath}/admin/courses"
                               class="btn btn-outline-light w-100 mt-2">
                                <i class="bi bi-arrow-left"></i> Retour modération
                            </a>
                        </div>
                    </c:when>

                    <%-- Instructeur propriétaire --%>
                    <c:when test="${estInstructeurProprietaire}">
                        <a href="${pageContext.request.contextPath}/instructor/course/curriculum?coursId=${cours.id}"
                           class="btn btn-warning btn-lg w-100 py-3">
                            <i class="bi bi-pencil"></i> Éditer le cours
                        </a>
                        <p class="text-white-50 small mt-2">
                            Statut : <strong>${cours.statut}</strong>
                        </p>
                    </c:when>

                    <%-- Apprenant déjà inscrit --%>
                    <c:when test="${estInscrit}">
                        <a href="${pageContext.request.contextPath}/student/learn?coursId=${cours.id}"
                           class="btn btn-success btn-lg w-100 py-3">
                            <i class="bi bi-play-circle"></i> Continuer le cours
                        </a>
                        <p class="text-white-50 small mt-2">
                            <i class="bi bi-check-circle-fill text-success"></i>
                            Vous êtes inscrit à ce cours
                        </p>
                    </c:when>

                    <%-- Apprenant connecté non inscrit --%>
                    <c:when test="${not empty sessionScope.utilisateur}">
                        <form method="post"
                              action="${pageContext.request.contextPath}/student/enroll">
                            <input type="hidden" name="coursId" value="${cours.id}">
                            <input type="hidden" name="slug" value="${cours.slug}">
                            <button type="submit" class="btn btn-warning btn-lg w-100 py-3">
                                <i class="bi bi-plus-circle"></i> S'inscrire gratuitement
                            </button>
                        </form>
                        <p class="text-white-50 small mt-2">
                            <i class="bi bi-unlock"></i> Cours 100% gratuit
                        </p>
                    </c:when>

                    <%-- Visiteur non connecté --%>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login"
                           class="btn btn-warning btn-lg w-100 py-3">
                            <i class="bi bi-box-arrow-in-right"></i>
                            Connectez-vous pour vous inscrire
                        </a>
                        <p class="text-white-50 small mt-2">
                            <i class="bi bi-unlock"></i> Cours 100% gratuit
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<!-- Messages succès/erreur -->
<div class="container mt-3">
    <c:if test="${succes == 'avis_soumis'}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="bi bi-check-circle"></i> Votre avis a été publié avec succès !
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${succes == 'avis_supprime'}">
        <div class="alert alert-info alert-dismissible fade show">
            <i class="bi bi-info-circle"></i> Votre avis a été supprimé.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty erreur}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="bi bi-exclamation-circle"></i> ${erreur}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
</div>

<!-- Contenu principal -->
<div class="container my-5">
    <div class="row">

        <!-- Colonne principale -->
        <div class="col-lg-8">

            <!-- Description complète -->
            <div class="card border-0 shadow-sm mb-4">
                <div class="card-body">
                    <h4><i class="bi bi-info-circle"></i> À propos de ce cours</h4>
                    <div class="mt-3" style="white-space: pre-line; line-height: 1.8;">
                        ${cours.descriptionLongue}
                    </div>
                </div>
            </div>

            <!-- Curriculum -->
            <div class="card border-0 shadow-sm mb-4">
                <div class="card-body">
                    <h4><i class="bi bi-list-ol"></i> Contenu du cours</h4>
                    <p class="text-muted small mt-1">
                        ${nombreLecons} leçons • ${cours.dureeTotaleMin} minutes au total
                    </p>

                    <div class="accordion mt-3" id="accordionCurriculum">
                        <c:forEach var="section" items="${sections}" varStatus="sIdx">
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button ${sIdx.index > 0 ? 'collapsed' : ''}"
                                            type="button"
                                            data-bs-toggle="collapse"
                                            data-bs-target="#section${section.id}">
                                        <div class="d-flex w-100 align-items-center">
                                            <strong>
                                                Section ${sIdx.index + 1} : ${section.titre}
                                            </strong>
                                            <span class="badge bg-secondary ms-auto me-2">
                                                ${leconsParSection[section.id].size()} leçons
                                            </span>
                                        </div>
                                    </button>
                                </h2>
                                <div id="section${section.id}"
                                     class="accordion-collapse collapse ${sIdx.index == 0 ? 'show' : ''}"
                                     data-bs-parent="#accordionCurriculum">
                                    <div class="accordion-body p-0">
                                        <ul class="list-group list-group-flush">
                                            <c:forEach var="lecon"
                                                       items="${leconsParSection[section.id]}">
                                                <li class="list-group-item d-flex align-items-center py-3">
                                                    <c:choose>
                                                        <c:when test="${lecon.typeLecon == 'VIDEO'}">
                                                            <i class="bi bi-play-circle text-primary me-2"></i>
                                                        </c:when>
                                                        <c:when test="${lecon.typeLecon == 'QUIZ'}">
                                                            <i class="bi bi-question-circle text-warning me-2"></i>
                                                        </c:when>
                                                        <c:when test="${lecon.typeLecon == 'RESSOURCE'}">
                                                            <i class="bi bi-file-earmark text-info me-2"></i>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <i class="bi bi-file-text text-secondary me-2"></i>
                                                        </c:otherwise>
                                                    </c:choose>

                                                    <span>${lecon.titre}</span>

                                                    <div class="ms-auto d-flex align-items-center gap-2">
                                                        <c:if test="${lecon.estGratuite}">
                                                            <span class="badge bg-success">Gratuit</span>
                                                        </c:if>
                                                        <span class="text-muted small">
                                                            ${lecon.dureeMin} min
                                                        </span>
                                                    </div>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <!-- Section Avis -->
            <div class="card border-0 shadow-sm mb-4">
                <div class="card-body">

                    <!-- Titre + note moyenne -->
                    <div class="d-flex align-items-center mb-3">
                        <h4 class="mb-0">
                            <i class="bi bi-star"></i> Avis des apprenants
                        </h4>
                        <c:if test="${nombreAvis > 0}">
                            <div class="ms-3 d-flex align-items-center gap-1">
                                <c:forEach begin="1" end="5" var="e">
                                    <i class="bi ${e <= noteMoyenne ? 'bi-star-fill text-warning' : 'bi-star text-muted'}"></i>
                                </c:forEach>
                                <span class="ms-1 fw-bold">
                                    <fmt:formatNumber value="${noteMoyenne}" maxFractionDigits="1"/>
                                </span>
                                <span class="text-muted small">(${nombreAvis} avis)</span>
                            </div>
                        </c:if>
                    </div>

                    <%-- Formulaire d'avis (visible uniquement si inscrit) --%>
                    <c:if test="${estInscrit}">
                        <div class="card bg-light border-0 mb-4">
                            <div class="card-body">
                                <h6 class="mb-3">
                                    <c:choose>
                                        <c:when test="${not empty monAvis}">
                                            <i class="bi bi-pencil text-primary"></i>
                                            Modifier mon avis
                                        </c:when>
                                        <c:otherwise>
                                            <i class="bi bi-star-fill text-warning"></i>
                                            Laisser un avis
                                        </c:otherwise>
                                    </c:choose>
                                </h6>

                                <%-- Formulaire soumission/modification --%>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/student/avis"
                                      id="formAvis">
                                    <input type="hidden" name="coursId" value="${cours.id}">
                                    <input type="hidden" name="slug" value="${cours.slug}">
                                    <input type="hidden" name="action" value="soumettre">

                                    <%-- Étoiles interactives --%>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold">Note *</label>
                                        <div class="etoiles-container d-flex gap-1 mb-1">
                                            <c:forEach begin="1" end="5" var="etoile">
                                                <input type="radio"
                                                       name="note"
                                                       id="note${etoile}"
                                                       value="${etoile}"
                                                       class="etoile-input"
                                                       ${not empty monAvis && monAvis.note == etoile ? 'checked' : ''}
                                                       required>
                                                <label for="note${etoile}"
                                                       class="etoile-label"
                                                       title="${etoile} étoile(s)">★</label>
                                            </c:forEach>
                                        </div>
                                        <small class="text-muted">
                                            Cliquez pour noter
                                        </small>
                                    </div>

                                    <%-- Commentaire --%>
                                    <div class="mb-3">
                                        <label for="commentaire" class="form-label fw-bold">
                                            Commentaire (optionnel)
                                        </label>
                                        <textarea class="form-control"
                                                  id="commentaire"
                                                  name="commentaire"
                                                  rows="3"
                                                  placeholder="Partagez votre expérience..."><c:if test="${not empty monAvis}">${monAvis.commentaire}</c:if></textarea>
                                    </div>

                                    <button type="submit" class="btn btn-warning">
                                        <i class="bi bi-star-fill"></i>
                                        ${not empty monAvis ? 'Modifier mon avis' : 'Publier mon avis'}
                                    </button>
                                </form>

                                <%-- Formulaire suppression (séparé) --%>
                                <c:if test="${not empty monAvis}">
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/student/avis"
                                          class="mt-2"
                                          onsubmit="return confirm('Supprimer votre avis ?')">
                                        <input type="hidden" name="coursId" value="${cours.id}">
                                        <input type="hidden" name="slug" value="${cours.slug}">
                                        <input type="hidden" name="action" value="supprimer">
                                        <button type="submit" class="btn btn-outline-danger btn-sm">
                                            <i class="bi bi-trash"></i> Supprimer mon avis
                                        </button>
                                    </form>
                                </c:if>

                            </div>
                        </div>
                    </c:if>

                    <%-- Liste des avis --%>
                    <c:choose>
                        <c:when test="${empty avis}">
                            <div class="text-center py-4 text-muted">
                                <i class="bi bi-chat-dots fs-1"></i>
                                <p class="mt-2">
                                    Aucun avis pour l'instant. Soyez le premier !
                                </p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="unAvis" items="${avis}">
                                <div class="border-bottom py-3">
                                    <div class="d-flex align-items-center mb-1">
                                        <c:forEach begin="1" end="${unAvis.note}">
                                            <i class="bi bi-star-fill text-warning"></i>
                                        </c:forEach>
                                        <c:forEach begin="1" end="${5 - unAvis.note}">
                                            <i class="bi bi-star text-muted"></i>
                                        </c:forEach>
                                        <span class="ms-2 text-muted small">
                                            ${unAvis.note}/5
                                        </span>
                                    </div>
                                    <c:if test="${not empty unAvis.commentaire}">
                                        <p class="mb-0 mt-1">
                                            <c:out value="${unAvis.commentaire}"/>
                                        </p>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>

                </div>
            </div>

        </div>

        <!-- Sidebar -->
        <div class="col-lg-4">

            <!-- Instructeur -->
            <c:if test="${instructeur != null}">
                <div class="card border-0 shadow-sm mb-4">
                    <div class="card-body text-center">
                        <i class="bi bi-person-circle display-4 text-primary"></i>
                        <h5 class="mt-2 mb-1">
                            ${instructeur.prenom} ${instructeur.nom}
                        </h5>
                        <span class="badge bg-primary mb-3">Instructeur</span>
                        <c:if test="${not empty instructeur.biographie}">
                            <p class="text-muted small mb-0">
                                ${instructeur.biographie}
                            </p>
                        </c:if>
                    </div>
                </div>
            </c:if>

            <!-- Infos du cours -->
            <div class="card border-0 shadow-sm">
                <div class="card-body">
                    <h6 class="fw-bold mb-3">
                        <i class="bi bi-info-circle"></i> Informations
                    </h6>
                    <ul class="list-unstyled mb-0">
                        <li class="mb-2">
                            <i class="bi bi-bar-chart text-primary me-2"></i>
                            <strong>Niveau :</strong> ${cours.niveau}
                        </li>
                        <li class="mb-2">
                            <i class="bi bi-book text-primary me-2"></i>
                            <strong>Leçons :</strong> ${nombreLecons}
                        </li>
                        <li class="mb-2">
                            <i class="bi bi-clock text-primary me-2"></i>
                            <strong>Durée :</strong> ${cours.dureeTotaleMin} min
                        </li>
                        <li class="mb-2">
                            <i class="bi bi-translate text-primary me-2"></i>
                            <strong>Langue :</strong> ${cours.langue}
                        </li>
                        <li class="mb-2">
                            <i class="bi bi-people text-primary me-2"></i>
                            <strong>Inscrits :</strong> ${nombreInscrits}
                        </li>
                        <c:if test="${nombreAvis > 0}">
                            <li class="mb-2">
                                <i class="bi bi-star text-warning me-2"></i>
                                <strong>Note :</strong>
                                <fmt:formatNumber value="${noteMoyenne}"
                                                  maxFractionDigits="1"/>/5
                                (${nombreAvis} avis)
                            </li>
                        </c:if>
                        <li>
                            <i class="bi bi-tag text-primary me-2"></i>
                            <strong>Accès :</strong>
                            <span class="text-success fw-bold">Gratuit</span>
                        </li>
                    </ul>
                </div>
            </div>

        </div>
    </div>
</div>

<!-- Footer -->
<footer class="bg-dark text-white text-center py-4 mt-5">
    <p class="mb-0">LMS Platform &copy; 2026 — Projet académique ISMAGI</p>
</footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<!-- Script étoiles interactives -->
<script>
document.addEventListener('DOMContentLoaded', function () {
    const labels = document.querySelectorAll('.etoile-label');
    const inputs = document.querySelectorAll('.etoile-input');

    if (labels.length === 0) return;

    // Initialiser selon la valeur pré-cochée (modification d'avis)
    inputs.forEach(function (input, idx) {
        if (input.checked) {
            colorerJusqua(idx);
        }
    });

    labels.forEach(function (label, idx) {

        // Survol → colorer jusqu'à cette étoile
        label.addEventListener('mouseenter', function () {
            colorerJusqua(idx);
        });

        // Quitter → revenir à la sélection actuelle
        label.addEventListener('mouseleave', function () {
            reinitialiser();
            inputs.forEach(function (input, i) {
                if (input.checked) colorerJusqua(i);
            });
        });

        // Clic → sélectionner cette étoile
        label.addEventListener('click', function () {
            inputs[idx].checked = true;
            colorerJusqua(idx);
        });
    });

    function colorerJusqua(idx) {
        labels.forEach(function (l, i) {
            if (i <= idx) {
                l.classList.add('active');
            } else {
                l.classList.remove('active');
            }
        });
    }

    function reinitialiser() {
        labels.forEach(function (l) {
            l.classList.remove('active');
        });
    }
});
</script>

</body>
</html>