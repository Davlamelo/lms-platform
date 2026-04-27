<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Curriculum - ${cours.titre}"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4">

    <!-- En-tête -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="d-flex align-items-center">
            <a href="${pageContext.request.contextPath}/instructor/dashboard"
               class="btn btn-outline-secondary me-3">
                <i class="bi bi-arrow-left"></i>
            </a>
            <div>
                <h2 class="mb-0">${cours.titre}</h2>
                <c:choose>
                    <c:when test="${cours.statut == 'PUBLIE'}">
                        <span class="badge bg-success">${cours.statut}</span>
                    </c:when>
                    <c:when test="${cours.statut == 'EN_ATTENTE_VALIDATION'}">
                        <span class="badge bg-warning text-dark">${cours.statut}</span>
                    </c:when>
                    <c:when test="${cours.statut == 'REJETE'}">
                        <span class="badge bg-danger">${cours.statut}</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge bg-secondary">${cours.statut}</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Bouton soumettre -->
        <c:if test="${cours.statut == 'BROUILLON' || cours.statut == 'REJETE'}">
            <form method="post"
                  action="${pageContext.request.contextPath}/instructor/course/curriculum"
                  onsubmit="return confirm('Soumettre ce cours pour validation ?')">
                <input type="hidden" name="action" value="soumettre">
                <input type="hidden" name="coursId" value="${cours.id}">
                <button type="submit" class="btn btn-success">
                    <i class="bi bi-send"></i> Soumettre pour validation
                </button>
            </form>
        </c:if>
    </div>

    <!-- Alertes -->
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

    <div class="row g-4">

        <!-- Sections et leçons -->
        <div class="col-lg-8">
            <h4 class="mb-3">
                <i class="bi bi-list-ol"></i> Curriculum
            </h4>

            <c:choose>
                <c:when test="${empty sections}">
                    <div class="alert alert-info">
                        <i class="bi bi-info-circle"></i>
                        Aucune section. Ajoutez votre première section !
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="section" items="${sections}" varStatus="sIdx">
                        <div class="card border-0 shadow-sm mb-3">

                            <!-- En-tête section -->
                            <div class="card-header bg-light d-flex
                                        justify-content-between align-items-center">
                                <strong>
                                    <i class="bi bi-folder2"></i>
                                    Section ${sIdx.index + 1} : ${section.titre}
                                </strong>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/instructor/course/curriculum"
                                      onsubmit="return confirm('Supprimer cette section et toutes ses leçons ?')">
                                    <input type="hidden" name="action"
                                           value="supprimerSection">
                                    <input type="hidden" name="sectionId"
                                           value="${section.id}">
                                    <input type="hidden" name="coursId"
                                           value="${cours.id}">
                                    <button type="submit"
                                            class="btn btn-sm btn-outline-danger">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </form>
                            </div>

                            <div class="card-body p-0">

                                <!-- Leçons existantes -->
                                <c:forEach var="lecon"
                                           items="${leconsParSection[section.id]}">
                                    <div class="d-flex align-items-center
                                                p-3 border-bottom">

                                        <!-- Icône type -->
                                        <c:choose>
                                            <c:when test="${lecon.typeLecon == 'VIDEO'}">
                                                <i class="bi bi-play-circle text-primary me-2"></i>
                                            </c:when>
                                            <c:when test="${lecon.typeLecon == 'QUIZ'}">
                                                <i class="bi bi-question-circle text-warning me-2"></i>
                                            </c:when>
                                            <c:when test="${lecon.typeLecon == 'RESSOURCE'}">
                                                <i class="bi bi-file-earmark text-info me-2"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="bi bi-file-text text-secondary me-2"></i>
                                            </c:otherwise>
                                        </c:choose>

                                        <span>${lecon.titre}</span>
                                        <span class="badge bg-light text-dark ms-2">
                                            ${lecon.typeLecon}
                                        </span>
                                        <span class="text-muted ms-2 small">
                                            ${lecon.dureeMin} min
                                        </span>

                                        <div class="ms-auto d-flex gap-2">

                                            <!-- Bouton éditer quiz -->
                                            <c:if test="${lecon.typeLecon == 'QUIZ'}">
                                                <a href="${pageContext.request.contextPath}/instructor/quiz/edit?leconId=${lecon.id}&coursId=${cours.id}"
                                                   class="btn btn-sm btn-outline-warning">
                                                    <i class="bi bi-question-circle"></i>
                                                    Quiz
                                                </a>
                                            </c:if>

                                            <!-- Bouton modifier leçon -->
                                            <button type="button"
                                                    class="btn btn-sm btn-outline-primary"
                                                    data-bs-toggle="modal"
                                                    data-bs-target="#modalEditerLecon${lecon.id}">
                                                <i class="bi bi-pencil"></i>
                                            </button>

                                            <!-- Bouton supprimer -->
                                            <form method="post"
                                                  action="${pageContext.request.contextPath}/instructor/course/curriculum"
                                                  onsubmit="return confirm('Supprimer cette leçon ?')"
                                                  class="d-inline">
                                                <input type="hidden" name="action"
                                                       value="supprimerLecon">
                                                <input type="hidden" name="leconId"
                                                       value="${lecon.id}">
                                                <input type="hidden" name="coursId"
                                                       value="${cours.id}">
                                                <button type="submit"
                                                        class="btn btn-sm btn-outline-danger">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </div>
                                    </div>

                                    <!-- Modal modification leçon -->
                                    <div class="modal fade"
                                         id="modalEditerLecon${lecon.id}"
                                         tabindex="-1">
                                        <div class="modal-dialog modal-lg">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title">
                                                        <i class="bi bi-pencil"></i>
                                                        Modifier la leçon
                                                    </h5>
                                                    <button type="button"
                                                            class="btn-close"
                                                            data-bs-dismiss="modal">
                                                    </button>
                                                </div>
                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/instructor/course/curriculum">
                                                    <input type="hidden" name="action"
                                                           value="modifierLecon">
                                                    <input type="hidden" name="leconId"
                                                           value="${lecon.id}">
                                                    <input type="hidden" name="coursId"
                                                           value="${cours.id}">
                                                    <div class="modal-body">

                                                        <div class="mb-3">
                                                            <label class="form-label fw-bold">
                                                                Titre *
                                                            </label>
                                                            <input type="text"
                                                                   class="form-control"
                                                                   name="titreLecon"
                                                                   value="${lecon.titre}"
                                                                   required>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col-md-6 mb-3">
                                                                <label class="form-label fw-bold">
                                                                    Type
                                                                </label>
                                                                <select class="form-select"
                                                                        name="typeLecon">
                                                                    <option value="TEXTE"
                                                                            ${lecon.typeLecon == 'TEXTE' ? 'selected' : ''}>
                                                                        Texte
                                                                    </option>
                                                                    <option value="VIDEO"
                                                                            ${lecon.typeLecon == 'VIDEO' ? 'selected' : ''}>
                                                                        Vidéo (URL)
                                                                    </option>
                                                                    <option value="QUIZ"
                                                                            ${lecon.typeLecon == 'QUIZ' ? 'selected' : ''}>
                                                                        Quiz
                                                                    </option>
                                                                    <option value="RESSOURCE"
                                                                            ${lecon.typeLecon == 'RESSOURCE' ? 'selected' : ''}>
                                                                        Ressource
                                                                    </option>
                                                                </select>
                                                            </div>
                                                            <div class="col-md-6 mb-3">
                                                                <label class="form-label fw-bold">
                                                                    Durée (min)
                                                                </label>
                                                                <input type="number"
                                                                       class="form-control"
                                                                       name="dureeMin"
                                                                       value="${lecon.dureeMin}"
                                                                       min="1">
                                                            </div>
                                                        </div>

                                                        <div class="mb-3">
                                                            <label class="form-label fw-bold">
                                                                Contenu / URL
                                                            </label>
                                                            <textarea class="form-control"
                                                                      name="contenu"
                                                                      rows="5"
                                                                      placeholder="Contenu HTML (Texte) ou URL YouTube/Vimeo (Vidéo) ou URL fichier (Ressource)"><c:choose>
                                                                <c:when test="${lecon.typeLecon == 'VIDEO'}">${lecon.videoUrl}</c:when>
                                                                <c:when test="${lecon.typeLecon == 'RESSOURCE'}">${lecon.ressourceUrl}</c:when>
                                                                <c:otherwise>${lecon.contenuTexte}</c:otherwise>
                                                            </c:choose></textarea>
                                                            <div class="form-text">
                                                                Pour Vidéo : URL YouTube ou Vimeo
                                                                (ex: https://youtube.com/watch?v=...)
                                                            </div>
                                                        </div>

                                                        <div class="form-check">
                                                            <input type="checkbox"
                                                                   class="form-check-input"
                                                                   name="estGratuite"
                                                                   id="gratuite${lecon.id}"
                                                                   ${lecon.estGratuite ? 'checked' : ''}>
                                                            <label class="form-check-label"
                                                                   for="gratuite${lecon.id}">
                                                                Leçon gratuite (visible sans inscription)
                                                            </label>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button"
                                                                class="btn btn-secondary"
                                                                data-bs-dismiss="modal">
                                                            Annuler
                                                        </button>
                                                        <button type="submit"
                                                                class="btn btn-primary">
                                                            <i class="bi bi-save"></i>
                                                            Enregistrer
                                                        </button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Fin modal -->

                                </c:forEach>
                                <!-- Fin forEach leçons -->

                                <!-- Formulaire ajout leçon -->
                                <div class="p-3 bg-light">
                                    <p class="small fw-bold text-muted mb-2">
                                        <i class="bi bi-plus-circle"></i>
                                        Ajouter une leçon
                                    </p>
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/instructor/course/curriculum"
                                          enctype="multipart/form-data">
                                        <input type="hidden" name="action"
                                               value="ajouterLecon">
                                        <input type="hidden" name="sectionId"
                                               value="${section.id}">
                                        <input type="hidden" name="coursId"
                                               value="${cours.id}">

                                        <div class="row g-2">
                                            <div class="col-md-4">
                                                <input type="text"
                                                       class="form-control form-control-sm"
                                                       name="titreLecon" required
                                                       placeholder="Titre de la leçon">
                                            </div>
                                            <div class="col-md-3">
                                                <select class="form-select form-control-sm"
                                                        name="typeLecon"
                                                        id="typeLecon_${section.id}"
                                                        onchange="toggleContenu(this, '${section.id}')">
                                                    <option value="TEXTE">Texte</option>
                                                    <option value="VIDEO">Vidéo (URL)</option>
                                                    <option value="QUIZ">Quiz</option>
                                                    <option value="RESSOURCE">Ressource (PDF...)</option>
                                                </select>
                                            </div>
                                            <div class="col-md-2">
                                                <input type="number"
                                                       class="form-control form-control-sm"
                                                       name="dureeMin"
                                                       placeholder="Min" min="1" value="15">
                                            </div>
                                            <div class="col-md-3">
                                                <button type="submit"
                                                        class="btn btn-sm btn-primary w-100">
                                                    <i class="bi bi-plus"></i> Ajouter
                                                </button>
                                            </div>
                                        </div>

                                        <!-- Zone contenu dynamique -->
                                        <div class="mt-2"
                                             id="contenu_${section.id}">
                                            <textarea class="form-control form-control-sm"
                                                      name="contenu" rows="3"
                                                      id="textarea_${section.id}"
                                                      placeholder="Contenu HTML de la leçon"></textarea>
                                        </div>

                                        <!-- Zone upload fichier (RESSOURCE) -->
                                        <div class="mt-2 d-none"
                                             id="upload_${section.id}">
                                            <label class="form-label small fw-bold">
                                                Uploader un fichier (PDF, ZIP, DOCX...)
                                            </label>
                                            <input type="file"
                                                   class="form-control form-control-sm"
                                                   name="fichierRessource"
                                                   accept=".pdf,.zip,.docx,.xlsx,.pptx">
                                            <div class="form-text">
                                                Ou saisissez une URL ci-dessus. Max 20 Mo.
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                <!-- Fin formulaire ajout leçon -->

                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Sidebar -->
        <div class="col-lg-4">

            <!-- Ajouter une section -->
            <div class="card border-0 shadow-sm mb-3">
                <div class="card-body">
                    <h5>
                        <i class="bi bi-folder-plus"></i> Ajouter une section
                    </h5>
                    <form method="post"
                          action="${pageContext.request.contextPath}/instructor/course/curriculum">
                        <input type="hidden" name="action" value="ajouterSection">
                        <input type="hidden" name="coursId" value="${cours.id}">
                        <div class="mb-3">
                            <input type="text" class="form-control"
                                   name="titreSection" required
                                   placeholder="Ex: Introduction au cours">
                        </div>
                        <button type="submit"
                                class="btn btn-outline-primary w-100">
                            <i class="bi bi-plus-circle"></i> Ajouter la section
                        </button>
                    </form>
                </div>
            </div>

            <!-- Statut du cours -->
            <div class="card border-0 shadow-sm">
                <div class="card-body">
                    <h6 class="fw-bold">Statut du cours</h6>
                    <c:choose>
                        <c:when test="${cours.statut == 'BROUILLON'}">
                            <p class="text-muted small">
                                <i class="bi bi-info-circle"></i>
                                Votre cours est en brouillon. Ajoutez vos sections
                                et leçons, puis soumettez pour validation.
                            </p>
                        </c:when>
                        <c:when test="${cours.statut == 'EN_ATTENTE_VALIDATION'}">
                            <p class="text-warning small">
                                <i class="bi bi-clock"></i>
                                En attente de validation par l'administrateur.
                            </p>
                        </c:when>
                        <c:when test="${cours.statut == 'PUBLIE'}">
                            <p class="text-success small">
                                <i class="bi bi-check-circle"></i>
                                Votre cours est publié et visible dans le catalogue.
                            </p>
                        </c:when>
                        <c:when test="${cours.statut == 'REJETE'}">
                            <p class="text-danger small">
                                <i class="bi bi-x-circle"></i>
                                Votre cours a été rejeté. Modifiez-le et soumettez
                                à nouveau.
                            </p>
                        </c:when>
                    </c:choose>

                    <a href="${pageContext.request.contextPath}/course?slug=${cours.slug}"
                       class="btn btn-outline-secondary btn-sm w-100 mt-2"
                       target="_blank">
                        <i class="bi bi-eye"></i> Prévisualiser
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Script gestion dynamique du formulaire -->
<script>
function toggleContenu(select, sectionId) {
    const contenuDiv = document.getElementById('contenu_' + sectionId);
    const uploadDiv = document.getElementById('upload_' + sectionId);
    const textarea = document.getElementById('textarea_' + sectionId);

    // Réinitialiser
    contenuDiv.classList.remove('d-none');
    uploadDiv.classList.add('d-none');

    switch (select.value) {
        case 'QUIZ':
            contenuDiv.classList.add('d-none');
            textarea.required = false;
            break;
        case 'VIDEO':
            textarea.placeholder =
                'URL YouTube ou Vimeo (ex: https://www.youtube.com/watch?v=...)';
            textarea.required = false;
            break;
        case 'RESSOURCE':
            textarea.placeholder =
                'URL du fichier (optionnel si vous uploadez ci-dessous)';
            textarea.required = false;
            uploadDiv.classList.remove('d-none');
            break;
        default: // TEXTE
            textarea.placeholder = 'Contenu HTML de la leçon';
            textarea.required = false;
    }
}
</script>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>