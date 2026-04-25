<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Accueil"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<!-- Hero -->
<div class="text-white py-5" style="background: linear-gradient(135deg, #5a2d82 0%, #8e44ad 100%);">
    <div class="container text-center py-4">
        <h1 class="display-4 fw-bold mb-3">Apprenez sans limites</h1>
        <p class="lead mb-4">Des cours gratuits en Data Science, Développement Web, IA et bien plus.</p>
        <a href="${pageContext.request.contextPath}/catalog" class="btn btn-light btn-lg px-4">
            <i class="bi bi-search"></i> Explorer le catalogue
        </a>
    </div>
</div>

<!-- Catégories -->
<div class="container my-5">
    <h2 class="mb-4"><i class="bi bi-grid"></i> Catégories populaires</h2>
    <div class="row g-3">
        <c:forEach var="cat" items="${categories}">
            <div class="col-md-3 col-sm-6">
                <a href="${pageContext.request.contextPath}/catalog?categorie=${cat.slug}"
                   class="card text-decoration-none h-100 border-0 shadow-sm">
                    <div class="card-body text-center py-4">
                        <i class="bi ${cat.icone} fs-1 text-primary"></i>
                        <h6 class="mt-3 mb-0 text-dark">${cat.nom}</h6>
                    </div>
                </a>
            </div>
        </c:forEach>
    </div>
</div>

<!-- Cours mis en avant -->
<div class="container my-5">
    <h2 class="mb-4"><i class="bi bi-star"></i> Cours à découvrir</h2>
    <div class="row g-4">
        <c:forEach var="cours" items="${coursMisEnAvant}">
            <div class="col-lg-4 col-md-6">
                <div class="card h-100 border-0 shadow-sm">
                    <div class="card-body">
                        <span class="badge bg-primary mb-2">${cours.niveau}</span>
                        <h5 class="card-title">${cours.titre}</h5>
                        <p class="card-text text-muted small">${cours.descriptionCourte}</p>
                    </div>
                    <div class="card-footer bg-white border-0">
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

<!-- Footer -->
<footer class="bg-dark text-white text-center py-4 mt-5">
    <p class="mb-0">LMS Platform &copy; 2026 — Projet académique ISMAGI</p>
</footer>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>