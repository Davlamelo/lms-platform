<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accueil - LMS Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
          rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
          rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .card-cours:hover {
            transform: translateY(-4px);
            transition: transform 0.2s ease;
            box-shadow: 0 8px 25px rgba(0,0,0,0.12) !important;
        }
        .hero-section {
            background: linear-gradient(135deg, #5a2d82 0%, #8e44ad 100%);
            color: white;
            padding: 80px 0;
        }
        .categorie-card:hover {
            transform: translateY(-3px);
            transition: transform 0.2s;
            box-shadow: 0 6px 20px rgba(0,0,0,0.1) !important;
        }
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<!-- Hero Section -->
<div class="hero-section">
    <div class="container text-center">
        <h1 class="display-4 fw-bold mb-3">
            Apprenez sans limites 🎓
        </h1>
        <p class="lead mb-4 opacity-75">
            Des cours gratuits en Data Science, Développement Web,
            IA et bien plus encore.
        </p>
        <div class="d-flex gap-3 justify-content-center flex-wrap">
            <a href="${pageContext.request.contextPath}/catalog"
               class="btn btn-light btn-lg px-4">
                <i class="bi bi-search"></i> Explorer le catalogue
            </a>
            <c:if test="${empty sessionScope.utilisateur}">
                <a href="${pageContext.request.contextPath}/register"
                   class="btn btn-outline-light btn-lg px-4">
                    <i class="bi bi-person-plus"></i> S'inscrire gratuitement
                </a>
            </c:if>
        </div>
    </div>
</div>

<!-- Catégories -->
<div class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">
            <i class="bi bi-grid"></i> Catégories populaires
        </h2>
        <a href="${pageContext.request.contextPath}/catalog"
           class="btn btn-outline-primary btn-sm">
            Voir tout <i class="bi bi-arrow-right"></i>
        </a>
    </div>

    <div class="row g-3">
        <c:forEach var="cat" items="${categories}">
            <div class="col-lg-3 col-md-4 col-sm-6">
                <a href="${pageContext.request.contextPath}/catalog?categorie=${cat.slug}"
                   class="card text-decoration-none h-100 border-0 shadow-sm
                          categorie-card">
                    <div class="card-body text-center py-4">
                        <i class="bi ${cat.icone} fs-1 text-primary"></i>
                        <h6 class="mt-3 mb-1 text-dark fw-bold">${cat.nom}</h6>
                        <small class="text-muted">${cat.description}</small>
                    </div>
                </a>
            </div>
        </c:forEach>
    </div>
</div>

<!-- Cours à découvrir -->
<div class="bg-white py-5">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="mb-0">
                <i class="bi bi-star"></i> Cours à découvrir
            </h2>
            <a href="${pageContext.request.contextPath}/catalog"
               class="btn btn-outline-primary btn-sm">
                Voir tout <i class="bi bi-arrow-right"></i>
            </a>
        </div>

        <c:if test="${empty coursMisEnAvant}">
            <div class="text-center py-4 text-muted">
                <i class="bi bi-collection display-1"></i>
                <p class="mt-3">Aucun cours disponible pour l'instant.</p>
            </div>
        </c:if>

        <div class="row g-4">
            <c:forEach var="cours" items="${coursMisEnAvant}">
                <div class="col-lg-4 col-md-6">
                    <div class="card h-100 border-0 shadow-sm card-cours">

                        <%-- MODIFIÉ : Miniature avec support URL absolue (http/https) et chemin local --%>
                        <c:choose>
                            <c:when test="${not empty cours.miniatureUrl && fn:startsWith(cours.miniatureUrl, 'http')}">
                                <img src="${cours.miniatureUrl}"
                                     alt="${cours.titre}"
                                     class="card-img-top"
                                     style="height: 160px; object-fit: cover;">
                            </c:when>
                            <c:when test="${not empty cours.miniatureUrl}">
                                <img src="${pageContext.request.contextPath}/${cours.miniatureUrl}"
                                     alt="${cours.titre}"
                                     class="card-img-top"
                                     style="height: 160px; object-fit: cover;">
                            </c:when>
                            <c:otherwise>
                                <div class="d-flex align-items-center
                                            justify-content-center card-img-top"
                                     style="height: 160px;
                                            background: linear-gradient(135deg, #5a2d82, #8e44ad);">
                                    <i class="bi bi-play-circle text-white"
                                       style="font-size: 3.5rem;"></i>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <div class="card-body d-flex flex-column">
                            <span class="badge bg-primary mb-2 align-self-start">
                                ${cours.niveau}
                            </span>
                            <h5 class="card-title">${cours.titre}</h5>
                            <p class="card-text text-muted small flex-grow-1">
                                ${cours.descriptionCourte}
                            </p>
                            <div class="d-flex align-items-center
                                        justify-content-between mt-2">
                                <small class="text-muted">
                                    <i class="bi bi-clock"></i>
                                    ${cours.dureeTotaleMin} min
                                </small>
                                <span class="badge bg-success">Gratuit</span>
                            </div>
                        </div>

                        <div class="card-footer bg-white border-0 pb-3">
                            <a href="${pageContext.request.contextPath}/course?slug=${cours.slug}"
                               class="btn btn-outline-primary btn-sm w-100">
                                Voir le cours <i class="bi bi-arrow-right"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<!-- Bannière d'inscription -->
<c:if test="${empty sessionScope.utilisateur}">
    <div class="container my-5">
        <div class="card border-0 shadow-sm"
             style="background: linear-gradient(135deg, #2c3e50, #3498db);">
            <div class="card-body text-white text-center py-5">
                <h3 class="mb-3">
                    Rejoignez notre communauté d'apprenants !
                </h3>
                <p class="mb-4 opacity-75">
                    Accédez à tous les cours gratuitement et obtenez
                    des certificats reconnus.
                </p>
                <a href="${pageContext.request.contextPath}/register"
                   class="btn btn-warning btn-lg px-5">
                    <i class="bi bi-rocket-takeoff"></i>
                    Commencer maintenant — C'est gratuit !
                </a>
            </div>
        </div>
    </div>
</c:if>

<!-- Footer -->
<footer class="bg-dark text-white py-4 mt-5">
    <div class="container text-center">
        <p class="mb-1">
            LMS Platform &copy; 2026 — Projet académique ISMAGI
        </p>
        <small class="text-muted">
            Développé par Tassembedo Ulrich David
        </small>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
