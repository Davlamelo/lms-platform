<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="${fil.titre}"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4" style="max-width: 900px;">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-3">
        <ol class="breadcrumb">
            <li class="breadcrumb-item">
                <a href="${pageContext.request.contextPath}/course?slug=${cours.slug}">
                    ${cours.titre}
                </a>
            </li>
            <li class="breadcrumb-item">
                <a href="${pageContext.request.contextPath}/qa?coursId=${cours.id}">
                    Q&R
                </a>
            </li>
            <li class="breadcrumb-item active">${fil.titre}</li>
        </ol>
    </nav>

    <!-- Message succès -->
    <c:if test="${not empty param.succes}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="bi bi-check-circle"></i> ${param.succes}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Question principale (le fil) -->
    <div class="card border-0 shadow-sm mb-4
         ${fil.estResolu ? 'border-start border-success border-4' : ''}">
        <div class="card-body">
            <div class="d-flex align-items-start justify-content-between mb-3">
                <div>
                    <h4 class="mb-1">${fil.titre}</h4>
                    <small class="text-muted">
                        <i class="bi bi-person"></i>
                        Posé par <strong>${nomsAuteurs[fil.auteurId]}</strong>
                    </small>
                </div>
                <div class="d-flex gap-2">
                    <c:if test="${fil.estResolu}">
                        <span class="badge bg-success fs-6">
                            <i class="bi bi-check-circle-fill"></i> Résolu
                        </span>
                    </c:if>
                    <c:if test="${estAuteurFil && !fil.estResolu}">
                        <form method="post"
                              action="${pageContext.request.contextPath}/qa/thread">
                            <input type="hidden" name="action" value="resoudre">
                            <input type="hidden" name="filId" value="${fil.id}">
                            <input type="hidden" name="coursId" value="${cours.id}">
                            <button type="submit" class="btn btn-sm btn-outline-success">
                                <i class="bi bi-check-circle"></i> Marquer résolu
                            </button>
                        </form>
                    </c:if>
                </div>
            </div>
            <div class="p-3 bg-light rounded">
                <c:out value="${fil.contenu}"/>
            </div>
        </div>
    </div>

    <!-- Réponses -->
    <h5 class="mb-3">
        <i class="bi bi-chat-dots"></i>
        ${messages.size()} réponse(s)
    </h5>

    <c:forEach var="msg" items="${messages}">
        <div class="card border-0 shadow-sm mb-3
             ${msg.estReponseOfficielle ? 'border-start border-primary border-4' : ''}">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-start mb-2">
                    <small class="text-muted">
                        <i class="bi bi-person"></i>
                        <strong>${nomsAuteurs[msg.auteurId]}</strong>
                        <c:if test="${msg.estReponseOfficielle}">
                            <span class="badge bg-primary ms-2">
                                <i class="bi bi-patch-check"></i>
                                Réponse officielle
                            </span>
                        </c:if>
                    </small>

                    <c:if test="${estInstructeur && !msg.estReponseOfficielle}">
                        <form method="post"
                              action="${pageContext.request.contextPath}/qa/thread">
                            <input type="hidden" name="action" value="officiel">
                            <input type="hidden" name="messageId" value="${msg.id}">
                            <input type="hidden" name="filId" value="${fil.id}">
                            <input type="hidden" name="coursId" value="${cours.id}">
                            <button type="submit"
                                    class="btn btn-sm btn-outline-primary">
                                <i class="bi bi-patch-check"></i> Marquer officielle
                            </button>
                        </form>
                    </c:if>
                </div>
                <p class="mb-0">
                    <c:out value="${msg.contenu}"/>
                </p>
            </div>
        </div>
    </c:forEach>

    <!-- Formulaire de réponse -->
    <c:if test="${not empty sessionScope.utilisateur}">
        <div class="card border-0 shadow-sm mt-4">
            <div class="card-body">
                <h6><i class="bi bi-reply"></i> Votre réponse</h6>
                <form method="post"
                      action="${pageContext.request.contextPath}/qa/thread">
                    <input type="hidden" name="action" value="repondre">
                    <input type="hidden" name="filId" value="${fil.id}">
                    <input type="hidden" name="coursId" value="${cours.id}">
                    <div class="mb-3">
                        <textarea class="form-control" name="contenu"
                                  rows="4" required
                                  placeholder="Rédigez votre réponse..."></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-send"></i> Publier la réponse
                    </button>
                </form>
            </div>
        </div>
    </c:if>

    <c:if test="${empty sessionScope.utilisateur}">
        <div class="alert alert-info mt-4">
            <i class="bi bi-info-circle"></i>
            <a href="${pageContext.request.contextPath}/login">Connectez-vous</a>
            pour répondre à cette question.
        </div>
    </c:if>

</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>