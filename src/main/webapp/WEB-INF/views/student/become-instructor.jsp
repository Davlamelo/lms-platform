<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Devenir instructeur"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4" style="max-width: 800px;">

    <div class="d-flex align-items-center mb-4">
        <a href="${pageContext.request.contextPath}/dashboard"
           class="btn btn-outline-secondary me-3">
            <i class="bi bi-arrow-left"></i>
        </a>
        <h2 class="mb-0">
            <i class="bi bi-person-video3"></i> Devenir instructeur
        </h2>
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

    <div class="row g-4">

        <div class="col-lg-8">

            <!-- Statut de la dernière candidature -->
            <c:if test="${not empty derniereCandidature}">
                <div class="card border-0 shadow-sm mb-4">
                    <div class="card-body p-4">
                        <h5 class="mb-3">
                            <i class="bi bi-clipboard-check"></i> Statut de votre candidature
                        </h5>

                        <c:choose>
                            <c:when test="${derniereCandidature.statut == 'EN_ATTENTE'}">
                                <div class="alert alert-warning mb-3">
                                    <i class="bi bi-clock"></i>
                                    <strong>En cours d'examen</strong> — Notre équipe examine
                                    votre candidature. Vous serez notifié sous 48h.
                                </div>
                            </c:when>
                            <c:when test="${derniereCandidature.statut == 'APPROUVEE'}">
                                <div class="alert alert-success mb-3">
                                    <i class="bi bi-check-circle"></i>
                                    <strong>Candidature approuvée !</strong> Vous êtes maintenant
                                    instructeur. Rendez-vous sur votre dashboard.
                                </div>
                            </c:when>
                            <c:when test="${derniereCandidature.statut == 'REJETEE'}">
                                <div class="alert alert-danger mb-3">
                                    <i class="bi bi-x-circle"></i>
                                    <strong>Candidature refusée.</strong>
                                    <c:if test="${not empty derniereCandidature.commentaireAdmin}">
                                        <br>Motif : <em>${derniereCandidature.commentaireAdmin}</em>
                                    </c:if>
                                    <br><small class="text-muted">
                                        Vous pouvez soumettre une nouvelle candidature ci-dessous.
                                    </small>
                                </div>
                            </c:when>
                        </c:choose>

                        <div class="text-muted small mb-2">
                            <i class="bi bi-calendar"></i>
                            Soumise le : ${derniereCandidature.dateSoumission}
                        </div>
                        <div class="mb-2">
                            <strong>Expertise :</strong> ${derniereCandidature.expertise}
                        </div>

                        <%-- MODIFIÉ : delims="|" — simple, fiable, sans ambiguïté --%>
                        <c:if test="${not empty derniereCandidature.cvUrl}">
                            <div class="mb-2">
                                <strong>Documents / Liens joints :</strong>
                                <div class="mt-1">
                                    <c:forTokens var="element"
                                                 items="${derniereCandidature.cvUrl}"
                                                 delims="|">
                                        <c:set var="el" value="${fn:trim(element)}"/>
                                        <c:if test="${not empty el}">
                                            <c:choose>
                                                <c:when test="${fn:startsWith(el, 'uploads/')}">
                                                    <a href="${pageContext.request.contextPath}/${el}"
                                                       target="_blank"
                                                       class="btn btn-sm btn-outline-primary me-1 mb-1">
                                                        <i class="bi bi-file-earmark-arrow-down"></i>
                                                        Télécharger le fichier
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

                        <p class="text-muted mt-2 mb-0 small">${derniereCandidature.motivation}</p>
                    </div>
                </div>
            </c:if>

            <!-- Formulaire de candidature -->
            <c:if test="${peutSoumettre}">
                <div class="card border-0 shadow-sm">
                    <div class="card-body p-4">
                        <h5 class="mb-1">
                            <i class="bi bi-send"></i>
                            <c:choose>
                                <c:when test="${not empty derniereCandidature}">
                                    Soumettre une nouvelle candidature
                                </c:when>
                                <c:otherwise>
                                    Soumettre une candidature
                                </c:otherwise>
                            </c:choose>
                        </h5>
                        <p class="text-muted small mb-4">
                            Remplissez ce formulaire pour rejoindre notre équipe d'instructeurs.
                        </p>

                        <form method="post"
                              action="${pageContext.request.contextPath}/become-instructor"
                              enctype="multipart/form-data">

                            <div class="mb-3">
                                <label class="form-label fw-bold">
                                    Domaine(s) d'expertise *
                                </label>
                                <input type="text" class="form-control"
                                       name="expertise" required maxlength="255"
                                       placeholder="Ex: Python, Machine Learning, Data Science">
                                <div class="form-text">
                                    Listez vos domaines de compétences principaux.
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold">
                                    Lettre de motivation *
                                </label>
                                <textarea class="form-control" name="motivation"
                                          rows="6" required minlength="50"
                                          id="motivationTextarea"
                                          placeholder="Expliquez pourquoi vous souhaitez devenir instructeur, votre expérience pédagogique, et ce que vous souhaitez enseigner..."></textarea>
                                <div class="d-flex justify-content-between form-text">
                                    <span>Minimum 50 caractères.</span>
                                    <span id="compteurChars">0 caractère(s)</span>
                                </div>
                            </div>

                            <!-- Section preuves d'expertise -->
                            <div class="card bg-light border-0 p-3 mb-4">
                                <p class="fw-bold small mb-3">
                                    <i class="bi bi-paperclip"></i>
                                    Preuves d'expertise
                                    <span class="text-muted fw-normal">(optionnel — au moins un recommandé)</span>
                                </p>

                                <div class="mb-3">
                                    <label class="form-label small fw-bold">
                                        Fichier — CV, portfolio, diplôme... (PDF, DOC, DOCX — max 10 Mo)
                                    </label>
                                    <input type="file" class="form-control form-control-sm"
                                           name="cvFichier" id="cvFichier"
                                           accept=".pdf,.doc,.docx">
                                    <div class="form-text">
                                        Vous pouvez joindre un fichier ET des liens externes simultanément.
                                    </div>
                                </div>

                                <div class="mb-0">
                                    <label class="form-label small fw-bold">
                                        Liens externes
                                        <span class="text-muted fw-normal">(un par ligne)</span>
                                    </label>
                                    <textarea class="form-control form-control-sm"
                                              name="liensExternesRaw"
                                              rows="4"
                                              placeholder="https://linkedin.com/in/votrenom&#10;https://github.com/votrenom&#10;https://monportfolio.com"></textarea>
                                    <div class="form-text">
                                        LinkedIn, GitHub, portfolio, Behance, certifications, etc.
                                        Un lien par ligne.
                                    </div>
                                </div>
                            </div>

                            <button type="submit" class="btn btn-primary w-100">
                                <i class="bi bi-send"></i> Soumettre ma candidature
                            </button>
                        </form>
                    </div>
                </div>
            </c:if>

            <!-- Si EN_ATTENTE : pas de nouveau formulaire -->
            <c:if test="${not empty derniereCandidature && derniereCandidature.statut == 'EN_ATTENTE'}">
                <div class="text-center text-muted mt-3">
                    <small>
                        <i class="bi bi-info-circle"></i>
                        Vous ne pouvez pas soumettre une nouvelle candidature
                        tant que la précédente est en cours d'examen.
                    </small>
                </div>
            </c:if>

        </div>

        <!-- Sidebar -->
        <div class="col-lg-4">
            <div class="card border-0 shadow-sm mb-3"
                 style="background: linear-gradient(135deg, #5a2d82, #8e44ad);">
                <div class="card-body text-white p-4">
                    <h6 class="fw-bold mb-3">
                        <i class="bi bi-star-fill"></i> Pourquoi enseigner ici ?
                    </h6>
                    <ul class="list-unstyled small mb-0">
                        <li class="mb-2"><i class="bi bi-check-circle-fill me-2"></i>Partagez vos connaissances</li>
                        <li class="mb-2"><i class="bi bi-check-circle-fill me-2"></i>Créez vos cours à votre rythme</li>
                        <li class="mb-2"><i class="bi bi-check-circle-fill me-2"></i>Outils pédagogiques complets</li>
                        <li class="mb-2"><i class="bi bi-check-circle-fill me-2"></i>Suivi des apprenants</li>
                        <li><i class="bi bi-check-circle-fill me-2"></i>Communauté bienveillante</li>
                    </ul>
                </div>
            </div>

            <div class="card border-0 shadow-sm mb-3">
                <div class="card-body p-4">
                    <h6 class="fw-bold mb-3">
                        <i class="bi bi-question-circle"></i> Comment ça marche ?
                    </h6>
                    <ol class="small text-muted ps-3 mb-0">
                        <li class="mb-2">Soumettez votre candidature</li>
                        <li class="mb-2">Examen sous 48h par notre équipe</li>
                        <li class="mb-2">Réponse avec commentaire</li>
                        <li>Accès immédiat si approuvé</li>
                    </ol>
                </div>
            </div>

            <div class="card border-0 shadow-sm">
                <div class="card-body p-4">
                    <h6 class="fw-bold mb-2">
                        <i class="bi bi-paperclip"></i> Quoi joindre ?
                    </h6>
                    <ul class="small text-muted ps-3 mb-0">
                        <li class="mb-1">CV ou résumé professionnel</li>
                        <li class="mb-1">Profil LinkedIn</li>
                        <li class="mb-1">Portfolio ou GitHub</li>
                        <li class="mb-1">Diplômes ou certifications</li>
                        <li>Tout ce qui témoigne de votre expertise</li>
                    </ul>
                </div>
            </div>
        </div>

    </div>
</div>

<script>
    const textarea = document.getElementById('motivationTextarea');
    const compteur = document.getElementById('compteurChars');
    if (textarea && compteur) {
        textarea.addEventListener('input', function () {
            const nb = textarea.value.length;
            compteur.textContent = nb + ' caractère(s)';
            compteur.style.color = nb < 50 ? '#dc3545' : '#198754';
        });
    }
</script>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>
