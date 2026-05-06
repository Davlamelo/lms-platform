package com.formation.lms.controllers.student;

import com.formation.lms.dao.factory.DAOFactory;
import com.formation.lms.models.CandidatureInstructeur;
import com.formation.lms.models.StatutCandidature;
import com.formation.lms.models.Utilisateur;
import com.formation.lms.utils.FileUploadUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize       = 10 * 1024 * 1024,
        maxRequestSize    = 15 * 1024 * 1024
)
@WebServlet("/become-instructor")
public class BecomeInstructorServlet extends HttpServlet {

    private static final String[] EXTENSIONS_CV = { ".pdf", ".doc", ".docx" };

    // MODIFIÉ : séparateur | fiable au lieu de \n (qui causait des problèmes JSP)
    private static final String SEPARATEUR = "|";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");

        if (utilisateur == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (!"APPRENANT".equals(utilisateur.getRole().name())) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        try {
            List<CandidatureInstructeur> candidatures = DAOFactory
                    .getCandidatureInstructeurDAO()
                    .findByUtilisateurId(utilisateur.getId());

            req.setAttribute("candidatures", candidatures);

            CandidatureInstructeur derniere = candidatures.isEmpty()
                    ? null : candidatures.get(0);

            // AJOUTÉ : normaliser les anciennes données (\n → |) au moment de la lecture
            if (derniere != null && derniere.getCvUrl() != null) {
                derniere.setCvUrl(normaliserSeparateurs(derniere.getCvUrl()));
            }

            req.setAttribute("derniereCandidature", derniere);

            boolean peutSoumettre = derniere == null
                    || derniere.getStatut() == StatutCandidature.REJETEE;
            req.setAttribute("peutSoumettre", peutSoumettre);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erreur", "Erreur lors du chargement.");
        }

        req.getRequestDispatcher("/WEB-INF/views/student/become-instructor.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Utilisateur utilisateur = (Utilisateur) req.getSession().getAttribute("utilisateur");

        if (utilisateur == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (!"APPRENANT".equals(utilisateur.getRole().name())) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        String motivation       = req.getParameter("motivation");
        String expertise        = req.getParameter("expertise");
        String liensExternesRaw = req.getParameter("liensExternesRaw");

        // Validation
        if (motivation == null || motivation.trim().length() < 50) {
            resp.sendRedirect(req.getContextPath() + "/become-instructor?erreur="
                    + enc("La motivation doit faire au moins 50 caractères."));
            return;
        }
        if (expertise == null || expertise.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/become-instructor?erreur="
                    + enc("Le domaine d'expertise est obligatoire."));
            return;
        }

        try {
            // Vérifier qu'il n'y a pas déjà une candidature EN_ATTENTE
            List<CandidatureInstructeur> existantes = DAOFactory
                    .getCandidatureInstructeurDAO()
                    .findByUtilisateurId(utilisateur.getId());

            boolean dejaEnAttente = existantes.stream()
                    .anyMatch(c -> c.getStatut() == StatutCandidature.EN_ATTENTE);

            if (dejaEnAttente) {
                resp.sendRedirect(req.getContextPath() + "/become-instructor?erreur="
                        + enc("Vous avez déjà une candidature en attente."));
                return;
            }

            CandidatureInstructeur candidature = new CandidatureInstructeur(
                    utilisateur.getId(),
                    motivation.trim(),
                    expertise.trim()
            );

            // --- Gestion upload CV (PDF, DOC, DOCX) ---
            String cheminFichierCv = null;
            Part partCv = req.getPart("cvFichier");
            if (partCv != null && partCv.getSize() > 0) {
                String nomOriginal = FileUploadUtil.extraireNomFichier(partCv);
                String extension   = FileUploadUtil
                        .extraireExtension(nomOriginal != null ? nomOriginal : "")
                        .toLowerCase();

                if (!FileUploadUtil.estExtensionAutorisee(extension, EXTENSIONS_CV)) {
                    resp.sendRedirect(req.getContextPath() + "/become-instructor?erreur="
                            + enc("Format non autorisé. Utilisez PDF, DOC ou DOCX."));
                    return;
                }

                String cheminRacine = getServletContext().getRealPath("/");
                String sousDossier  = "uploads" + File.separator + "cv" + File.separator;
                File dossier = new File(cheminRacine + File.separator + sousDossier);
                if (!dossier.exists()) {
                    dossier.mkdirs();
                }

                String nomFichier   = UUID.randomUUID().toString() + extension;
                String cheminAbsolu = dossier.getAbsolutePath() + File.separator + nomFichier;

                try (InputStream is = partCv.getInputStream();
                     OutputStream os = new FileOutputStream(cheminAbsolu)) {
                    byte[] buffer = new byte[4096];
                    int lu;
                    while ((lu = is.read(buffer)) != -1) {
                        os.write(buffer, 0, lu);
                    }
                }

                cheminFichierCv = "uploads/cv/" + nomFichier;
            }

            // --- CV + liens combinés avec | comme séparateur ---
            StringBuilder cvUrlBuilder = new StringBuilder();

            if (cheminFichierCv != null) {
                cvUrlBuilder.append(cheminFichierCv);
            }

            if (liensExternesRaw != null && !liensExternesRaw.trim().isEmpty()) {
                // Normaliser les sauts de ligne et filtrer les lignes vides
                String liensNettoyes = liensExternesRaw
                        .replace("\r\n", "\n")
                        .replace("\r", "\n")
                        .lines()
                        .map(String::trim)
                        .filter(l -> !l.isEmpty())
                        // MODIFIÉ : utiliser | comme séparateur au lieu de \n
                        .reduce((a, b) -> a + SEPARATEUR + b)
                        .orElse("").trim();

                if (!liensNettoyes.isEmpty()) {
                    if (cvUrlBuilder.length() > 0) {
                        cvUrlBuilder.append(SEPARATEUR);
                    }
                    cvUrlBuilder.append(liensNettoyes);
                }
            }

            if (cvUrlBuilder.length() > 0) {
                candidature.setCvUrl(cvUrlBuilder.toString());
            }

            DAOFactory.getCandidatureInstructeurDAO().save(candidature);

            resp.sendRedirect(req.getContextPath() + "/become-instructor?succes="
                    + enc("Votre candidature a été soumise avec succès ! L'équipe vous contactera sous 48h."));

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/become-instructor?erreur="
                    + enc("Erreur : " + e.getMessage()));
        }
    }

    /**
     * Normalise les anciennes données stockées avec \n ou \r\n
     * vers le nouveau format avec | comme séparateur.
     */
    private String normaliserSeparateurs(String cvUrl) {
        if (cvUrl == null) return null;
        // Remplace les anciens \r\n et \n par | (nouveau séparateur)
        return cvUrl
                .replace("\r\n", SEPARATEUR)
                .replace("\r", SEPARATEUR)
                .replace("\n", SEPARATEUR);
    }

    private String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}