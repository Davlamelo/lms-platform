<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Résultat du quiz - LMS Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .score-cercle {
            width: 160px; height: 160px;
            border-radius: 50%;
            display: flex; flex-direction: column;
            align-items: center; justify-content: center;
            margin: 0 auto;
            font-size: 2.5rem; font-weight: 800;
        }
        .score-reussi { background: linear-gradient(135deg, #22c55e, #16a34a); color: white; }
        .score-echoue { background: linear-gradient(135deg, #ef4444, #dc2626); color: white; }
        .bonne-reponse { background-color: rgba(34, 197, 94, 0.1); }
        .mauvaise-reponse { background-color: rgba(239, 68, 68, 0.1); }
        .carte-correcte { border-left: 4px solid #22c55e !important; }
        .carte-incorrecte { border-left: 4px solid #ef4444 !important; }
    </style>
</head>
<body>

<div class="container py-5" style="max-width: 800px;">

    <!-- Score principal -->
    <div class="card border-0 shadow-sm mb-4">
        <div class="card-body text-center py-5">
            <h2 class="mb-4">${quiz.titre}</h2>

            <div class="score-cercle ${tentative.estReussi ? 'score-reussi' : 'score-echoue'} mb-4">
                <fmt:formatNumber value="${tentative.score}" maxFractionDigits="0"/>%
            </div>

            <c:choose>
                <c:when test="${tentative.estReussi}">
                    <h3 class="text-success mt-3">
                        <i class="bi bi-check-circle-fill"></i> Quiz réussi !
                    </h3>
                    <p class="text-muted">
                        Félicitations ! Vous avez obtenu
                        <strong>${tentative.pointsObtenus}/${tentative.pointsTotal}</strong>
                        points. La leçon est marquée comme complétée. ✅
                    </p>
                </c:when>
                <c:otherwise>
                    <h3 class="text-danger mt-3">
                        <i class="bi bi-x-circle-fill"></i> Quiz non réussi
                    </h3>
                    <p class="text-muted">
                        Vous avez obtenu
                        <strong>${tentative.pointsObtenus}/${tentative.pointsTotal}</strong>
                        points. Score minimum requis :
                        <strong>${quiz.scoreMinimum}%</strong>. Vous pouvez réessayer !
                    </p>
                </c:otherwise>
            </c:choose>

            <div class="d-flex gap-3 justify-content-center mt-4">
                <a href="${pageContext.request.contextPath}/student/learn?coursId=${coursId}&leconId=${leconId}"
                   class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left"></i> Retour au cours
                </a>
                <c:if test="${tentative.estReussi}">
                    <a href="${pageContext.request.contextPath}/student/dashboard"
                       class="btn btn-success">
                        <i class="bi bi-speedometer2"></i> Mon dashboard
                    </a>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Correction détaillée -->
    <h4 class="mb-3"><i class="bi bi-list-check"></i> Correction détaillée</h4>

    <c:forEach var="question" items="${questions}" varStatus="qIdx">
        <%-- Récupérer le résultat de cette question via la clé String --%>
        <c:set var="questionIdStr" value="${question.id}"/>
        <c:set var="estCorrect" value="${resultatParQuestion[questionIdStr]}"/>
        <c:set var="reponseChoisieId" value="${reponseChoisie[questionIdStr]}"/>

        <div class="card border-0 shadow-sm mb-3 ${estCorrect ? 'carte-correcte' : 'carte-incorrecte'}">
            <div class="card-body">
                <div class="d-flex align-items-start mb-3">
                    <c:choose>
                        <c:when test="${estCorrect}">
                            <i class="bi bi-check-circle-fill text-success fs-5 me-2 mt-1"></i>
                        </c:when>
                        <c:otherwise>
                            <i class="bi bi-x-circle-fill text-danger fs-5 me-2 mt-1"></i>
                        </c:otherwise>
                    </c:choose>
                    <div>
                        <p class="fw-bold mb-1">
                            Q${qIdx.index + 1}. <c:out value="${question.enonce}"/>
                        </p>
                        <small class="text-muted">${question.points} pt(s)</small>
                    </div>
                </div>

                <c:forEach var="reponse" items="${reponsesParQuestion[question.id]}">
                    <c:set var="estChoisie" value="${reponse.id == reponseChoisieId}"/>
                    <div class="p-2 rounded mb-1
                         ${reponse.estCorrecte ? 'bonne-reponse' : ''}
                         ${estChoisie && !reponse.estCorrecte ? 'mauvaise-reponse' : ''}">
                        <div class="d-flex align-items-center">
                            <c:choose>
                                <c:when test="${reponse.estCorrecte}">
                                    <i class="bi bi-check text-success me-2"></i>
                                </c:when>
                                <c:when test="${estChoisie && !reponse.estCorrecte}">
                                    <i class="bi bi-x text-danger me-2"></i>
                                </c:when>
                                <c:otherwise>
                                    <i class="bi bi-circle text-muted me-2"></i>
                                </c:otherwise>
                            </c:choose>
                            <span class="${reponse.estCorrecte ? 'text-success fw-semibold' : ''}
                                        ${estChoisie && !reponse.estCorrecte ? 'text-danger' : ''}">
                                <c:out value="${reponse.texte}"/>
                                <c:if test="${estChoisie}">
                                    <span class="badge bg-secondary ms-2">Votre réponse</span>
                                </c:if>
                                <c:if test="${reponse.estCorrecte}">
                                    <span class="badge bg-success ms-2">Bonne réponse</span>
                                </c:if>
                            </span>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:forEach>

    <div class="text-center mt-4">
        <a href="${pageContext.request.contextPath}/student/learn?coursId=${coursId}"
           class="btn btn-primary">
            <i class="bi bi-arrow-left"></i> Continuer le cours
        </a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>