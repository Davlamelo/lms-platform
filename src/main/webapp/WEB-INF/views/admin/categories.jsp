<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Gestion des catégories"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4">
    <div class="d-flex align-items-center mb-4">
        <a href="${pageContext.request.contextPath}/admin/dashboard"
           class="btn btn-outline-secondary me-3">
            <i class="bi bi-arrow-left"></i>
        </a>
        <h2 class="mb-0"><i class="bi bi-grid"></i> Gestion des catégories</h2>
    </div>

    <c:if test="${not empty param.succes}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="bi bi-check-circle"></i> ${param.succes}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${not empty erreur}">
        <div class="alert alert-danger">
            <i class="bi bi-exclamation-circle"></i> ${erreur}
        </div>
    </c:if>

    <div class="row g-4">
        <!-- Liste des catégories -->
        <div class="col-lg-8">
            <div class="card border-0 shadow-sm">
                <div class="card-body">
                    <h5>Catégories existantes</h5>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th>Icône</th>
                                    <th>Nom</th>
                                    <th>Slug</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="cat" items="${categories}">
                                    <tr>
                                        <td>
                                            <i class="bi ${cat.icone} fs-4 text-primary"></i>
                                        </td>
                                        <td><strong>${cat.nom}</strong></td>
                                        <td><code>${cat.slug}</code></td>
                                        <td>
                                            <form method="post"
                                                  action="${pageContext.request.contextPath}/admin/categories"
                                                  class="d-inline"
                                                  onsubmit="return confirm('Supprimer cette catégorie ?')">
                                                <input type="hidden" name="action" value="supprimer">
                                                <input type="hidden" name="categorieId" value="${cat.id}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger">
                                                    <i class="bi bi-trash"></i>
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
        </div>

        <!-- Formulaire ajout catégorie -->
        <div class="col-lg-4">
            <div class="card border-0 shadow-sm">
                <div class="card-body">
                    <h5><i class="bi bi-plus-circle"></i> Nouvelle catégorie</h5>
                    <form method="post"
                          action="${pageContext.request.contextPath}/admin/categories">
                        <input type="hidden" name="action" value="creer">

                        <div class="mb-3">
                            <label class="form-label fw-bold">Nom *</label>
                            <input type="text" class="form-control" name="nom"
                                   required placeholder="Ex: Cybersécurité">
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Description</label>
                            <textarea class="form-control" name="description" rows="3"
                                      placeholder="Description de la catégorie"></textarea>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Icône Bootstrap</label>
                            <input type="text" class="form-control" name="icone"
                                   placeholder="bi-shield-lock" value="bi-book">
                            <div class="form-text">
                                Voir <a href="https://icons.getbootstrap.com" target="_blank">
                                Bootstrap Icons</a>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100">
                            <i class="bi bi-plus"></i> Créer la catégorie
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>