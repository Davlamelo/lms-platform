<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Dashboard Admin"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="bi bi-shield-check"></i> Dashboard Administrateur</h2>
        <span class="badge bg-danger fs-6">
            <i class="bi bi-person-fill-gear"></i> Admin
        </span>
    </div>

    <!-- Stats globales -->
    <div class="row g-3 mb-4">
        <div class="col-md-2 col-sm-4">
            <div class="card border-0 shadow-sm text-center py-3">
                <div class="card-body">
                    <i class="bi bi-people fs-1 text-primary"></i>
                    <h3 class="mt-2 mb-0">${stats.totalUtilisateurs}</h3>
                    <p class="text-muted small mb-0">Utilisateurs</p>
                </div>
            </div>
        </div>
        <div class="col-md-2 col-sm-4">
            <div class="card border-0 shadow-sm text-center py-3">
                <div class="card-body">
                    <i class="bi bi-mortarboard fs-1 text-success"></i>
                    <h3 class="mt-2 mb-0">${stats.totalApprenants}</h3>
                    <p class="text-muted small mb-0">Apprenants</p>
                </div>
            </div>
        </div>
        <div class="col-md-2 col-sm-4">
            <div class="card border-0 shadow-sm text-center py-3">
                <div class="card-body">
                    <i class="bi bi-person-video3 fs-1 text-info"></i>
                    <h3 class="mt-2 mb-0">${stats.totalInstructeurs}</h3>
                    <p class="text-muted small mb-0">Instructeurs</p>
                </div>
            </div>
        </div>
        <div class="col-md-2 col-sm-4">
            <div class="card border-0 shadow-sm text-center py-3">
                <div class="card-body">
                    <i class="bi bi-collection fs-1 text-warning"></i>
                    <h3 class="mt-2 mb-0">${stats.coursPublies}</h3>
                    <p class="text-muted small mb-0">Cours publiés</p>
                </div>
            </div>
        </div>
        <div class="col-md-2 col-sm-4">
            <div class="card border-0 shadow-sm text-center py-3 border-warning">
                <div class="card-body">
                    <i class="bi bi-clock fs-1 text-warning"></i>
                    <h3 class="mt-2 mb-0 text-warning">${stats.coursEnAttente}</h3>
                    <p class="text-muted small mb-0">En attente</p>
                </div>
            </div>
        </div>
        <div class="col-md-2 col-sm-4">
            <div class="card border-0 shadow-sm text-center py-3">
                <div class="card-body">
                    <i class="bi bi-journal-check fs-1 text-secondary"></i>
                    <h3 class="mt-2 mb-0">${stats.totalInscriptions}</h3>
                    <p class="text-muted small mb-0">Inscriptions</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Navigation rapide -->
    <div class="row g-3 mb-4">
        <div class="col-md-3">
            <a href="${pageContext.request.contextPath}/admin/courses"
               class="card border-0 shadow-sm text-decoration-none h-100">
                <div class="card-body text-center py-4">
                    <i class="bi bi-collection-play fs-1 text-primary"></i>
                    <h6 class="mt-2 text-dark">Modérer les cours</h6>
                    <c:if test="${stats.coursEnAttente > 0}">
                        <span class="badge bg-warning text-dark">
                            ${stats.coursEnAttente} en attente
                        </span>
                    </c:if>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a href="${pageContext.request.contextPath}/admin/users"
               class="card border-0 shadow-sm text-decoration-none h-100">
                <div class="card-body text-center py-4">
                    <i class="bi bi-people fs-1 text-success"></i>
                    <h6 class="mt-2 text-dark">Gérer les utilisateurs</h6>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a href="${pageContext.request.contextPath}/admin/categories"
               class="card border-0 shadow-sm text-decoration-none h-100">
                <div class="card-body text-center py-4">
                    <i class="bi bi-grid fs-1 text-info"></i>
                    <h6 class="mt-2 text-dark">Gérer les catégories</h6>
                </div>
            </a>
        </div>

        <%-- AJOUTÉ : carte navigation candidatures instructeur --%>
        <div class="col-md-3">
            <a href="${pageContext.request.contextPath}/admin/candidatures"
               class="card border-0 shadow-sm text-decoration-none h-100">
                <div class="card-body text-center py-4">
                    <i class="bi bi-person-video3 fs-1 text-purple" style="color: #5a2d82;"></i>
                    <h6 class="mt-2 text-dark">Candidatures instructeur</h6>
                    <c:if test="${not empty candidaturesEnAttente}">
                        <span class="badge bg-warning text-dark">
                            ${candidaturesEnAttente.size()} en attente
                        </span>
                    </c:if>
                </div>
            </a>
        </div>

    </div>

    <!-- Cours en attente de validation -->
    <c:if test="${not empty coursEnAttente}">
        <div class="card border-0 shadow-sm border-start border-warning border-4 mb-4">
            <div class="card-body">
                <h5 class="card-title">
                    <i class="bi bi-clock text-warning"></i>
                    Cours en attente de validation (${coursEnAttente.size()})
                </h5>
                <div class="table-responsive">
                    <table class="table table-sm align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Cours</th>
                                <th>Niveau</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${coursEnAttente}">
                                <tr>
                                    <td>
                                        <strong>${c.titre}</strong>
                                        <br>
                                        <small class="text-muted">${c.descriptionCourte}</small>
                                    </td>
                                    <td>
                                        <span class="badge bg-secondary">${c.niveau}</span>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/course?slug=${c.slug}"
                                               class="btn btn-sm btn-outline-info me-1" target="_blank">
                                                <i class="bi bi-eye"></i> Aperçu
                                        </a>
                                        <form method="post"
                                              action="${pageContext.request.contextPath}/admin/courses"
                                              class="d-inline">
                                            <input type="hidden" name="coursId" value="${c.id}">
                                            <input type="hidden" name="action" value="valider">
                                            <button type="submit" class="btn btn-sm btn-success me-1">
                                                <i class="bi bi-check"></i> Valider
                                            </button>
                                        </form>
                                        <form method="post"
                                              action="${pageContext.request.contextPath}/admin/courses"
                                              class="d-inline">
                                            <input type="hidden" name="coursId" value="${c.id}">
                                            <input type="hidden" name="action" value="rejeter">
                                            <button type="submit" class="btn btn-sm btn-danger">
                                                <i class="bi bi-x"></i> Rejeter
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${empty coursEnAttente}">
        <div class="alert alert-success">
            <i class="bi bi-check-circle"></i> Aucun cours en attente de validation. 🎉
        </div>
    </c:if>

    <%-- AJOUTÉ : bloc candidatures instructeur en attente (aperçu rapide) --%>
    <c:if test="${not empty candidaturesEnAttente}">
        <div class="card border-0 shadow-sm border-start border-4 mt-4"
             style="border-color: #5a2d82 !important;">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="card-title mb-0">
                        <i class="bi bi-person-video3" style="color: #5a2d82;"></i>
                        Candidatures instructeur en attente (${candidaturesEnAttente.size()})
                    </h5>
                    <a href="${pageContext.request.contextPath}/admin/candidatures?statut=EN_ATTENTE"
                       class="btn btn-sm btn-outline-secondary">
                        Voir toutes <i class="bi bi-arrow-right"></i>
                    </a>
                </div>
                <div class="table-responsive">
                    <table class="table table-sm align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Candidat</th>
                                <th>Expertise</th>
                                <th>Date</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cand" items="${candidaturesEnAttente}">
                                <c:set var="candidat" value="${candidats[cand.utilisateurId]}"/>
                                <tr>
                                    <td>
                                        <strong>${candidat.prenom} ${candidat.nom}</strong>
                                        <br>
                                        <small class="text-muted">${candidat.email}</small>
                                    </td>
                                    <td>
                                        <small>${cand.expertise}</small>
                                    </td>
                                    <td>
                                        <small class="text-muted">${cand.dateSoumission}</small>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/candidatures"
                                           class="btn btn-sm btn-outline-primary">
                                            <i class="bi bi-eye"></i> Examiner
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:if>

</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>
