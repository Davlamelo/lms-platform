<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Mon Dashboard"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>
            <i class="bi bi-speedometer2"></i>
            Bonjour, ${sessionScope.utilisateur.prenom} !
        </h2>
        <a href="${pageContext.request.contextPath}/catalog" class="btn btn-primary">
            <i class="bi bi-plus-circle"></i> Découvrir des cours
        </a>
    </div>

    <c:choose>
        <c:when test="${empty inscriptions}">
            <div class="text-center py-5">
                <i class="bi bi-book display-1 text-muted"></i>
                <h4 class="mt-3 text-muted">Vous n'êtes inscrit à aucun cours</h4>
                <a href="${pageContext.request.contextPath}/catalog" class="btn btn-primary mt-3">
                    Explorer le catalogue
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <h5 class="mb-3"><i class="bi bi-collection-play"></i> Mes cours</h5>
            <div class="row g-4">
                <c:forEach var="entry" items="${inscriptionsAvecCours}">
                    <div class="col-md-6 col-lg-4">
                        <div class="card h-100 border-0 shadow-sm">
                            <div class="card-body d-flex flex-column">
                                <span class="badge bg-primary mb-2 align-self-start">
                                    ${entry.value.niveau}
                                </span>
                                <h6 class="card-title">${entry.value.titre}</h6>
                                <p class="card-text text-muted small flex-grow-1">
                                    ${entry.value.descriptionCourte}
                                </p>

                                <!-- Barre de progression -->
                                <div class="mt-3 mb-2">
                                    <div class="d-flex justify-content-between small text-muted mb-1">
                                        <span>Progression</span>
                                        <span>
                                            <fmt:formatNumber value="${entry.key.pourcentageProgression}"
                                                              maxFractionDigits="0"/>%
                                        </span>
                                    </div>
                                    <div class="progress" style="height: 8px;">
                                        <div class="progress-bar bg-success"
                                             style="width: ${entry.key.pourcentageProgression}%">
                                        </div>
                                    </div>
                                </div>

                                <a href="${pageContext.request.contextPath}/student/learn?coursId=${entry.value.id}"
                                   class="btn btn-outline-primary btn-sm mt-2">
                                    <c:choose>
                                        <c:when test="${entry.key.pourcentageProgression == 0}">
                                            <i class="bi bi-play"></i> Commencer
                                        </c:when>
                                        <c:when test="${entry.key.pourcentageProgression == 100}">
                                            <i class="bi bi-trophy"></i> Revoir le cours
                                        </c:when>
                                        <c:otherwise>
                                            <i class="bi bi-play-circle"></i> Continuer
                                        </c:otherwise>
                                    </c:choose>
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>