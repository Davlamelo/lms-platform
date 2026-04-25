<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="${cours.titre}"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<!-- En-tête du cours -->
<div class="text-white py-5" style="background: linear-gradient(135deg, #2c3e50, #3498db);">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-lg-8">
                <span class="badge bg-warning text-dark mb-2">${cours.niveau}</span>
                <h1 class="mb-3">${cours.titre}</h1>
                <p class="lead mb-3">${cours.descriptionCourte}</p>
                <div class="d-flex gap-4 text-white-50 mb-3">
                    <span><i class="bi bi-people"></i> ${nombreInscrits} inscrits</span>
                    <span><i class="bi bi-book"></i> ${nombreLecons} leçons</span>
                    <span><i class="bi bi-clock"></i> ${cours.dureeTotaleMin} min</span>
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
                        Créé par <strong>${instructeur.prenom} ${instructeur.nom}</strong>
                    </p>
                </c:if>
            </div>
            <div class="col-lg-4 text-center mt-4 mt-lg-0">
                <c:choose>
                    <c:when test="${estInscrit}">
                        <a href="${pageContext.request.contextPath}/student/learn?coursId=${cours.id}"
                           class="btn btn-success btn-lg w-100 py-3">
                            <i class="bi bi-play-circle"></i> Continuer le cours
                        </a>
                    </c:when>
                    <c:when test="${not empty sessionScope.utilisateur}">
                        <form method="post" action="${pageContext.request.contextPath}/student/enroll">
                            <input type="hidden" name="coursId" value="${cours.id}">
                            <input type="hidden" name="slug" value="${cours.slug}">
                            <button type="submit" class="btn btn-warning btn-lg w-100 py-3">
                                <i class="bi bi-plus-circle"></i> S'inscrire gratuitement
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login"
                           class="btn btn-warning btn-lg w-100 py-3">
                            <i class="bi bi-box-arrow-in-right"></i> Connectez-vous pour vous inscrire
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<div class="container my-5">
    <div class="row">
        <!-- Contenu principal -->
        <div class="col-lg-8">
            <!-- Description -->
            <div class="card border-0 shadow-sm mb-4">
                <div class="card-body">
                    <h4><i class="bi bi-info-circle"></i> À propos de ce cours</h4>
                    <div class="mt-3" style="white-space: pre-line;">${cours.descriptionLongue}</div>
                </div>
            </div>

            <!-- Curriculum -->
            <div class="card border-0 shadow-sm mb-4">
                <div class="card-body">
                    <h4><i class="bi bi-list-ol"></i> Contenu du cours</h4>
                    <div class="accordion mt-3" id="accordionCurriculum">
                        <c:forEach var="section" items="${sections}" varStatus="idx">
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button ${idx.index > 0 ? 'collapsed' : ''}"
                                            type="button" data-bs-toggle="collapse"
                                            data-bs-target="#section${section.id}">
                                        <strong>Section ${idx.index + 1} :</strong>&nbsp;${section.titre}
                                        <span class="badge bg-secondary ms-auto me-2">
                                            ${leconsParSection[section.id].size()} leçons
                                        </span>
                                    </button>
                                </h2>
                                <div id="section${section.id}"
                                     class="accordion-collapse collapse ${idx.index == 0 ? 'show' : ''}"
                                     data-bs-parent="#accordionCurriculum">
                                    <div class="accordion-body p-0">
                                        <ul class="list-group list-group-flush">
                                            <c:forEach var="lecon" items="${leconsParSection[section.id]}">
                                                <li class="list-group-item d-flex align-items-center">
                                                    <c:choose>
                                                        <c:when test="${lecon.typeLecon == 'VIDEO'}">
                                                            <i class="bi bi-play-circle text-primary me-2"></i>
                                                        </c:when>
                                                        <c:when test="${lecon.typeLecon == 'TEXTE'}">
                                                            <i class="bi bi-file-text text-info me-2"></i>
                                                        </c:when>
                                                        <c:when test="${lecon.typeLecon == 'QUIZ'}">
                                                            <i class="bi bi-question-circle text-warning me-2"></i>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <i class="bi bi-file-earmark text-secondary me-2"></i>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    ${lecon.titre}
                                                    <span class="text-muted ms-auto small">
                                                        ${lecon.dureeMin} min
                                                    </span>
                                                    <c:if test="${lecon.estGratuite}">
                                                        <span class="badge bg-success ms-2">Gratuit</span>
                                                    </c:if>
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

            <!-- Avis -->
            <c:if test="${not empty avis}">
                <div class="card border-0 shadow-sm">
                    <div class="card-body">
                        <h4><i class="bi bi-chat-dots"></i> Avis des apprenants</h4>
                        <c:forEach var="unAvis" items="${avis}">
                            <div class="border-bottom py-3">
                                <div class="d-flex align-items-center mb-1">
                                    <c:forEach begin="1" end="${unAvis.note}">
                                        <i class="bi bi-star-fill text-warning"></i>
                                    </c:forEach>
                                    <c:forEach begin="1" end="${5 - unAvis.note}">
                                        <i class="bi bi-star text-warning"></i>
                                    </c:forEach>
                                </div>
                                <p class="mb-0">${unAvis.commentaire}</p>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </div>

        <!-- Sidebar instructeur -->
        <div class="col-lg-4">
            <c:if test="${instructeur != null}">
                <div class="card border-0 shadow-sm">
                    <div class="card-body text-center">
                        <i class="bi bi-person-circle display-4 text-primary"></i>
                        <h5 class="mt-2">${instructeur.prenom} ${instructeur.nom}</h5>
                        <span class="badge bg-primary">Instructeur</span>
                        <c:if test="${not empty instructeur.biographie}">
                            <p class="text-muted mt-3 small">${instructeur.biographie}</p>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>