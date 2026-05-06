<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catalogue - LMS Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .card-cours:hover {
            transform: translateY(-3px);
            transition: transform 0.2s;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1) !important;
        }
        .list-group-item.active {
            background-color: #5a2d82;
            border-color: #5a2d82;
        }
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4">
    <h2 class="mb-4">
        <i class="bi bi-grid"></i> Catalogue des cours
    </h2>

    <div class="row">

        <!-- Sidebar : Recherche + Catégories -->
        <div class="col-md-3">

            <!-- Recherche -->
            <div class="card border-0 shadow-sm mb-4">
                <div class="card-body">
                    <h6 class="card-title fw-bold">
                        <i class="bi bi-search"></i> Rechercher
                    </h6>
                    <form method="get"
                          action="${pageContext.request.contextPath}/catalog">
                        <div class="input-group">
                            <input type="text" class="form-control form-control-sm"
                                   name="q"
                                   placeholder="Python, SQL, UX..."
                                   value="${recherche}">
                            <button class="btn btn-primary btn-sm" type="submit">
                                <i class="bi bi-search"></i>
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Catégories -->
            <div class="card border-0 shadow-sm">
                <div class="card-body">
                    <h6 class="card-title fw-bold">
                        <i class="bi bi-grid"></i> Catégories
                    </h6>
                    <div class="list-group list-group-flush">
                        <a href="${pageContext.request.contextPath}/catalog"
                           class="list-group-item list-group-item-action
                           ${empty categorieActive ? 'active' : ''}">
                            <i class="bi bi-collection"></i> Toutes
                        </a>
                        <c:forEach var="cat" items="${categories}">
                            <a href="${pageContext.request.contextPath}/catalog?categorie=${cat.slug}"
                               class="list-group-item list-group-item-action
                               ${not empty categorieActive && categorieActive.id == cat.id ? 'active' : ''}">
                                <i class="bi ${cat.icone}"></i> ${cat.nom}
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>

        <!-- Grille de cours -->
        <div class="col-md-9">

            <!-- Indicateur de recherche -->
            <c:if test="${not empty recherche}">
                <div class="alert alert-info mb-3">
                    <i class="bi bi-info-circle"></i>
                    Résultats pour "<strong>${recherche}</strong>" :
                    ${cours.size()} cours trouvé(s)
                    <a href="${pageContext.request.contextPath}/catalog"
                       class="ms-3 btn btn-sm btn-outline-secondary">
                        <i class="bi bi-x"></i> Effacer
                    </a>
                </div>
            </c:if>

            <!-- Indicateur de catégorie -->
            <c:if test="${not empty categorieActive}">
                <div class="alert alert-secondary mb-3">
                    <i class="bi ${categorieActive.icone}"></i>
                    Catégorie : <strong>${categorieActive.nom}</strong>
                    — ${cours.size()} cours
                    <a href="${pageContext.request.contextPath}/catalog"
                       class="ms-3 btn btn-sm btn-outline-secondary">
                        <i class="bi bi-x"></i> Voir tout
                    </a>
                </div>
            </c:if>

            <!-- Aucun résultat -->
            <c:if test="${empty cours}">
                <div class="text-center py-5">
                    <i class="bi bi-emoji-frown display-1 text-muted"></i>
                    <h4 class="mt-3 text-muted">Aucun cours trouvé</h4>
                    <a href="${pageContext.request.contextPath}/catalog"
                       class="btn btn-primary mt-3">
                        Voir tous les cours
                    </a>
                </div>
            </c:if>

            <!-- Grille des cours -->
            <div class="row g-4">
                <c:forEach var="unCours" items="${cours}">
                    <div class="col-lg-4 col-md-6">
                        <div class="card h-100 border-0 shadow-sm card-cours">

                            <%-- MODIFIÉ : Miniature avec support URL absolue (http/https) et chemin local --%>
                            <c:choose>
                                <c:when test="${not empty unCours.miniatureUrl && fn:startsWith(unCours.miniatureUrl, 'http')}">
                                    <img src="${unCours.miniatureUrl}"
                                         alt="${unCours.titre}"
                                         class="card-img-top"
                                         style="height: 140px; object-fit: cover;">
                                </c:when>
                                <c:when test="${not empty unCours.miniatureUrl}">
                                    <img src="${pageContext.request.contextPath}/${unCours.miniatureUrl}"
                                         alt="${unCours.titre}"
                                         class="card-img-top"
                                         style="height: 140px; object-fit: cover;">
                                </c:when>
                                <c:otherwise>
                                    <div class="card-img-top d-flex align-items-center
                                                justify-content-center"
                                         style="height: 140px;
                                                background: linear-gradient(135deg, #5a2d82, #8e44ad);">
                                        <i class="bi bi-play-circle text-white"
                                           style="font-size: 3rem;"></i>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <div class="card-body d-flex flex-column">
                                <!-- Niveau -->
                                <span class="badge bg-primary mb-2 align-self-start">
                                    ${unCours.niveau}
                                </span>

                                <!-- Titre -->
                                <h6 class="card-title">${unCours.titre}</h6>

                                <!-- Description -->
                                <p class="card-text text-muted small flex-grow-1">
                                    ${unCours.descriptionCourte}
                                </p>

                                <!-- Durée -->
                                <div class="text-muted small mb-3">
                                    <i class="bi bi-clock"></i>
                                    ${unCours.dureeTotaleMin} min
                                </div>

                                <!-- Bouton -->
                                <a href="${pageContext.request.contextPath}/course?slug=${unCours.slug}"
                                   class="btn btn-outline-primary btn-sm w-100 mt-auto">
                                    Voir le cours
                                    <i class="bi bi-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<footer class="bg-dark text-white text-center py-4 mt-5">
    <p class="mb-0">LMS Platform &copy; 2026 — Projet académique ISMAGI</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
