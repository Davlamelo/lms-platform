<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Inscription"/>
</jsp:include>

<div class="auth-container">
    <div class="text-center mb-4">
        <h1 class="brand-title"><i class="bi bi-mortarboard-fill"></i> LMS Platform</h1>
        <p class="text-muted">Créez votre compte gratuitement</p>
    </div>

    <div class="auth-card">
        <!-- Message d'erreur -->
        <c:if test="${not empty erreur}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-circle"></i> ${erreur}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/register">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="prenom" class="form-label">Prénom</label>
                    <input type="text" class="form-control" id="prenom" name="prenom"
                           value="${prenom}" required placeholder="Jean">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="nom" class="form-label">Nom</label>
                    <input type="text" class="form-control" id="nom" name="nom"
                           value="${nom}" required placeholder="Dupont">
                </div>
            </div>

            <div class="mb-3">
                <label for="email" class="form-label">Adresse email</label>
                <input type="email" class="form-control" id="email" name="email"
                       value="${email}" required placeholder="jean@exemple.com">
            </div>

            <div class="mb-3">
                <label for="motDePasse" class="form-label">Mot de passe</label>
                <input type="password" class="form-control" id="motDePasse" name="motDePasse"
                       required minlength="6" placeholder="6 caractères minimum">
            </div>

            <div class="mb-4">
                <label for="confirmationMdp" class="form-label">Confirmer le mot de passe</label>
                <input type="password" class="form-control" id="confirmationMdp" name="confirmationMdp"
                       required placeholder="Retapez le mot de passe">
            </div>

            <button type="submit" class="btn btn-primary w-100 py-2">
                <i class="bi bi-person-plus"></i> S'inscrire
            </button>
        </form>

        <hr class="my-4">

        <p class="text-center mb-0">
            Déjà un compte ?
            <a href="${pageContext.request.contextPath}/login">Se connecter</a>
        </p>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>