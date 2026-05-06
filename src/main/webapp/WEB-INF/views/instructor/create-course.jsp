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

            <%-- MODIFIÉ : ajout de enctype="multipart/form-data" pour l'upload de miniature --%>
            <form method="post"
                  action="${pageContext.request.contextPath}/instructor/course/create"
                  enctype="multipart/form-data">

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

                <%-- AJOUTÉ : champ upload miniature avec aperçu --%>
                <div class="mb-3">
                    <label for="miniature" class="form-label fw-bold">Miniature du cours</label>
                    <input type="file" class="form-control" id="miniature"
                           name="miniature" accept="image/jpeg,image/png,image/webp,image/gif">
                    <div class="form-text">
                        Format JPG, PNG, WEBP ou GIF — max 5 Mo.
                        Si absent, un fond violet sera affiché par défaut.
                    </div>
                    <div class="mt-2" id="previewMiniature" style="display: none;">
                        <img id="imgPreview" src="" alt="Aperçu miniature"
                             style="height: 120px; object-fit: cover; border-radius: 8px; border: 1px solid #dee2e6;">
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

<%-- AJOUTÉ : script d'aperçu de la miniature avant upload --%>
<script>
    document.getElementById('miniature').addEventListener('change', function (e) {
        const file = e.target.files[0];
        if (!file) return;
        const preview = document.getElementById('previewMiniature');
        const img = document.getElementById('imgPreview');
        img.src = URL.createObjectURL(file);
        preview.style.display = 'block';
    });
</script>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>
