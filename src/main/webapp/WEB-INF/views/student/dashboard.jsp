<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Mon Dashboard"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4">

    <!-- En-tête -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="mb-1">
                <i class="bi bi-speedometer2"></i>
                Bonjour, ${sessionScope.utilisateur.prenom} !
            </h2>
            <p class="text-muted mb-0">
                Bienvenue sur votre espace d'apprentissage
            </p>
        </div>
        <a href="${pageContext.request.contextPath}/catalog"
           class="btn btn-primary">
            <i class="bi bi-plus-circle"></i> Découvrir des cours
        </a>
    </div>

    <!-- Messages -->
    <c:if test="${not empty param.erreur}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="bi bi-exclamation-circle"></i> ${param.erreur}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Statistiques rapides -->
    <c:if test="${not empty inscriptionsAvecCours}">
        <div class="row g-3 mb-4">
            <div class="col-md-4">
                <div class="card border-0 shadow-sm text-center py-3">
                    <div class="card-body">
                        <i class="bi bi-collection-play fs-1 text-primary"></i>
                        <h3 class="mt-2 mb-0">${inscriptionsAvecCours.size()}</h3>
                        <p class="text-muted mb-0">Cours suivis</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card border-0 shadow-sm text-center py-3">
                    <div class="card-body">
                        <i class="bi bi-trophy fs-1 text-warning"></i>
                        <h3 class="mt-2 mb-0">
                            <%-- Compter les cours complétés --%>
                            <c:set var="nbCompletes" value="0"/>
                            <c:forEach var="entry" items="${inscriptionsAvecCours}">
                                <c:if test="${entry.key.pourcentageProgression >= 100}">
                                    <c:set var="nbCompletes" value="${nbCompletes + 1}"/>
                                </c:if>
                            </c:forEach>
                            ${nbCompletes}
                        </h3>
                        <p class="text-muted mb-0">Cours complétés</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card border-0 shadow-sm text-center py-3">
                    <div class="card-body">
                        <i class="bi bi-patch-check fs-1 text-success"></i>
                        <h3 class="mt-2 mb-0">${nbCompletes}</h3>
                        <p class="text-muted mb-0">Certificats obtenus</p>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <!-- Liste des cours -->
    <c:choose>
        <c:when test="${empty inscriptionsAvecCours}">
            <div class="card border-0 shadow-sm">
                <div class="card-body text-center py-5">
                    <i class="bi bi-book display-1 text-muted"></i>
                    <h4 class="mt-3 text-muted">
                        Vous n'êtes inscrit à aucun cours
                    </h4>
                    <p class="text-muted">
                        Explorez notre catalogue et commencez à apprendre !
                    </p>
                    <a href="${pageContext.request.contextPath}/catalog"
                       class="btn btn-primary mt-2">
                        <i class="bi bi-search"></i> Explorer le catalogue
                    </a>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <h5 class="mb-3">
                <i class="bi bi-collection-play"></i> Mes cours
            </h5>
            <div class="row g-4">
                <c:forEach var="entry" items="${inscriptionsAvecCours}">
                    <div class="col-lg-4 col-md-6">
                        <div class="card h-100 border-0 shadow-sm">
                            <div class="card-body d-flex flex-column">

                                <!-- Niveau -->
                                <span class="badge bg-primary mb-2 align-self-start">
                                    ${entry.value.niveau}
                                </span>

                                <!-- Titre -->
                                <h6 class="card-title">${entry.value.titre}</h6>

                                <!-- Description courte -->
                                <p class="card-text text-muted small flex-grow-1">
                                    ${entry.value.descriptionCourte}
                                </p>

                                <!-- Barre de progression -->
                                <div class="mt-2 mb-3">
                                    <div class="d-flex justify-content-between
                                                align-items-center mb-1">
                                        <small class="text-muted">Progression</small>
                                        <small class="fw-bold
                                            ${entry.key.pourcentageProgression >= 100
                                              ? 'text-success' : 'text-primary'}">
                                            <fmt:formatNumber
                                                value="${entry.key.pourcentageProgression}"
                                                maxFractionDigits="0"/>%
                                        </small>
                                    </div>
                                    <div class="progress" style="height: 8px;">
                                        <div class="progress-bar
                                            ${entry.key.pourcentageProgression >= 100
                                              ? 'bg-success' : 'bg-primary'}"
                                             style="width: ${entry.key.pourcentageProgression}%">
                                        </div>
                                    </div>
                                </div>

                                <!-- Boutons d'action selon la progression -->
                                <c:choose>

                                    <%-- Cours complété à 100% --%>
                                    <c:when test="${entry.key.pourcentageProgression >= 100}">
                                        <div class="mb-2">
                                            <span class="badge bg-success w-100 py-2">
                                                <i class="bi bi-check-circle-fill"></i>
                                                Cours complété !
                                            </span>
                                        </div>
                                        <div class="d-flex gap-2">
                                            <a href="${pageContext.request.contextPath}/student/learn?coursId=${entry.value.id}"
                                               class="btn btn-outline-secondary btn-sm flex-fill">
                                                <i class="bi bi-arrow-repeat"></i> Revoir
                                            </a>
                                            <a href="${pageContext.request.contextPath}/student/certificate?inscriptionId=${entry.key.id}"
                                               class="btn btn-warning btn-sm flex-fill">
                                                <i class="bi bi-trophy-fill"></i> Certificat
                                            </a>
                                        </div>
                                    </c:when>

                                    <%-- Cours non commencé --%>
                                    <c:when test="${entry.key.pourcentageProgression == 0}">
                                        <a href="${pageContext.request.contextPath}/student/learn?coursId=${entry.value.id}"
                                           class="btn btn-primary btn-sm w-100">
                                            <i class="bi bi-play-fill"></i> Commencer
                                        </a>
                                    </c:when>

                                    <%-- Cours en cours --%>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/student/learn?coursId=${entry.value.id}"
                                           class="btn btn-outline-primary btn-sm w-100">
                                            <i class="bi bi-play-circle"></i> Continuer
                                        </a>
                                    </c:otherwise>

                                </c:choose>

                                <!-- Lien vers la fiche du cours -->
                                <a href="${pageContext.request.contextPath}/course?slug=${entry.value.slug}"
                                   class="btn btn-link btn-sm text-muted mt-1 p-0">
                                    <i class="bi bi-info-circle"></i> Détails du cours
                                </a>

                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

</div>

<!-- Footer -->
<footer class="bg-dark text-white text-center py-4 mt-5">
    <p class="mb-0">
        LMS Platform &copy; 2026 — Projet académique ISMAGI
    </p>
</footer>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>