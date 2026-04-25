<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Connexion"/>
</jsp:include>

<div class="auth-container">
    <div class="text-center mb-4">
        <h1 class="brand-title"><i class="bi bi-mortarboard-fill"></i> LMS Platform</h1>
        <p class="text-muted">Connectez-vous à votre espace</p>
    </div>

    <div class="auth-card">
        <!-- Message de succès (venant de l'inscription) -->
        <c:if test="${not empty succes}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle"></i> ${succes}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Message d'erreur -->
        <c:if test="${not empty erreur}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-circle"></i> ${erreur}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="mb-3">
                <label for="email" class="form-label">Adresse email</label>
                <input type="email" class="form-control" id="email" name="email"
                       value="${email}" required placeholder="jean@exemple.com">
            </div>

            <div class="mb-4">
                <label for="motDePasse" class="form-label">Mot de passe</label>
                <input type="password" class="form-control" id="motDePasse" name="motDePasse"
                       required placeholder="Votre mot de passe">
            </div>

            <button type="submit" class="btn btn-primary w-100 py-2">
                <i class="bi bi-box-arrow-in-right"></i> Se connecter
            </button>
        </form>

        <hr class="my-4">

        <p class="text-center mb-0">
            Pas encore de compte ?
            <a href="${pageContext.request.contextPath}/register">S'inscrire gratuitement</a>
        </p>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>