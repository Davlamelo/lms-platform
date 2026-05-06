<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Mon profil"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4" style="max-width: 800px;">

    <h2 class="mb-4">
        <i class="bi bi-person-circle"></i> Mon profil
    </h2>

    <!-- Messages -->
    <c:if test="${not empty param.succes}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="bi bi-check-circle"></i> ${param.succes}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty erreur}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="bi bi-exclamation-circle"></i> ${erreur}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="row g-4">

        <!-- Formulaire profil -->
        <div class="col-lg-8">
            <div class="card border-0 shadow-sm mb-4">
                <div class="card-body p-4">
                    <h5 class="mb-4">
                        <i class="bi bi-pencil"></i> Informations personnelles
                    </h5>

                    <%-- enctype="multipart/form-data" OBLIGATOIRE pour l'upload --%>
                    <form method="post"
                          action="${pageContext.request.contextPath}/profile"
                          enctype="multipart/form-data">
                        <input type="hidden" name="action" value="modifierProfil">

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-bold">Prénom *</label>
                                <input type="text" class="form-control"
                                       name="prenom"
                                       value="${profil.prenom}" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-bold">Nom *</label>
                                <input type="text" class="form-control"
                                       name="nom"
                                       value="${profil.nom}" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Email</label>
                            <input type="email" class="form-control"
                                   value="${profil.email}" disabled>
                            <div class="form-text text-muted">
                                L'email ne peut pas être modifié.
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Biographie</label>
                            <textarea class="form-control" name="biographie"
                                      rows="4"
                                      placeholder="Parlez de vous, votre parcours, vos centres d'intérêt...">${profil.biographie}</textarea>
                        </div>

                        <div class="mb-4">
                            <label class="form-label fw-bold">
                                Photo de profil
                            </label>
                            <div class="d-flex align-items-center gap-3 mb-2">
                                <%-- Avatar actuel --%>
                                <c:choose>
                                    <c:when test="${not empty profil.avatarUrl}">
                                        <img src="${pageContext.request.contextPath}/${profil.avatarUrl}"
                                             alt="Avatar"
                                             class="rounded-circle"
                                             style="width: 60px; height: 60px; object-fit: cover;">
                                    </c:when>
                                    <c:otherwise>
                                        <i class="bi bi-person-circle"
                                           style="font-size: 3.5rem; color: #5a2d82;"></i>
                                    </c:otherwise>
                                </c:choose>
                                <div class="flex-grow-1">
                                    <input type="file" class="form-control"
                                           name="avatar"
                                           accept="image/jpeg,image/png,image/gif,image/webp">
                                    <div class="form-text">
                                        JPG, PNG, GIF ou WEBP. Max 5 Mo.
                                    </div>
                                </div>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-check-circle"></i>
                            Enregistrer les modifications
                        </button>
                    </form>
                </div>
            </div>

            <!-- Changer mot de passe -->
            <div class="card border-0 shadow-sm">
                <div class="card-body p-4">
                    <h5 class="mb-4">
                        <i class="bi bi-shield-lock"></i> Changer le mot de passe
                    </h5>
                    <form method="post"
                          action="${pageContext.request.contextPath}/profile">
                        <input type="hidden" name="action" value="changerMotDePasse">

                        <div class="mb-3">
                            <label class="form-label fw-bold">
                                Mot de passe actuel *
                            </label>
                            <input type="password" class="form-control"
                                   name="ancienMotDePasse" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">
                                Nouveau mot de passe *
                            </label>
                            <input type="password" class="form-control"
                                   name="nouveauMotDePasse"
                                   required minlength="6">
                            <div class="form-text">Minimum 6 caractères.</div>
                        </div>
                        <div class="mb-4">
                            <label class="form-label fw-bold">
                                Confirmer le nouveau mot de passe *
                            </label>
                            <input type="password" class="form-control"
                                   name="confirmationMotDePasse" required>
                        </div>

                        <button type="submit" class="btn btn-warning">
                            <i class="bi bi-shield-check"></i>
                            Changer le mot de passe
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Sidebar : infos compte -->
        <div class="col-lg-4">
            <div class="card border-0 shadow-sm mb-3">
                <div class="card-body text-center p-4">
                    <c:choose>
                        <c:when test="${not empty profil.avatarUrl}">
                            <img src="${pageContext.request.contextPath}/${profil.avatarUrl}"
                                 alt="Avatar"
                                 class="rounded-circle mb-3"
                                 style="width: 100px; height: 100px; object-fit: cover;">
                        </c:when>
                        <c:otherwise>
                            <i class="bi bi-person-circle display-3 text-primary mb-3"></i>
                        </c:otherwise>
                    </c:choose>

                    <h5>${profil.prenom} ${profil.nom}</h5>
                    <p class="text-muted small">${profil.email}</p>

                    <span class="badge
                        ${profil.role == 'ADMIN' ? 'bg-danger' :
                          profil.role == 'INSTRUCTEUR' ? 'bg-primary' : 'bg-success'}
                        fs-6 mb-3">
                        ${profil.role}
                    </span>

                    <c:if test="${not empty profil.biographie}">
                        <hr>
                        <p class="text-muted small text-start">
                            ${profil.biographie}
                        </p>
                    </c:if>

                    <hr>
                    <div class="text-start small text-muted">
                        <p class="mb-1">
                            <i class="bi bi-calendar text-primary me-2"></i>
                            Membre depuis :
                            ${profil.dateCreation}
                        </p>
                    </div>
                </div>
            </div>

            <%-- AJOUTÉ : bloc candidature instructeur dans la sidebar, visible uniquement pour les APPRENANTS --%>
            <c:if test="${profil.role == 'APPRENANT'}">
                <div class="card border-0 shadow-sm">
                    <div class="card-body p-4">
                        <h6 class="fw-bold mb-3">
                            <i class="bi bi-person-video3"></i> Devenir instructeur
                        </h6>

                        <c:choose>
                            <c:when test="${not empty derniereCandidature}">
                                <c:choose>
                                    <c:when test="${derniereCandidature.statut == 'EN_ATTENTE'}">
                                        <span class="badge bg-warning text-dark mb-2">
                                            <i class="bi bi-clock"></i> Candidature en attente
                                        </span>
                                        <p class="text-muted small mb-0">
                                            Votre candidature est en cours d'examen.
                                        </p>
                                    </c:when>
                                    <c:when test="${derniereCandidature.statut == 'REJETEE'}">
                                        <span class="badge bg-danger mb-2">
                                            <i class="bi bi-x-circle"></i> Candidature refusée
                                        </span>
                                        <p class="text-muted small mb-2">
                                            Vous pouvez soumettre une nouvelle candidature.
                                        </p>
                                        <a href="${pageContext.request.contextPath}/become-instructor"
                                           class="btn btn-outline-primary btn-sm w-100">
                                            <i class="bi bi-arrow-repeat"></i> Réessayer
                                        </a>
                                    </c:when>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted small mb-3">
                                    Partagez vos connaissances et créez vos propres cours.
                                </p>
                                <a href="${pageContext.request.contextPath}/become-instructor"
                                   class="btn btn-primary btn-sm w-100">
                                    <i class="bi bi-send"></i> Postuler maintenant
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>

        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>
