<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Créer un cours"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4" style="max-width: 800px;">
    <div class="d-flex align-items-center mb-4">
        <a href="${pageContext.request.contextPath}/instructor/dashboard"
           class="btn btn-outline-secondary me-3">
            <i class="bi bi-arrow-left"></i>
        </a>
        <h2 class="mb-0"><i class="bi bi-plus-circle"></i> Créer un nouveau cours</h2>
    </div>

    <c:if test="${not empty erreur}">
        <div class="alert alert-danger">
            <i class="bi bi-exclamation-circle"></i> ${erreur}
        </div>
    </c:if>

    <div class="card border-0 shadow-sm">
        <div class="card-body p-4">
            <form method="post" action="${pageContext.request.contextPath}/instructor/course/create">

                <div class="mb-3">
                    <label for="titre" class="form-label fw-bold">Titre du cours *</label>
                    <input type="text" class="form-control" id="titre" name="titre"
                           value="${titre}" required maxlength="200"
                           placeholder="Ex: Python pour la Data Science">
                    <div class="form-text">Un titre clair et accrocheur (max 200 caractères)</div>
                </div>

                <div class="mb-3">
                    <label for="descriptionCourte" class="form-label fw-bold">
                        Description courte *
                    </label>
                    <input type="text" class="form-control" id="descriptionCourte"
                           name="descriptionCourte" value="${descriptionCourte}"
                           required maxlength="500"
                           placeholder="Ce que l'apprenant va apprendre en 1-2 phrases">
                    <div class="form-text">Affiché dans le catalogue (max 500 caractères)</div>
                </div>

                <div class="mb-3">
                    <label for="descriptionLongue" class="form-label fw-bold">
                        Description complète
                    </label>
                    <textarea class="form-control" id="descriptionLongue"
                              name="descriptionLongue" rows="6"
                              placeholder="Description détaillée : objectifs, prérequis, public cible...">${descriptionLongue}</textarea>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="categorieId" class="form-label fw-bold">Catégorie *</label>
                        <select class="form-select" id="categorieId" name="categorieId" required>
                            <option value="">-- Choisir une catégorie --</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat.id}">${cat.nom}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="niveau" class="form-label fw-bold">Niveau *</label>
                        <select class="form-select" id="niveau" name="niveau" required>
                            <option value="DEBUTANT">Débutant</option>
                            <option value="INTERMEDIAIRE">Intermédiaire</option>
                            <option value="AVANCE">Avancé</option>
                        </select>
                    </div>
                </div>

                <div class="d-flex justify-content-end gap-2 mt-3">
                    <a href="${pageContext.request.contextPath}/instructor/dashboard"
                       class="btn btn-outline-secondary">Annuler</a>
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-arrow-right"></i> Créer et ajouter le contenu
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>