<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Dashboard Apprenant"/>
</jsp:include>

<div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>
            <i class="bi bi-house-door"></i>
            Bienvenue, ${sessionScope.utilisateur.prenom} !
        </h2>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger">
            <i class="bi bi-box-arrow-right"></i> Déconnexion
        </a>
    </div>

    <div class="alert alert-info">
        <i class="bi bi-info-circle"></i>
        <strong>Phase 3 validée !</strong>
        Tu es connecté en tant que <strong>${sessionScope.utilisateur.role}</strong>
        avec l'email <strong>${sessionScope.utilisateur.email}</strong>.
    </div>

    <div class="card">
        <div class="card-body">
            <h5 class="card-title">Informations de session</h5>
            <p>Nom complet : ${sessionScope.utilisateur.nomComplet}</p>
            <p>Rôle : ${sessionScope.utilisateur.role}</p>
            <p>Email : ${sessionScope.utilisateur.email}</p>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>