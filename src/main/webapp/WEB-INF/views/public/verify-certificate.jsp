<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Vérification de certificat"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-5" style="max-width: 700px;">
    <h2 class="mb-4 text-center">
        <i class="bi bi-patch-check"></i> Vérification de certificat
    </h2>

    <%-- Formulaire de recherche par code --%>
    <div class="card border-0 shadow-sm mb-4">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/verify">
                <div class="input-group">
                    <input type="text" class="form-control" name="code"
                           value="${code}"
                           placeholder="Entrez le code de vérification..."
                           required>
                    <button class="btn btn-primary" type="submit">
                        <i class="bi bi-search"></i> Vérifier
                    </button>
                </div>
            </form>
        </div>
    </div>

    <%-- Résultat --%>
    <c:if test="${not empty code}">
        <c:choose>
            <c:when test="${certValide}">
                <div class="card border-0 shadow-sm border-start border-success border-4">
                    <div class="card-body text-center py-4">
                        <i class="bi bi-patch-check-fill text-success display-3"></i>
                        <h3 class="text-success mt-3">Certificat authentique ✅</h3>
                        <hr>
                        <div class="text-start mt-3">
                            <p>
                                <i class="bi bi-person-fill text-primary me-2"></i>
                                <strong>Apprenant :</strong>
                                ${apprenant.prenom} ${apprenant.nom}
                            </p>
                            <p>
                                <i class="bi bi-book-fill text-primary me-2"></i>
                                <strong>Cours :</strong> ${cours.titre}
                            </p>
                            <p>
                                <i class="bi bi-calendar-fill text-primary me-2"></i>
                                <strong>Date d'émission :</strong>
                                ${certificat.dateEmission}
                            </p>
                            <p>
                                <i class="bi bi-key-fill text-primary me-2"></i>
                                <strong>Code :</strong>
                                <code>${certificat.codeVerification}</code>
                            </p>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card border-0 shadow-sm border-start border-danger border-4">
                    <div class="card-body text-center py-4">
                        <i class="bi bi-x-circle-fill text-danger display-3"></i>
                        <h3 class="text-danger mt-3">Certificat invalide ❌</h3>
                        <p class="text-muted mt-2">
                            Ce code de vérification ne correspond à aucun certificat
                            dans notre système.
                        </p>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </c:if>
</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>