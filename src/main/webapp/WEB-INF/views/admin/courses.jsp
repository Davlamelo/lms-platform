<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Modération des cours"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4">
    <div class="d-flex align-items-center mb-4">
        <a href="${pageContext.request.contextPath}/admin/dashboard"
           class="btn btn-outline-secondary me-3">
            <i class="bi bi-arrow-left"></i>
        </a>
        <h2 class="mb-0"><i class="bi bi-collection-play"></i> Modération des cours</h2>
    </div>

    <c:if test="${not empty param.succes}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="bi bi-check-circle"></i> ${param.succes}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-dark">
                <tr>
                    <th>Titre</th>
                    <th>Instructeur</th>
                    <th>Catégorie</th>
                    <th>Niveau</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="c" items="${tousCours}">
                    <tr>
                        <td>
                            <strong>${c.titre}</strong>
                            <br>
                            <small class="text-muted">${c.descriptionCourte}</small>
                        </td>
                        <td>${nomsInstructeurs[c.id]}</td>
                        <td><small>${c.categorieId}</small></td>
                        <td><span class="badge bg-secondary">${c.niveau}</span></td>
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
                                <c:when test="${c.statut == 'ARCHIVE'}">
                                    <span class="badge bg-dark">Archivé</span>
                                </c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${c.statut == 'EN_ATTENTE_VALIDATION'}">
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
                                    <button type="submit" class="btn btn-sm btn-danger me-1">
                                        <i class="bi bi-x"></i> Rejeter
                                    </button>
                                </form>
                            </c:if>
                            <c:if test="${c.statut == 'PUBLIE'}">
                                <form method="post"
                                      action="${pageContext.request.contextPath}/admin/courses"
                                      class="d-inline">
                                    <input type="hidden" name="coursId" value="${c.id}">
                                    <input type="hidden" name="action" value="archiver">
                                    <button type="submit" class="btn btn-sm btn-outline-secondary"
                                            onclick="return confirm('Archiver ce cours ?')">
                                        <i class="bi bi-archive"></i> Archiver
                                    </button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>