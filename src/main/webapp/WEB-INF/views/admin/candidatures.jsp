<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Candidatures instructeur"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="d-flex align-items-center">
            <a href="${pageContext.request.contextPath}/admin/dashboard"
               class="btn btn-outline-secondary me-3">
                <i class="bi bi-arrow-left"></i>
            </a>
            <h2 class="mb-0">
                <i class="bi bi-person-video3"></i> Candidatures instructeur
                <c:if test="${nbEnAttente > 0}">
                    <span class="badge bg-warning text-dark ms-2">${nbEnAttente} en attente</span>
                </c:if>
            </h2>
        </div>
    </div>

    <!-- Messages -->
    <c:if test="${not empty param.succes}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="bi bi-check-circle"></i> ${param.succes}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty param.erreur}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="bi bi-exclamation-circle"></i> ${param.erreur}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Filtres par statut -->
    <div class="d-flex gap-2 mb-4 flex-wrap">
        <a href="${pageContext.request.contextPath}/admin/candidatures"
           class="btn btn-sm ${empty filtreStatut ? 'btn-primary' : 'btn-outline-secondary'}">
            Toutes
        </a>
        <a href="${pageContext.request.contextPath}/admin/candidatures?statut=EN_ATTENTE"
           class="btn btn-sm ${filtreStatut == 'EN_ATTENTE' ? 'btn-warning' : 'btn-outline-warning'}">
            <i class="bi bi-clock"></i> En attente
        </a>
        <a href="${pageContext.request.contextPath}/admin/candidatures?statut=APPROUVEE"
           class="btn btn-sm ${filtreStatut == 'APPROUVEE' ? 'btn-success' : 'btn-outline-success'}">
            <i class="bi bi-check-circle"></i> Approuvées
        </a>
        <a href="${pageContext.request.contextPath}/admin/candidatures?statut=REJETEE"
           class="btn btn-sm ${filtreStatut == 'REJETEE' ? 'btn-danger' : 'btn-outline-danger'}">
            <i class="bi bi-x-circle"></i> Refusées
        </a>
    </div>

    <!-- Liste des candidatures -->
    <c:choose>
        <c:when test="${empty candidatures}">
            <div class="alert alert-info">
                <i class="bi bi-info-circle"></i> Aucune candidature trouvée.
            </div>
        </c:when>
        <c:otherwise>
            <c:forEach var="cand" items="${candidatures}">
                <c:set var="candidat" value="${candidats[cand.utilisateurId]}"/>
                <div class="card border-0 shadow-sm mb-3">
                    <div class="card-body p-4">

                        <div class="d-flex justify-content-between align-items-start mb-3">

                            <!-- Infos candidat -->
                            <div class="d-flex align-items-center gap-3">
                                <c:choose>
                                    <c:when test="${not empty candidat.avatarUrl}">
                                        <img src="${pageContext.request.contextPath}/${candidat.avatarUrl}"
                                             class="rounded-circle"
                                             style="width:50px;height:50px;object-fit:cover;"
                                             alt="Avatar">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="rounded-circle bg-primary d-flex align-items-center
                                                    justify-content-center text-white fw-bold"
                                             style="width:50px;height:50px;font-size:1.2rem;">
                                            ${fn:substring(candidat.prenom, 0, 1)}${fn:substring(candidat.nom, 0, 1)}
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                <div>
                                    <h6 class="mb-0 fw-bold">
                                        ${candidat.prenom} ${candidat.nom}
                                    </h6>
                                    <small class="text-muted">${candidat.email}</small>
                                </div>
                            </div>

                            <!-- Badge statut -->
                            <c:choose>
                                <c:when test="${cand.statut == 'EN_ATTENTE'}">
                                    <span class="badge bg-warning text-dark">
                                        <i class="bi bi-clock"></i> En attente
                                    </span>
                                </c:when>
                                <c:when test="${cand.statut == 'APPROUVEE'}">
                                    <span class="badge bg-success">
                                        <i class="bi bi-check-circle"></i> Approuvée
                                    </span>
                                </c:when>
                                <c:when test="${cand.statut == 'REJETEE'}">
                                    <span class="badge bg-danger">
                                        <i class="bi bi-x-circle"></i> Refusée
                                    </span>
                                </c:when>
                            </c:choose>
                        </div>

                        <!-- Contenu de la candidature -->
                        <div class="row g-3 mb-3">
                            <div class="col-md-4">
                                <p class="text-muted small mb-1 fw-bold">
                                    <i class="bi bi-calendar"></i> Date de soumission
                                </p>
                                <p class="mb-0">${cand.dateSoumission}</p>
                            </div>
                            <div class="col-md-8">
                                <p class="text-muted small mb-1 fw-bold">
                                    <i class="bi bi-star"></i> Domaine(s) d'expertise
                                </p>
                                <p class="mb-0">${cand.expertise}</p>
                            </div>
                        </div>

                        <div class="mb-3">
                            <p class="text-muted small mb-1 fw-bold">
                                <i class="bi bi-chat-text"></i> Lettre de motivation
                            </p>
                            <div class="bg-light rounded p-3 small">
                                ${cand.motivation}
                            </div>
                        </div>

                        <%-- MODIFIÉ : delims="|" — fiable, pas de problème avec les caractères spéciaux --%>
                        <c:if test="${not empty cand.cvUrl}">
                            <div class="mb-3">
                                <p class="text-muted small mb-1 fw-bold">
                                    <i class="bi bi-paperclip"></i> CV / Portfolio / Liens
                                </p>
                                <div>
                                    <c:forTokens var="element"
                                                 items="${cand.cvUrl}"
                                                 delims="|">
                                        <c:set var="el" value="${fn:trim(element)}"/>
                                        <c:if test="${not empty el}">
                                            <c:choose>
                                                <c:when test="${fn:startsWith(el, 'uploads/')}">
                                                    <a href="${pageContext.request.contextPath}/${el}"
                                                       target="_blank"
                                                       class="btn btn-sm btn-outline-primary me-1 mb-1">
                                                        <i class="bi bi-file-earmark-arrow-down"></i>
                                                        Télécharger le CV
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${el}" target="_blank"
                                                       class="btn btn-sm btn-outline-info me-1 mb-1">
                                                        <i class="bi bi-box-arrow-up-right"></i>
                                                        ${el}
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </c:forTokens>
                                </div>
                            </div>
                        </c:if>

                        <!-- Biographie du candidat -->
                        <c:if test="${not empty candidat.biographie}">
                            <div class="mb-3">
                                <p class="text-muted small mb-1 fw-bold">
                                    <i class="bi bi-person"></i> Biographie
                                </p>
                                <p class="small text-muted mb-0">${candidat.biographie}</p>
                            </div>
                        </c:if>

                        <!-- Commentaire admin -->
                        <c:if test="${not empty cand.commentaireAdmin}">
                            <div class="alert alert-secondary py-2 small mb-3">
                                <i class="bi bi-shield-check"></i>
                                <strong>Commentaire admin :</strong> ${cand.commentaireAdmin}
                            </div>
                        </c:if>

                        <!-- Actions si EN_ATTENTE -->
                        <c:if test="${cand.statut == 'EN_ATTENTE'}">
                            <hr>
                            <div class="row g-2">
                                <div class="col-md-6">
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/candidatures"
                                          onsubmit="return confirm('Approuver cette candidature et promouvoir en instructeur ?')">
                                        <input type="hidden" name="action" value="approuver">
                                        <input type="hidden" name="candidatureId" value="${cand.id}">
                                        <div class="input-group input-group-sm">
                                            <input type="text" class="form-control"
                                                   name="commentaire"
                                                   placeholder="Commentaire (optionnel)">
                                            <button type="submit" class="btn btn-success">
                                                <i class="bi bi-check-lg"></i> Approuver
                                            </button>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-md-6">
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/candidatures"
                                          onsubmit="return confirm('Rejeter cette candidature ?')">
                                        <input type="hidden" name="action" value="rejeter">
                                        <input type="hidden" name="candidatureId" value="${cand.id}">
                                        <div class="input-group input-group-sm">
                                            <input type="text" class="form-control"
                                                   name="commentaire"
                                                   placeholder="Motif du refus (recommandé)">
                                            <button type="submit" class="btn btn-danger">
                                                <i class="bi bi-x-lg"></i> Rejeter
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </c:if>

                        <!-- Bouton rétrograder si APPROUVEE -->
                        <c:if test="${cand.statut == 'APPROUVEE'}">
                            <hr>
                            <div class="d-flex align-items-center gap-3">
                                <small class="text-muted">
                                    <i class="bi bi-info-circle"></i>
                                    Cet utilisateur est actuellement instructeur.
                                </small>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/admin/candidatures"
                                      onsubmit="return confirm('Rétrograder cet instructeur en apprenant ? Il perdra accès au dashboard instructeur et pourra repostuler.')">
                                    <input type="hidden" name="action" value="retrograder">
                                    <input type="hidden" name="candidatureId" value="${cand.id}">
                                    <button type="submit" class="btn btn-sm btn-outline-warning">
                                        <i class="bi bi-arrow-down-circle"></i> Rétrograder en apprenant
                                    </button>
                                </form>
                            </div>
                        </c:if>

                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>
