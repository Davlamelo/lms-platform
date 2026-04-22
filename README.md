# 🎓 LMS Platform

Plateforme d'apprentissage en ligne inspirée d'Udemy, développée en **Java / JDBC / Servlets / JSP**.

## 🚀 Fonctionnalités

- **3 rôles** : Apprenant, Instructeur, Administrateur
- **Catalogue de cours** avec recherche, filtres et catégories
- **Lecteur de cours** multi-format : vidéos, documents, quiz
- **Suivi de progression** granulaire par leçon
- **Système de quiz** avec scoring automatique
- **Certificats PDF** générés automatiquement à 100% de progression
- **Notes et avis** sur les cours
- **Forum Q&R** par cours
- **Dashboard admin** avec modération des cours et gestion utilisateurs

## 🛠️ Stack technique

| Couche | Technologie |
|--------|-------------|
| Langage | Java 21 |
| Web | Servlets 6.0 + JSP + JSTL |
| Persistence | JDBC (pas d'ORM) |
| SGBD | MySQL 8 |
| Pool connexions | HikariCP |
| Serveur | Tomcat 10.1 |
| Build | Maven |
| Sécurité | BCrypt (hash mots de passe) |
| PDF | iText 5 |
| Logs | Log4j2 |
| Front | Bootstrap 5 / Tailwind |

## 📁 Architecture

Projet structuré en **MVC** avec pattern **DAO + Interfaces** (SOLID - Dependency Inversion) :