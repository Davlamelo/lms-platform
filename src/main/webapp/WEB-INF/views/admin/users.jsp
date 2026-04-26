<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Gestion des utilisateurs"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4">
    <div class="d-flex align-items-center mb-4">
        <a href="${pageContext.request.contextPath}/admin/dashboard"
           class="btn btn-outline-secondary me-3">
            <i class="bi bi-arrow-left"></i>
        </a>
        <h2 class="mb-0"><i class="bi bi-people"></i> Gestion des utilisateurs</h2>
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
                    <th>Nom</th>
                    <th>Email</th>
                    <th>Rôle</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${utilisateurs}">
                    <tr class="${u.actif ? '' : 'table-secondary'}">
                        <td>
                            <strong>${u.prenom} ${u.nom}</strong>
                        </td>
                        <td>${u.email}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.role == 'ADMIN'}">
                                    <span class="badge bg-danger">Admin</span>
                                </c:when>
                                <c:when test="${u.role == 'INSTRUCTEUR'}">
                                    <span class="badge bg-primary">Instructeur</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-success">Apprenant</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${u.actif}">
                                    <span class="badge bg-success">Actif</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger">Suspendu</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${u.role != 'ADMIN'}">
                                <!-- Toggle actif/suspendu -->
                                <form method="post"
                                      action="${pageContext.request.contextPath}/admin/users"
                                      class="d-inline">
                                    <input type="hidden" name="action" value="toggleStatut">
                                    <input type="hidden" name="userId" value="${u.id}">
                                    <button type="submit"
                                            class="btn btn-sm ${u.actif ? 'btn-outline-danger' : 'btn-outline-success'} me-1">
                                        <i class="bi ${u.actif ? 'bi-slash-circle' : 'bi-check-circle'}"></i>
                                        ${u.actif ? 'Suspendre' : 'Activer'}
                                    </button>
                                </form>

                                <!-- Promouvoir instructeur (si apprenant) -->
                                <c:if test="${u.role == 'APPRENANT'}">
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/users"
                                          class="d-inline">
                                        <input type="hidden" name="action" value="promouvoir">
                                        <input type="hidden" name="userId" value="${u.id}">
                                        <button type="submit" class="btn btn-sm btn-outline-primary"
                                                onclick="return confirm('Promouvoir comme instructeur ?')">
                                            <i class="bi bi-arrow-up-circle"></i> Instructeur
                                        </button>
                                    </form>
                                </c:if>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>