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
                <span class="badge bg-secondary">${cours.statut}</span>
            </div>
        </div>

        <!-- Bouton soumettre (si brouillon) -->
        <c:if test="${cours.statut == 'BROUILLON' || cours.statut == 'REJETE'}">
            <form method="post" action="${pageContext.request.contextPath}/instructor/course/curriculum"
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

    <div class="row g-4">

        <!-- Sections existantes -->
        <div class="col-lg-8">
            <h4 class="mb-3"><i class="bi bi-list-ol"></i> Curriculum</h4>

            <c:choose>
                <c:when test="${empty sections}">
                    <div class="alert alert-info">
                        <i class="bi bi-info-circle"></i>
                        Aucune section pour l'instant. Ajoutez votre première section !
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="section" items="${sections}" varStatus="sIdx">
                        <div class="card border-0 shadow-sm mb-3">
                            <div class="card-header bg-light d-flex justify-content-between align-items-center">
                                <strong>
                                    <i class="bi bi-folder2"></i>
                                    Section ${sIdx.index + 1} : ${section.titre}
                                </strong>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/instructor/course/curriculum"
                                      onsubmit="return confirm('Supprimer cette section ?')">
                                    <input type="hidden" name="action" value="supprimerSection">
                                    <input type="hidden" name="sectionId" value="${section.id}">
                                    <input type="hidden" name="coursId" value="${cours.id}">
                                    <button type="submit" class="btn btn-sm btn-outline-danger">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </form>
                            </div>
                            <div class="card-body p-0">
                                <!-- Leçons existantes -->
                                <c:forEach var="lecon" items="${leconsParSection[section.id]}">
                                    <div class="d-flex align-items-center p-3 border-bottom">
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
                                        <div class="ms-auto">
                                            <form method="post"
                                                  action="${pageContext.request.contextPath}/instructor/course/curriculum"
                                                  onsubmit="return confirm('Supprimer cette leçon ?')"
                                                  class="d-inline">
                                                <input type="hidden" name="action" value="supprimerLecon">
                                                <input type="hidden" name="leconId" value="${lecon.id}">
                                                <input type="hidden" name="coursId" value="${cours.id}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </c:forEach>

                                <!-- Formulaire ajout leçon -->
                                <div class="p-3 bg-light">
                                    <p class="small fw-bold text-muted mb-2">
                                        <i class="bi bi-plus-circle"></i> Ajouter une leçon
                                    </p>
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/instructor/course/curriculum">
                                        <input type="hidden" name="action" value="ajouterLecon">
                                        <input type="hidden" name="sectionId" value="${section.id}">
                                        <input type="hidden" name="coursId" value="${cours.id}">

                                        <div class="row g-2">
                                            <div class="col-md-4">
                                                <input type="text" class="form-control form-control-sm"
                                                       name="titreLecon" required
                                                       placeholder="Titre de la leçon">
                                            </div>
                                            <div class="col-md-3">
                                                <select class="form-select form-control-sm"
                                                        name="typeLecon" id="typeLecon_${section.id}"
                                                        onchange="toggleContenu(this, ${section.id})">
                                                    <option value="TEXTE">Texte</option>
                                                    <option value="VIDEO">Vidéo (URL)</option>
                                                    <option value="QUIZ">Quiz</option>
                                                    <option value="RESSOURCE">Ressource</option>
                                                </select>
                                            </div>
                                            <div class="col-md-2">
                                                <input type="number" class="form-control form-control-sm"
                                                       name="dureeMin" placeholder="Durée (min)"
                                                       min="1" value="15">
                                            </div>
                                            <div class="col-md-3">
                                                <button type="submit" class="btn btn-sm btn-primary w-100">
                                                    <i class="bi bi-plus"></i> Ajouter
                                                </button>
                                            </div>
                                        </div>

                                        <div class="mt-2" id="contenu_${section.id}">
                                            <textarea class="form-control form-control-sm"
                                                      name="contenu" rows="3"
                                                      placeholder="Contenu HTML de la leçon (pour type Texte) ou URL (pour Vidéo/Ressource)"></textarea>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Sidebar : Ajouter une section -->
        <div class="col-lg-4">
            <div class="card border-0 shadow-sm">
                <div class="card-body">
                    <h5><i class="bi bi-folder-plus"></i> Ajouter une section</h5>
                    <form method="post"
                          action="${pageContext.request.contextPath}/instructor/course/curriculum">
                        <input type="hidden" name="action" value="ajouterSection">
                        <input type="hidden" name="coursId" value="${cours.id}">
                        <div class="mb-3">
                            <input type="text" class="form-control" name="titreSection"
                                   required placeholder="Ex: Introduction au cours">
                        </div>
                        <button type="submit" class="btn btn-outline-primary w-100">
                            <i class="bi bi-plus-circle"></i> Ajouter la section
                        </button>
                    </form>
                </div>
            </div>

            <!-- Info statut -->
            <div class="card border-0 shadow-sm mt-3">
                <div class="card-body">
                    <h6>Statut du cours</h6>
                    <c:choose>
                        <c:when test="${cours.statut == 'BROUILLON'}">
                            <p class="text-muted small">
                                <i class="bi bi-info-circle"></i>
                                Votre cours est en brouillon. Ajoutez vos sections et leçons,
                                puis soumettez-le pour validation par l'administrateur.
                            </p>
                        </c:when>
                        <c:when test="${cours.statut == 'EN_ATTENTE_VALIDATION'}">
                            <p class="text-warning small">
                                <i class="bi bi-clock"></i>
                                Votre cours est en attente de validation.
                                L'administrateur va l'examiner prochainement.
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
                                Votre cours a été rejeté. Modifiez-le et soumettez à nouveau.
                            </p>
                        </c:when>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function toggleContenu(select, sectionId) {
    const contenuDiv = document.getElementById('contenu_' + sectionId);
    const textarea = contenuDiv.querySelector('textarea');
    if (select.value === 'QUIZ') {
        contenuDiv.style.display = 'none';
        textarea.required = false;
    } else if (select.value === 'VIDEO' || select.value === 'RESSOURCE') {
        contenuDiv.style.display = 'block';
        textarea.placeholder = 'URL de la vidéo ou du fichier';
        textarea.required = false;
    } else {
        contenuDiv.style.display = 'block';
        textarea.placeholder = 'Contenu HTML de la leçon';
        textarea.required = false;
    }
}
</script>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>