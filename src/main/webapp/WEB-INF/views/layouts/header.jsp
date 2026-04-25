<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.titre} - LMS Platform</title>
    <!-- Bootstrap 5 via CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
          rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
          rel="stylesheet">
    <style>
        body {
            font-family: 'Segoe UI', system-ui, sans-serif;
            background-color: #f8f9fa;
        }
        .auth-container {
            max-width: 480px;
            margin: 60px auto;
        }
        .auth-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 20px rgba(0, 0, 0, 0.08);
            padding: 2.5rem;
        }
        .brand-title {
            color: #5a2d82;
            font-weight: 700;
        }
        .btn-primary {
            background-color: #5a2d82;
            border-color: #5a2d82;
        }
        .btn-primary:hover {
            background-color: #4a2569;
            border-color: #4a2569;
        }
        a {
            color: #5a2d82;
        }
    </style>
</head>
<body>