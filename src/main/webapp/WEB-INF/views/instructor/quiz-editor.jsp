<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp">
    <jsp:param name="titre" value="Éditeur de quiz"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layouts/navbar.jsp"/>

<div class="container my-4">

    <!-- En-tête -->
    <div class="d-flex align-items-center mb-4">
        <a href="${pageContext.request.contextPath}/instructor/course/curriculum?coursId=${coursId}"
           class="btn btn-outline-secondary me-3">
            <i class="bi bi-arrow-left"></i> Retour au curriculum
        </a>
        <div>
            <h2 class="mb-0">
                <i class="bi bi-question-circle"></i> Éditeur de quiz
            </h2>
            <small class="text-muted">
                Leçon : <strong>${lecon.titre}</strong>
            </small>
        </div>
    </div>

    <!-- Alertes -->
    <c:if test="${not empty param.succes}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="bi bi-check-circle"></i> ${param.succes}
            <button type="button" class="btn-close"
                    data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty param.erreur}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="bi bi-exclamation-circle"></i> ${param.erreur}
            <button type="button" class="btn-close"
                    data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="row g-4">

        <!-- Colonne principale : questions -->
        <div class="col-lg-8">

            <!-- Paramètres du quiz -->
            <div class="card border-0 shadow-sm mb-4">
                <div class="card-body">
                    <h5 class="mb-3">
                        <i class="bi bi-gear"></i> Paramètres du quiz
                    </h5>
                    <form method="post"
                          action="${pageContext.request.contextPath}/instructor/quiz/edit">
                        <input type="hidden" name="action" value="mettreAJourQuiz">
                        <input type="hidden" name="quizId" value="${quiz.id}">
                        <input type="hidden" name="leconId" value="${lecon.id}">
                        <input type="hidden" name="coursId" value="${coursId}">

                        <div class="row g-3">
                            <div class="col-md-8">
                                <label class="form-label fw-bold">Titre du quiz</label>
                                <input type="text" class="form-control"
                                       name="titre" value="${quiz.titre}" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-bold">
                                    Score minimum (%)
                                </label>
                                <input type="number" class="form-control"
                                       name="scoreMinimum"
                                       value="${quiz.scoreMinimum}"
                                       min="0" max="100" required>
                                <div class="form-text">
                                    Score requis pour valider la leçon
                                </div>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary mt-3">
                            <i class="bi bi-save"></i> Sauvegarder les paramètres
                        </button>
                    </form>
                </div>
            </div>

            <!-- Liste des questions -->
            <h5 class="mb-3">
                <i class="bi bi-list-ol"></i>
                Questions
                <span class="badge bg-secondary ms-1">
                    ${questions.size()}
                </span>
            </h5>

            <c:if test="${empty questions}">
                <div class="alert alert-info mb-4">
                    <i class="bi bi-info-circle"></i>
                    Aucune question pour l'instant.
                    Utilisez le formulaire à droite pour en ajouter !
                </div>
            </c:if>

            <c:forEach var="question" items="${questions}" varStatus="qIdx">
                <div class="card border-0 shadow-sm mb-3">
                    <div class="card-body">

                        <!-- En-tête question -->
                        <div class="d-flex justify-content-between
                                    align-items-start mb-3">
                            <div class="flex-grow-1">
                                <div class="d-flex align-items-center gap-2 mb-1">
                                    <span class="badge bg-primary">
                                        Q${qIdx.index + 1}
                                    </span>
                                    <span class="badge bg-secondary">
                                        ${question.typeQuestion}
                                    </span>
                                    <span class="badge bg-info">
                                        ${question.points} pt(s)
                                    </span>
                                </div>
                                <p class="fw-bold mb-0">
                                    <c:out value="${question.enonce}"/>
                                </p>
                            </div>
                            <form method="post"
                                  action="${pageContext.request.contextPath}/instructor/quiz/edit"
                                  class="ms-2"
                                  onsubmit="return confirm('Supprimer cette question et ses réponses ?')">
                                <input type="hidden" name="action"
                                       value="supprimerQuestion">
                                <input type="hidden" name="questionId"
                                       value="${question.id}">
                                <input type="hidden" name="quizId"
                                       value="${quiz.id}">
                                <input type="hidden" name="leconId"
                                       value="${lecon.id}">
                                <input type="hidden" name="coursId"
                                       value="${coursId}">
                                <button type="submit"
                                        class="btn btn-sm btn-outline-danger">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </form>
                        </div>

                        <!-- Réponses existantes -->
                        <div class="ms-2 mb-3">
                            <c:if test="${empty reponsesParQuestion[question.id]}">
                                <small class="text-muted fst-italic">
                                    Aucune réponse — ajoutez-en ci-dessous
                                </small>
                            </c:if>
                            <c:forEach var="reponse"
                                       items="${reponsesParQuestion[question.id]}">
                                <div class="d-flex align-items-center
                                            gap-2 mb-1 p-2 rounded
                                            ${reponse.estCorrecte ? 'bg-success bg-opacity-10' : ''}">
                                    <c:choose>
                                        <c:when test="${reponse.estCorrecte}">
                                            <i class="bi bi-check-circle-fill
                                                      text-success"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="bi bi-circle text-muted"></i>
                                        </c:otherwise>
                                    </c:choose>
                                    <span class="${reponse.estCorrecte
                                        ? 'text-success fw-bold' : ''}">
                                        <c:out value="${reponse.texte}"/>
                                    </span>
                                    <c:if test="${reponse.estCorrecte}">
                                        <span class="badge bg-success ms-1">
                                            Correcte
                                        </span>
                                    </c:if>
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/instructor/quiz/edit"
                                          class="ms-auto d-inline">
                                        <input type="hidden" name="action"
                                               value="supprimerReponse">
                                        <input type="hidden" name="reponseId"
                                               value="${reponse.id}">
                                        <input type="hidden" name="quizId"
                                               value="${quiz.id}">
                                        <input type="hidden" name="leconId"
                                               value="${lecon.id}">
                                        <input type="hidden" name="coursId"
                                               value="${coursId}">
                                        <button type="submit"
                                                class="btn btn-sm btn-link
                                                       text-danger p-0">
                                            <i class="bi bi-x-circle"></i>
                                        </button>
                                    </form>
                                </div>
                            </c:forEach>
                        </div>

                        <!-- Formulaire ajout réponse -->
                        <div class="border-top pt-3">
                            <p class="small text-muted fw-bold mb-2">
                                <i class="bi bi-plus-circle"></i>
                                Ajouter une réponse
                            </p>
                            <form method="post"
                                  action="${pageContext.request.contextPath}/instructor/quiz/edit">
                                <input type="hidden" name="action"
                                       value="ajouterReponse">
                                <input type="hidden" name="questionId"
                                       value="${question.id}">
                                <input type="hidden" name="quizId"
                                       value="${quiz.id}">
                                <input type="hidden" name="leconId"
                                       value="${lecon.id}">
                                <input type="hidden" name="coursId"
                                       value="${coursId}">
                                <div class="input-group input-group-sm">
                                    <input type="text" class="form-control"
                                           name="texte"
                                           placeholder="Texte de la réponse"
                                           required>
                                    <div class="input-group-text">
                                        <input type="checkbox"
                                               name="estCorrecte"
                                               class="form-check-input me-1"
                                               id="correct_${question.id}">
                                        <label for="correct_${question.id}"
                                               class="form-check-label small">
                                            Correcte
                                        </label>
                                    </div>
                                    <button type="submit"
                                            class="btn btn-success btn-sm">
                                        <i class="bi bi-plus"></i> Ajouter
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </c:forEach>

        </div>

        <!-- Sidebar : ajouter question -->
        <div class="col-lg-4">
            <div class="card border-0 shadow-sm sticky-top" style="top: 80px;">
                <div class="card-body">
                    <h5 class="mb-3">
                        <i class="bi bi-plus-circle"></i> Nouvelle question
                    </h5>
                    <form method="post"
                          action="${pageContext.request.contextPath}/instructor/quiz/edit">
                        <input type="hidden" name="action"
                               value="ajouterQuestion">
                        <input type="hidden" name="quizId" value="${quiz.id}">
                        <input type="hidden" name="leconId" value="${lecon.id}">
                        <input type="hidden" name="coursId" value="${coursId}">

                        <div class="mb-3">
                            <label class="form-label fw-bold">Énoncé *</label>
                            <textarea class="form-control" name="enonce"
                                      rows="3" required
                                      placeholder="Quel est...?
Quelle est la différence entre...?
Vrai ou Faux :..."></textarea>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Type de question</label>
                            <select class="form-select" name="typeQuestion">
                                <option value="QCM_UNIQUE">
                                    QCM — 1 bonne réponse
                                </option>
                                <option value="QCM_MULTIPLE">
                                    QCM — Plusieurs bonnes réponses
                                </option>
                                <option value="VRAI_FAUX">
                                    Vrai / Faux
                                </option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Points</label>
                            <input type="number" class="form-control"
                                   name="points" value="1" min="1" max="10">
                            <div class="form-text">
                                Valeur de cette question dans le score final
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100">
                            <i class="bi bi-plus-circle"></i>
                            Ajouter la question
                        </button>
                    </form>

                    <hr>

                    <!-- Aide -->
                    <div class="alert alert-info small mb-0">
                        <i class="bi bi-lightbulb"></i>
                        <strong>Comment créer un quiz :</strong>
                        <ol class="mt-2 mb-0 ps-3">
                            <li>Ajoutez vos questions</li>
                            <li>Pour chaque question, ajoutez les réponses possibles</li>
                            <li>Cochez "Correcte" pour la/les bonne(s) réponse(s)</li>
                            <li>Définissez le score minimum pour valider</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>