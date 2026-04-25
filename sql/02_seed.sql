-- ============================================================================
-- LMS Platform - Données de test (Seed)
-- Tous les mots de passe = "password123" (hashé BCrypt)
-- ============================================================================

USE db_lms;

-- ============================================================================
-- 1. UTILISATEURS (3 rôles)
-- Hash BCrypt de "password123" :
-- $2a$12$75kT7mSDXUp670fv3kK8gODHvVWsRYasJT/f3vKWUcLaM0i5QdYau
-- ============================================================================

INSERT INTO utilisateurs (email, mot_de_passe, prenom, nom, role, actif, biographie) VALUES
-- ADMIN
('admin@lms-platform.com',
 '$2a$12$75kT7mSDXUp670fv3kK8gODHvVWsRYasJT/f3vKWUcLaM0i5QdYau',
 'Admin', 'LMS', 'ADMIN', TRUE,
 'Administrateur de la plateforme LMS.'),

-- INSTRUCTEURS
('marie.dupont@email.com',
 '$2a$12$75kT7mSDXUp670fv3kK8gODHvVWsRYasJT/f3vKWUcLaM0i5QdYau',
 'Marie', 'Dupont', 'INSTRUCTEUR', TRUE,
 'Experte en Data Science et Machine Learning avec 10 ans d''expérience.'),

('ahmed.benali@email.com',
 '$2a$12$75kT7mSDXUp670fv3kK8gODHvVWsRYasJT/f3vKWUcLaM0i5QdYau',
 'Ahmed', 'Benali', 'INSTRUCTEUR', TRUE,
 'Développeur Full Stack spécialisé en Java et Spring Boot.'),

('sophie.martin@email.com',
 '$2a$12$75kT7mSDXUp670fv3kK8gODHvVWsRYasJT/f3vKWUcLaM0i5QdYau',
 'Sophie', 'Martin', 'INSTRUCTEUR', TRUE,
 'Designer UX/UI et formatrice en développement web front-end.'),

-- APPRENANTS
('david.test@email.com',
 '$2a$12$75kT7mSDXUp670fv3kK8gODHvVWsRYasJT/f3vKWUcLaM0i5QdYau',
 'David', 'Tassembedo', 'APPRENANT', TRUE,
 'Étudiant en Data Science et IA à l''ISMAGI.'),

('fatima.zahra@email.com',
 '$2a$12$75kT7mSDXUp670fv3kK8gODHvVWsRYasJT/f3vKWUcLaM0i5QdYau',
 'Fatima', 'Zahra', 'APPRENANT', TRUE, NULL),

('omar.fall@email.com',
 '$2a$12$75kT7mSDXUp670fv3kK8gODHvVWsRYasJT/f3vKWUcLaM0i5QdYau',
 'Omar', 'Fall', 'APPRENANT', TRUE, NULL),

('lina.idrissi@email.com',
 '$2a$12$75kT7mSDXUp670fv3kK8gODHvVWsRYasJT/f3vKWUcLaM0i5QdYau',
 'Lina', 'Idrissi', 'APPRENANT', TRUE, NULL),

('paul.kouame@email.com',
 '$2a$12$75kT7mSDXUp670fv3kK8gODHvVWsRYasJT/f3vKWUcLaM0i5QdYau',
 'Paul', 'Kouamé', 'APPRENANT', TRUE, NULL),

('amina.diallo@email.com',
 '$2a$12$75kT7mSDXUp670fv3kK8gODHvVWsRYasJT/f3vKWUcLaM0i5QdYau',
 'Amina', 'Diallo', 'APPRENANT', TRUE, NULL);

-- ============================================================================
-- 2. CATEGORIES
-- ============================================================================

INSERT INTO categories (nom, slug, description, icone) VALUES
('Data Science', 'data-science',
 'Analyse de données, statistiques, machine learning et intelligence artificielle.',
 'bi-graph-up'),
('Développement Web', 'developpement-web',
 'HTML, CSS, JavaScript, frameworks front-end et back-end.',
 'bi-code-slash'),
('Développement Mobile', 'developpement-mobile',
 'Applications iOS, Android, Flutter et React Native.',
 'bi-phone'),
('Bases de Données', 'bases-de-donnees',
 'SQL, NoSQL, modélisation, optimisation et administration.',
 'bi-database'),
('DevOps & Cloud', 'devops-cloud',
 'Docker, Kubernetes, CI/CD, AWS, Azure et Google Cloud.',
 'bi-cloud'),
('Design & UX', 'design-ux',
 'UI/UX Design, Figma, prototypage et expérience utilisateur.',
 'bi-palette'),
('Cybersécurité', 'cybersecurite',
 'Sécurité réseau, ethical hacking, cryptographie.',
 'bi-shield-lock'),
('Intelligence Artificielle', 'intelligence-artificielle',
 'Deep learning, NLP, vision par ordinateur, LLMs.',
 'bi-robot');

-- ============================================================================
-- 3. COURS (8 cours variés)
-- ============================================================================

-- Cours 1 : Python Data Science (Marie Dupont, id=2)
INSERT INTO cours (titre, slug, description_courte, description_longue, niveau, langue,
                   duree_totale_min, statut, instructeur_id, categorie_id, date_publication) VALUES
('Python pour la Data Science',
 'python-data-science',
 'Maîtrisez Python, Pandas, NumPy et Matplotlib pour l''analyse de données.',
 'Ce cours complet vous emmène de zéro à héros en Data Science avec Python. Vous apprendrez à manipuler des datasets réels, créer des visualisations percutantes et construire vos premiers modèles de machine learning.\n\nAu programme :\n- Les bases de Python (variables, boucles, fonctions)\n- NumPy pour le calcul scientifique\n- Pandas pour la manipulation de données\n- Matplotlib et Seaborn pour la visualisation\n- Scikit-learn pour le machine learning\n- Projet final : analyse d''un dataset réel',
 'DEBUTANT', 'fr', 480, 'PUBLIE', 2, 1, NOW());

-- Cours 2 : Java Spring Boot (Ahmed Benali, id=3)
INSERT INTO cours (titre, slug, description_courte, description_longue, niveau, langue,
                   duree_totale_min, statut, instructeur_id, categorie_id, date_publication) VALUES
('Java Spring Boot : API REST complète',
 'java-spring-boot-api-rest',
 'Construisez une API REST professionnelle avec Spring Boot, JPA et MySQL.',
 'Apprenez à développer des API REST robustes et sécurisées avec le framework le plus demandé en entreprise.\n\nCe cours couvre :\n- Spring Boot fundamentals\n- Spring Data JPA et Hibernate\n- REST controllers et validation\n- Spring Security avec JWT\n- Tests unitaires et d''intégration\n- Déploiement Docker',
 'INTERMEDIAIRE', 'fr', 600, 'PUBLIE', 3, 2, NOW());

-- Cours 3 : HTML/CSS pour débutants (Sophie Martin, id=4)
INSERT INTO cours (titre, slug, description_courte, description_longue, niveau, langue,
                   duree_totale_min, statut, instructeur_id, categorie_id, date_publication) VALUES
('HTML & CSS : de zéro à autonome',
 'html-css-debutant',
 'Apprenez les fondations du web : HTML5, CSS3, Flexbox, Grid et responsive design.',
 'Le point de départ idéal pour toute personne voulant créer des sites web. Ce cours vous apprend les fondamentaux du HTML et CSS avec une approche pratique.\n\nVous créerez 5 projets concrets :\n- Page de profil personnelle\n- Landing page responsive\n- Portfolio avec galerie\n- Formulaire de contact\n- Clone de page d''accueil',
 'DEBUTANT', 'fr', 360, 'PUBLIE', 4, 2, NOW());

-- Cours 4 : Machine Learning (Marie Dupont, id=2)
INSERT INTO cours (titre, slug, description_courte, description_longue, niveau, langue,
                   duree_totale_min, statut, instructeur_id, categorie_id, date_publication) VALUES
('Machine Learning avec Scikit-Learn',
 'machine-learning-scikit-learn',
 'Maîtrisez les algorithmes de ML : régression, classification, clustering et évaluation.',
 'Plongez dans le machine learning et apprenez à construire des modèles prédictifs performants.\n\nAlgorithmes couverts :\n- Régression linéaire et logistique\n- Arbres de décision et Random Forest\n- SVM et KNN\n- K-Means et DBSCAN\n- Évaluation et validation croisée\n- Feature engineering',
 'INTERMEDIAIRE', 'fr', 540, 'PUBLIE', 2, 1, NOW());

-- Cours 5 : SQL Masterclass (Ahmed Benali, id=3)
INSERT INTO cours (titre, slug, description_courte, description_longue, niveau, langue,
                   duree_totale_min, statut, instructeur_id, categorie_id, date_publication) VALUES
('SQL : du débutant au niveau avancé',
 'sql-masterclass',
 'Maîtrisez SQL avec MySQL : requêtes, jointures, sous-requêtes, optimisation.',
 'SQL est LE langage incontournable pour tout développeur ou data analyst.\n\nCe cours couvre :\n- SELECT, WHERE, ORDER BY\n- JOINs (INNER, LEFT, RIGHT, FULL)\n- GROUP BY et fonctions d''agrégation\n- Sous-requêtes et CTE\n- Index et optimisation\n- Procédures stockées et triggers',
 'DEBUTANT', 'fr', 420, 'PUBLIE', 3, 4, NOW());

-- Cours 6 : UX Design (Sophie Martin, id=4)
INSERT INTO cours (titre, slug, description_courte, description_longue, niveau, langue,
                   duree_totale_min, statut, instructeur_id, categorie_id, date_publication) VALUES
('UX/UI Design avec Figma',
 'ux-ui-design-figma',
 'Concevez des interfaces utilisateur modernes et intuitives avec Figma.',
 'Apprenez le processus complet de design d''interface, de la recherche utilisateur au prototype interactif.\n\nAu programme :\n- Principes de l''UX Design\n- Wireframing et prototypage\n- Design system et composants\n- Figma : outils et plugins\n- Tests utilisateurs\n- Handoff développeur',
 'DEBUTANT', 'fr', 300, 'PUBLIE', 4, 6, NOW());

-- Cours 7 : Deep Learning (Marie Dupont, id=2) - EN BROUILLON
INSERT INTO cours (titre, slug, description_courte, description_longue, niveau, langue,
                   duree_totale_min, statut, instructeur_id, categorie_id) VALUES
('Deep Learning avec TensorFlow',
 'deep-learning-tensorflow',
 'Réseaux de neurones, CNN, RNN et transformers avec TensorFlow et Keras.',
 'Cours avancé sur le deep learning. En cours de création.',
 'AVANCE', 'fr', 0, 'BROUILLON', 2, 8);

-- Cours 8 : Docker & Kubernetes (Ahmed Benali, id=3) - EN ATTENTE
INSERT INTO cours (titre, slug, description_courte, description_longue, niveau, langue,
                   duree_totale_min, statut, instructeur_id, categorie_id) VALUES
('Docker & Kubernetes en pratique',
 'docker-kubernetes-pratique',
 'Containerisez et orchestrez vos applications comme un DevOps senior.',
 'Apprenez Docker et Kubernetes de A à Z avec des projets pratiques.',
 'INTERMEDIAIRE', 'fr', 0, 'EN_ATTENTE_VALIDATION', 3, 5);

-- ============================================================================
-- 4. SECTIONS ET LEÇONS (pour le cours 1 : Python Data Science)
-- ============================================================================

-- Section 1
INSERT INTO sections (cours_id, titre, description, ordre) VALUES
(1, 'Introduction à Python', 'Bases du langage Python', 1);

INSERT INTO lecons (section_id, titre, type_lecon, contenu_texte, duree_min, ordre, est_gratuite) VALUES
(1, 'Bienvenue dans le cours', 'TEXTE',
 '<h2>Bienvenue !</h2><p>Dans ce cours, vous allez apprendre Python pour la Data Science. Python est le langage numéro 1 en science des données grâce à ses librairies puissantes comme Pandas, NumPy et Scikit-Learn.</p><p>Prérequis : aucun ! Ce cours part de zéro.</p><h3>Ce que vous allez apprendre</h3><ul><li>Les bases de Python</li><li>La manipulation de données avec Pandas</li><li>La visualisation avec Matplotlib</li><li>Vos premiers modèles de ML</li></ul>',
 5, 1, TRUE),
(1, 'Installer Python et Jupyter', 'TEXTE',
 '<h2>Installation</h2><p>Nous allons installer Python via <strong>Anaconda</strong>, qui inclut Jupyter Notebook et toutes les librairies nécessaires.</p><h3>Étapes :</h3><ol><li>Téléchargez Anaconda sur <a href="https://www.anaconda.com">anaconda.com</a></li><li>Lancez l''installation</li><li>Ouvrez Anaconda Navigator</li><li>Lancez Jupyter Notebook</li></ol><p>Félicitations, votre environnement est prêt !</p>',
 10, 2, TRUE),
(1, 'Variables et types de données', 'TEXTE',
 '<h2>Variables en Python</h2><p>En Python, une variable est créée dès qu''on lui assigne une valeur :</p><pre><code>nom = "David"\nage = 22\ntaille = 1.75\nest_etudiant = True</code></pre><h3>Types principaux :</h3><ul><li><code>str</code> : chaîne de caractères</li><li><code>int</code> : entier</li><li><code>float</code> : décimal</li><li><code>bool</code> : booléen</li></ul>',
 15, 3, FALSE),
(1, 'Quiz : Bases de Python', 'QUIZ', NULL, 10, 4, FALSE);

-- Section 2
INSERT INTO sections (cours_id, titre, description, ordre) VALUES
(1, 'NumPy : calcul scientifique', 'Tableaux et opérations matricielles avec NumPy', 2);

INSERT INTO lecons (section_id, titre, type_lecon, contenu_texte, duree_min, ordre) VALUES
(2, 'Introduction à NumPy', 'TEXTE',
 '<h2>NumPy</h2><p>NumPy est la librairie fondamentale pour le calcul scientifique en Python. Elle fournit des tableaux multidimensionnels performants.</p><pre><code>import numpy as np\n\ntableau = np.array([1, 2, 3, 4, 5])\nprint(tableau.mean())  # 3.0\nprint(tableau.std())   # 1.41</code></pre>',
 20, 1),
(2, 'Opérations sur les tableaux', 'TEXTE',
 '<h2>Opérations vectorisées</h2><p>NumPy permet de faire des opérations sur des tableaux entiers sans boucles :</p><pre><code>a = np.array([1, 2, 3])\nb = np.array([4, 5, 6])\n\nprint(a + b)   # [5, 7, 9]\nprint(a * b)   # [4, 10, 18]\nprint(a ** 2)  # [1, 4, 9]</code></pre>',
 20, 2),
(2, 'Quiz : NumPy', 'QUIZ', NULL, 10, 3);

-- Section 3
INSERT INTO sections (cours_id, titre, description, ordre) VALUES
(1, 'Pandas : manipulation de données', 'DataFrames, filtrage, groupby', 3);

INSERT INTO lecons (section_id, titre, type_lecon, contenu_texte, duree_min, ordre) VALUES
(3, 'Les DataFrames Pandas', 'TEXTE',
 '<h2>Pandas DataFrame</h2><p>Le DataFrame est la structure centrale de Pandas. C''est un tableau 2D avec des étiquettes pour les lignes et les colonnes.</p><pre><code>import pandas as pd\n\ndf = pd.DataFrame({\n    "nom": ["Alice", "Bob", "Charlie"],\n    "age": [25, 30, 35],\n    "ville": ["Paris", "Lyon", "Marseille"]\n})\n\nprint(df.head())</code></pre>',
 25, 1),
(3, 'Filtrage et sélection', 'TEXTE',
 '<h2>Filtrer les données</h2><p>Pandas permet de filtrer facilement :</p><pre><code># Sélection par colonne\ndf["nom"]\n\n# Filtrage conditionnel\ndf[df["age"] > 28]\n\n# Plusieurs conditions\ndf[(df["age"] > 25) & (df["ville"] == "Lyon")]</code></pre>',
 20, 2),
(3, 'GroupBy et agrégation', 'TEXTE',
 '<h2>GroupBy</h2><p>GroupBy permet de regrouper les données et appliquer des fonctions d''agrégation :</p><pre><code>ventes = pd.DataFrame({\n    "region": ["Nord", "Sud", "Nord", "Sud"],\n    "montant": [100, 200, 150, 300]\n})\n\nventes.groupby("region")["montant"].sum()\n# Nord: 250, Sud: 500</code></pre>',
 20, 3);

-- ============================================================================
-- 5. SECTIONS ET LEÇONS (pour le cours 3 : HTML/CSS)
-- ============================================================================

INSERT INTO sections (cours_id, titre, description, ordre) VALUES
(3, 'Les bases du HTML', 'Structure d''une page web, balises principales', 1);

INSERT INTO lecons (section_id, titre, type_lecon, contenu_texte, duree_min, ordre, est_gratuite) VALUES
(4, 'Votre première page HTML', 'TEXTE',
 '<h2>Structure de base</h2><p>Toute page HTML commence par cette structure :</p><pre><code>&lt;!DOCTYPE html&gt;\n&lt;html lang="fr"&gt;\n&lt;head&gt;\n    &lt;meta charset="UTF-8"&gt;\n    &lt;title&gt;Ma page&lt;/title&gt;\n&lt;/head&gt;\n&lt;body&gt;\n    &lt;h1&gt;Bonjour le monde !&lt;/h1&gt;\n&lt;/body&gt;\n&lt;/html&gt;</code></pre>',
 15, 1, TRUE),
(4, 'Les balises essentielles', 'TEXTE',
 '<h2>Balises de texte</h2><ul><li><code>&lt;h1&gt;</code> à <code>&lt;h6&gt;</code> : titres</li><li><code>&lt;p&gt;</code> : paragraphe</li><li><code>&lt;a&gt;</code> : lien</li><li><code>&lt;img&gt;</code> : image</li><li><code>&lt;ul&gt;</code>, <code>&lt;ol&gt;</code> : listes</li></ul>',
 20, 2, FALSE);

INSERT INTO sections (cours_id, titre, description, ordre) VALUES
(3, 'CSS : styliser vos pages', 'Sélecteurs, propriétés, Flexbox', 2);

INSERT INTO lecons (section_id, titre, type_lecon, contenu_texte, duree_min, ordre) VALUES
(5, 'Introduction au CSS', 'TEXTE',
 '<h2>CSS en 3 méthodes</h2><ol><li><strong>Inline</strong> : <code>style="color: red;"</code></li><li><strong>Interne</strong> : balise <code>&lt;style&gt;</code> dans le head</li><li><strong>Externe</strong> : fichier .css séparé (recommandé)</li></ol>',
 15, 1),
(5, 'Flexbox en 10 minutes', 'TEXTE',
 '<h2>Flexbox</h2><p>Flexbox est le système de mise en page moderne le plus utilisé :</p><pre><code>.container {\n    display: flex;\n    justify-content: center;\n    align-items: center;\n    gap: 1rem;\n}</code></pre><p>Propriétés clés : <code>flex-direction</code>, <code>justify-content</code>, <code>align-items</code>, <code>flex-wrap</code>.</p>',
 20, 2);

-- ============================================================================
-- 6. QUIZ (pour les leçons de type QUIZ)
-- ============================================================================

-- Quiz 1 : Bases de Python (lecon_id = 4)
INSERT INTO quiz (lecon_id, titre, description, score_minimum) VALUES
(4, 'Quiz : Bases de Python', 'Testez vos connaissances sur les fondamentaux de Python.', 70);

-- Questions du Quiz 1
INSERT INTO questions (quiz_id, enonce, type_question, points, ordre) VALUES
(1, 'Quel est le type de la variable x = 3.14 ?', 'QCM_UNIQUE', 1, 1),
(1, 'Quelle fonction affiche du texte dans la console ?', 'QCM_UNIQUE', 1, 2),
(1, 'Python est un langage interprété.', 'VRAI_FAUX', 1, 3),
(1, 'Quel mot-clé définit une fonction en Python ?', 'QCM_UNIQUE', 1, 4);

-- Réponses Q1
INSERT INTO reponses (question_id, texte, est_correcte, ordre) VALUES
(1, 'int', FALSE, 1),
(1, 'float', TRUE, 2),
(1, 'str', FALSE, 3),
(1, 'double', FALSE, 4);

-- Réponses Q2
INSERT INTO reponses (question_id, texte, est_correcte, ordre) VALUES
(2, 'echo()', FALSE, 1),
(2, 'print()', TRUE, 2),
(2, 'console.log()', FALSE, 3),
(2, 'System.out.println()', FALSE, 4);

-- Réponses Q3
INSERT INTO reponses (question_id, texte, est_correcte, ordre) VALUES
(3, 'Vrai', TRUE, 1),
(3, 'Faux', FALSE, 2);

-- Réponses Q4
INSERT INTO reponses (question_id, texte, est_correcte, ordre) VALUES
(4, 'function', FALSE, 1),
(4, 'def', TRUE, 2),
(4, 'func', FALSE, 3),
(4, 'void', FALSE, 4);

-- Quiz 2 : NumPy (lecon_id = 7)
INSERT INTO quiz (lecon_id, titre, description, score_minimum) VALUES
(7, 'Quiz : NumPy', 'Testez vos connaissances sur NumPy.', 70);

INSERT INTO questions (quiz_id, enonce, type_question, points, ordre) VALUES
(2, 'Quelle fonction crée un tableau NumPy ?', 'QCM_UNIQUE', 1, 1),
(2, 'NumPy est plus rapide que les listes Python pour le calcul numérique.', 'VRAI_FAUX', 1, 2);

INSERT INTO reponses (question_id, texte, est_correcte, ordre) VALUES
(5, 'np.list()', FALSE, 1),
(5, 'np.array()', TRUE, 2),
(5, 'np.create()', FALSE, 3),
(5, 'np.new()', FALSE, 4);

INSERT INTO reponses (question_id, texte, est_correcte, ordre) VALUES
(6, 'Vrai', TRUE, 1),
(6, 'Faux', FALSE, 2);

-- ============================================================================
-- 7. INSCRIPTIONS (quelques apprenants inscrits)
-- ============================================================================

-- David (id=5) inscrit au cours Python (id=1) avec progression
INSERT INTO inscriptions (apprenant_id, cours_id, pourcentage_progression, statut) VALUES
(5, 1, 33.33, 'EN_COURS'),
(5, 3, 0.00, 'EN_COURS'),
(5, 5, 0.00, 'EN_COURS');

-- Fatima (id=6) inscrite au cours Python et HTML
INSERT INTO inscriptions (apprenant_id, cours_id, pourcentage_progression, statut) VALUES
(6, 1, 66.67, 'EN_COURS'),
(6, 3, 50.00, 'EN_COURS');

-- Omar (id=7) inscrit au cours SQL
INSERT INTO inscriptions (apprenant_id, cours_id, pourcentage_progression, statut) VALUES
(7, 5, 0.00, 'EN_COURS');

-- ============================================================================
-- 8. PROGRESSION (David a complété 3 leçons sur le cours Python)
-- ============================================================================

INSERT INTO progression_lecons (inscription_id, lecon_id, est_completee, date_completion) VALUES
(1, 1, TRUE, NOW()),
(1, 2, TRUE, NOW()),
(1, 3, TRUE, NOW()),
(1, 4, FALSE, NULL);

-- ============================================================================
-- 9. AVIS
-- ============================================================================

INSERT INTO avis (apprenant_id, cours_id, note, commentaire) VALUES
(5, 1, 5, 'Excellent cours ! Les explications sont claires et les exemples pratiques. Je recommande vivement.'),
(6, 1, 4, 'Très bon cours pour débuter en Data Science. Il manque quelques exercices supplémentaires.'),
(6, 3, 5, 'Parfait pour les débutants. Sophie explique très bien les concepts.');

-- ============================================================================
-- 10. DISCUSSION Q&R
-- ============================================================================

INSERT INTO fils_discussion (cours_id, auteur_id, titre, contenu, est_resolu) VALUES
(1, 5, 'Différence entre liste et tuple ?',
 'Bonjour, je ne comprends pas bien la différence entre une liste et un tuple en Python. Quelqu''un peut m''expliquer ?',
 TRUE),
(1, 6, 'Erreur ModuleNotFoundError',
 'J''ai l''erreur "ModuleNotFoundError: No module named pandas" quand j''essaie d''importer Pandas. Comment résoudre ?',
 FALSE);

INSERT INTO messages_discussion (fil_id, auteur_id, contenu, est_reponse_officielle) VALUES
(1, 2, 'Bonne question ! La différence principale est qu''une liste est modifiable (mutable) tandis qu''un tuple est immuable. Utilisez un tuple quand les données ne doivent pas changer.', TRUE),
(1, 6, 'Merci pour l''explication claire !', FALSE),
(2, 7, 'Essaie de faire : pip install pandas dans ton terminal.', FALSE);

-- ============================================================================
-- Fin du seed
-- ============================================================================

SELECT '✅ Seed exécuté avec succès !' AS resultat;
SELECT CONCAT(COUNT(*), ' utilisateurs') AS stats FROM utilisateurs
UNION ALL
SELECT CONCAT(COUNT(*), ' catégories') FROM categories
UNION ALL
SELECT CONCAT(COUNT(*), ' cours') FROM cours
UNION ALL
SELECT CONCAT(COUNT(*), ' sections') FROM sections
UNION ALL
SELECT CONCAT(COUNT(*), ' leçons') FROM lecons
UNION ALL
SELECT CONCAT(COUNT(*), ' quiz') FROM quiz
UNION ALL
SELECT CONCAT(COUNT(*), ' inscriptions') FROM inscriptions;