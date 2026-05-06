<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Dashboard Instructeur"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4">

    <!-- En-tête -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2><i class="bi bi-person-video3"></i> Dashboard Instructeur</h2>
            <p class="text-muted mb-0">Bonjour, ${sessionScope.utilisateur.prenom} !</p>
        </div>
        <a href="${pageContext.request.contextPath}/instructor/course/create"
           class="btn btn-primary">
            <i class="bi bi-plus-circle"></i> Créer un cours
        </a>
    </div>

    <!-- Message succès -->
    <c:if test="${not empty param.succes}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="bi bi-check-circle"></i> ${param.succes}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- AJOUTÉ : Message erreur -->
    <c:if test="${not empty param.erreur}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="bi bi-exclamation-circle"></i> ${param.erreur}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Statistiques globales -->
    <div class="row g-3 mb-4">
        <div class="col-md-3">
            <div class="card border-0 shadow-sm text-center py-3">
                <div class="card-body">
                    <i class="bi bi-collection fs-1 text-primary"></i>
                    <h3 class="mt-2 mb-0">${stats.totalCours}</h3>
                    <p class="text-muted mb-0">Cours créés</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card border-0 shadow-sm text-center py-3">
                <div class="card-body">
                    <i class="bi bi-people fs-1 text-success"></i>
                    <h3 class="mt-2 mb-0">${stats.totalInscrits}</h3>
                    <p class="text-muted mb-0">Apprenants</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card border-0 shadow-sm text-center py-3">
                <div class="card-body">
                    <i class="bi bi-star fs-1 text-warning"></i>
                    <h3 class="mt-2 mb-0">
                        <fmt:formatNumber value="${stats.noteMoyenne}" maxFractionDigits="1"/>
                    </h3>
                    <p class="text-muted mb-0">Note moyenne</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card border-0 shadow-sm text-center py-3">
                <div class="card-body">
                    <i class="bi bi-check-circle fs-1 text-info"></i>
                    <h3 class="mt-2 mb-0">${stats.totalPublies}</h3>
                    <p class="text-muted mb-0">Cours publiés</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Liste des cours -->
    <h4 class="mb-3"><i class="bi bi-collection"></i> Mes cours</h4>

    <c:choose>
        <c:when test="${empty cours}">
            <div class="card border-0 shadow-sm">
                <div class="card-body text-center py-5">
                    <i class="bi bi-plus-circle display-1 text-muted"></i>
                    <h5 class="mt-3">Aucun cours pour l'instant</h5>
                    <p class="text-muted">Créez votre premier cours et commencez à enseigner !</p>
                    <a href="${pageContext.request.contextPath}/instructor/course/create"
                       class="btn btn-primary mt-2">
                        <i class="bi bi-plus-circle"></i> Créer mon premier cours
                    </a>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>Titre</th>
                            <th>Statut</th>
                            <th>Inscrits</th>
                            <th>Note</th>
                            <th>Leçons</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="c" items="${cours}">
                            <tr>
                                <td>
                                    <strong>${c.titre}</strong>
                                    <br>
                                    <small class="text-muted">${c.niveau}</small>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${c.statut == 'PUBLIE'}">
                                            <span class="badge bg-success">Publié</span>
                                        </c:when>
                                        <c:when test="${c.statut == 'BROUILLON'}">
                                            <span class="badge bg-secondary">Brouillon</span>
                                        </c:when>
                                        <c:when test="${c.statut == 'EN_ATTENTE_VALIDATION'}">
                                            <span class="badge bg-warning text-dark">En attente</span>
                                        </c:when>
                                        <c:when test="${c.statut == 'REJETE'}">
                                            <span class="badge bg-danger">Rejeté</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary">${c.statut}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <i class="bi bi-people text-success"></i>
                                    ${statsParCours[c.id].inscrits}
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${statsParCours[c.id].nbAvis > 0}">
                                            <i class="bi bi-star-fill text-warning"></i>
                                            <fmt:formatNumber value="${statsParCours[c.id].noteMoyenne}"
                                                              maxFractionDigits="1"/>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">—</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <i class="bi bi-book text-info"></i>
                                    ${statsParCours[c.id].nbLecons}
                                </td>

                                <%-- MODIFIÉ : colonne Actions avec retirer et supprimer --%>
                                <td>
                                    <div class="d-flex gap-1 flex-wrap">

                                        <%-- Bouton Éditer (toujours visible) --%>
                                        <a href="${pageContext.request.contextPath}/instructor/course/curriculum?coursId=${c.id}"
                                           class="btn btn-sm btn-outline-primary">
                                            <i class="bi bi-pencil"></i> Éditer
                                        </a>

                                        <%-- Bouton Voir (si publié) --%>
                                        <c:if test="${c.statut == 'PUBLIE'}">
                                            <a href="${pageContext.request.contextPath}/course?slug=${c.slug}"
                                               class="btn btn-sm btn-outline-success">
                                                <i class="bi bi-eye"></i> Voir
                                            </a>
                                        </c:if>

                                        <%-- AJOUTÉ : Bouton Retirer (si publié) --%>
                                        <c:if test="${c.statut == 'PUBLIE'}">
                                            <form method="post"
                                                  action="${pageContext.request.contextPath}/instructor/dashboard"
                                                  onsubmit="return confirm('Retirer ce cours du catalogue ? Il repassera en brouillon.')">
                                                <input type="hidden" name="action" value="retirer">
                                                <input type="hidden" name="coursId" value="${c.id}">
                                                <button type="submit" class="btn btn-sm btn-outline-warning">
                                                    <i class="bi bi-arrow-down-circle"></i> Retirer
                                                </button>
                                            </form>
                                        </c:if>

                                        <%-- AJOUTÉ : Bouton Supprimer (si brouillon ou rejeté) --%>
                                        <c:if test="${c.statut == 'BROUILLON' || c.statut == 'REJETE'}">
                                            <form method="post"
                                                  action="${pageContext.request.contextPath}/instructor/dashboard"
                                                  onsubmit="return confirm('Supprimer définitivement ce cours ? Cette action est irréversible.')">
                                                <input type="hidden" name="action" value="supprimer">
                                                <input type="hidden" name="coursId" value="${c.id}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger">
                                                    <i class="bi bi-trash"></i> Supprimer
                                                </button>
                                            </form>
                                        </c:if>

                                    </div>
                                </td>

                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>
