package com.formation.lms.controllers;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.models.*;
import com.formation.lms.services.CertificatService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * Page publique de vérification d'un certificat.
 * GET /verify?code=XXXXX
 */
@WebServlet("/verify")
public class VerifyCertificateServlet extends HttpServlet {

    private CertificatService certificatService;

    @Override
    public void init() throws ServletException {
        this.certificatService = new CertificatService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String code = req.getParameter("code");

        if (code != null && !code.isEmpty()) {
            try {
                Optional<Certificat> optCert = certificatService.verifierCertificat(code);

                if (optCert.isPresent()) {
                    Certificat cert = optCert.get();

                    // Récupérer l'inscription associée
                    Inscription inscription = DAOFactory.getInscriptionDAO()
                            .findById(cert.getInscriptionId()).orElse(null);

                    if (inscription != null) {
                        Utilisateur apprenant = DAOFactory.getUtilisateurDAO()
                                .findById(inscription.getApprenantId()).orElse(null);
                        Cours cours = DAOFactory.getCoursDAO()
                                .findById(inscription.getCoursId()).orElse(null);

                        req.setAttribute("certValide", true);
                        req.setAttribute("certificat", cert);
                        req.setAttribute("apprenant", apprenant);
                        req.setAttribute("cours", cours);
                    }
                } else {
                    req.setAttribute("certValide", false);
                }

                req.setAttribute("code", code);

            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("certValide", false);
            }
        }

        req.getRequestDispatcher("/WEB-INF/views/public/verify-certificate.jsp")
                .forward(req, resp);
    }
}