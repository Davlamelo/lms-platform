<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Q&R - ${cours.titre}"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4">

    <!-- En-tête -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item">
                        <a href="${pageContext.request.contextPath}/course?slug=${cours.slug}">
                            ${cours.titre}
                        </a>
                    </li>
                    <li class="breadcrumb-item active">Questions & Réponses</li>
                </ol>
            </nav>
            <h2 class="mb-0">
                <i class="bi bi-chat-dots"></i> Questions & Réponses
            </h2>
        </div>

        <c:if test="${estInscrit}">
            <button class="btn btn-primary" data-bs-toggle="modal"
                    data-bs-target="#modalNouvelleQuestion">
                <i class="bi bi-plus-circle"></i> Poser une question
            </button>
        </c:if>
    </div>

    <!-- Messages -->
    <c:if test="${not empty param.succes}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="bi bi-check-circle"></i> ${param.succes}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Liste des fils -->
    <c:choose>
        <c:when test="${empty fils}">
            <div class="card border-0 shadow-sm">
                <div class="card-body text-center py-5">
                    <i class="bi bi-chat-dots display-1 text-muted"></i>
                    <h5 class="mt-3 text-muted">
                        Aucune question pour l'instant
                    </h5>
                    <c:if test="${estInscrit}">
                        <p class="text-muted">
                            Soyez le premier à poser une question !
                        </p>
                    </c:if>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="list-group">
                <c:forEach var="fil" items="${fils}">
                    <a href="${pageContext.request.contextPath}/qa/thread?filId=${fil.id}"
                       class="list-group-item list-group-item-action border-0 shadow-sm mb-2 rounded">
                        <div class="d-flex align-items-start">
                            <div class="flex-grow-1">
                                <div class="d-flex align-items-center gap-2 mb-1">
                                    <h6 class="mb-0">${fil.titre}</h6>
                                    <c:if test="${fil.estResolu}">
                                        <span class="badge bg-success">
                                            <i class="bi bi-check-circle"></i> Résolu
                                        </span>
                                    </c:if>
                                </div>
                                <p class="text-muted small mb-1">
                                    <c:out value="${fil.contenu}"/>
                                </p>
                                <small class="text-muted">
                                    Par <strong>${nomsAuteurs[fil.id]}</strong>
                                </small>
                            </div>
                            <div class="text-end ms-3">
                                <span class="badge bg-light text-dark">
                                    <i class="bi bi-chat"></i>
                                    ${nbMessagesParFil[fil.id]} réponse(s)
                                </span>
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Modal : Nouvelle question -->
<c:if test="${estInscrit}">
    <div class="modal fade" id="modalNouvelleQuestion" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="bi bi-question-circle"></i> Poser une question
                    </h5>
                    <button type="button" class="btn-close"
                            data-bs-dismiss="modal"></button>
                </div>
                <form method="post"
                      action="${pageContext.request.contextPath}/qa/thread">
                    <input type="hidden" name="action" value="creer">
                    <input type="hidden" name="coursId" value="${cours.id}">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Titre *</label>
                            <input type="text" class="form-control" name="titre"
                                   required maxlength="300"
                                   placeholder="Résumez votre question en une phrase">
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">Détails *</label>
                            <textarea class="form-control" name="contenu"
                                      rows="5" required
                                      placeholder="Décrivez votre problème ou question en détail..."></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary"
                                data-bs-dismiss="modal">Annuler</button>
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-send"></i> Publier la question
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</c:if>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>