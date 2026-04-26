package com.formation.lms.controllers.student;

import com.formation.lms.models.*;
import com.formation.lms.services.CertificatService;
import com.formation.lms.services.InscriptionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.file.Files;
import java.util.Optional;

/**
 * Gère la génération et le téléchargement des certificats.
 * GET /student/certificate?inscriptionId=X → génère et télécharge le PDF
 */
@WebServlet("/student/certificate")
public class CertificatServlet extends HttpServlet {

    private CertificatService certificatService;
    private InscriptionService inscriptionService;

    @Override
    public void init() throws ServletException {
        this.certificatService = new CertificatService();
        this.inscriptionService = new InscriptionService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");
        String inscriptionIdStr = req.getParameter("inscriptionId");

        if (inscriptionIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/student/dashboard");
            return;
        }

        try {
            Long inscriptionId = Long.parseLong(inscriptionIdStr);

            // Chemin absolu du dossier webapp
            String cheminRacine = getServletContext().getRealPath("/");

            // Générer ou récupérer le certificat
            Certificat certificat = certificatService.genererOuRecupererCertificat(
                    inscriptionId, cheminRacine);

            // Chemin du fichier PDF
            String cheminFichier = cheminRacine + certificat.getFichierPdfUrl();
            File fichierPdf = new File(cheminFichier);

            if (!fichierPdf.exists()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Fichier certificat introuvable.");
                return;
            }

            // Envoyer le PDF au navigateur pour téléchargement
            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition",
                    "attachment; filename=\"certificat.pdf\"");
            resp.setContentLength((int) fichierPdf.length());

            try (InputStream is = new FileInputStream(fichierPdf);
                 OutputStream os = resp.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int lu;
                while ((lu = is.read(buffer)) != -1) {
                    os.write(buffer, 0, lu);
                }
            }

        } catch (IllegalStateException e) {
            // Cours pas encore complété à 100%
            resp.sendRedirect(req.getContextPath() +
                    "/student/dashboard?erreur=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() +
                    "/student/dashboard?erreur=Erreur+lors+de+la+génération+du+certificat");
        }
    }
}