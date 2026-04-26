package com.formation.lms.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilitaire pour la génération de certificats PDF avec iText 5.
 *
 * BOILERPLATE RÉUTILISABLE : la structure iText est identique pour tout PDF.
 * Seul le contenu (textes, couleurs, mise en page) change selon le projet.
 */
public class PDFGeneratorUtil {

    // Couleurs de la plateforme
    private static final BaseColor COULEUR_PRIMAIRE = new BaseColor(90, 45, 130);   // Violet
    private static final BaseColor COULEUR_OR = new BaseColor(212, 175, 55);         // Or
    private static final BaseColor COULEUR_TEXTE = new BaseColor(44, 62, 80);        // Gris foncé
    private static final BaseColor COULEUR_FOND = new BaseColor(248, 249, 250);      // Gris clair

    /**
     * Génère un certificat PDF en mémoire.
     *
     * @param nomApprenant    prénom + nom de l'apprenant
     * @param titreCours      titre du cours complété
     * @param nomInstructeur  prénom + nom de l'instructeur
     * @param dateEmission    date de génération du certificat
     * @param codeVerification code unique de vérification
     * @return le PDF sous forme de tableau d'octets
     */
    public static byte[] genererCertificat(String nomApprenant,
                                           String titreCours,
                                           String nomInstructeur,
                                           LocalDateTime dateEmission,
                                           String codeVerification)
            throws DocumentException {

        // Créer le document en mode paysage (A4 horizontal)
        Rectangle pageSize = PageSize.A4.rotate();
        Document document = new Document(pageSize, 50, 50, 50, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // === Fond coloré ===
        PdfContentByte canvas = writer.getDirectContentUnder();
        canvas.setColorFill(COULEUR_FOND);
        canvas.rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());
        canvas.fill();

        // === Bordure décorative ===
        canvas.setColorStroke(COULEUR_PRIMAIRE);
        canvas.setLineWidth(8f);
        canvas.rectangle(25, 25, pageSize.getWidth() - 50, pageSize.getHeight() - 50);
        canvas.stroke();

        canvas.setColorStroke(COULEUR_OR);
        canvas.setLineWidth(2f);
        canvas.rectangle(35, 35, pageSize.getWidth() - 70, pageSize.getHeight() - 70);
        canvas.stroke();

        // === Polices ===
        Font fonteTitrePrincipal = new Font(Font.FontFamily.HELVETICA, 36, Font.BOLD, COULEUR_PRIMAIRE);
        Font fonteSousTitre = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, COULEUR_TEXTE);
        Font fonteNomApprenant = new Font(Font.FontFamily.HELVETICA, 28, Font.BOLD | Font.ITALIC, COULEUR_PRIMAIRE);
        Font fonteTitreCours = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, COULEUR_TEXTE);
        Font fonteNormal = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, COULEUR_TEXTE);
        Font fontePetit = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.GRAY);
        Font fonteCode = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL, BaseColor.GRAY);

        // === En-tête : Logo / Nom plateforme ===
        Paragraph entete = new Paragraph("🎓 LMS Platform", fonteTitrePrincipal);
        entete.setAlignment(Element.ALIGN_CENTER);
        entete.setSpacingBefore(20f);
        entete.setSpacingAfter(5f);
        document.add(entete);

        // Ligne décorative sous le logo
        ajouterLigneDecorative(document, COULEUR_OR, pageSize.getWidth() - 200);

        // === Titre du certificat ===
        Paragraph titreCertificat = new Paragraph("CERTIFICAT DE RÉUSSITE", fonteSousTitre);
        titreCertificat.setAlignment(Element.ALIGN_CENTER);
        titreCertificat.setSpacingBefore(15f);
        titreCertificat.setSpacingAfter(25f);
        document.add(titreCertificat);

        // === Texte principal ===
        Paragraph texte1 = new Paragraph("Ce certificat est décerné à", fonteNormal);
        texte1.setAlignment(Element.ALIGN_CENTER);
        texte1.setSpacingAfter(5f);
        document.add(texte1);

        // Nom de l'apprenant (mis en valeur)
        Paragraph nomApprenantPara = new Paragraph(nomApprenant, fonteNomApprenant);
        nomApprenantPara.setAlignment(Element.ALIGN_CENTER);
        nomApprenantPara.setSpacingAfter(5f);
        document.add(nomApprenantPara);

        ajouterLigneDecorative(document, COULEUR_PRIMAIRE, 300);

        Paragraph texte2 = new Paragraph(
                "pour avoir complété avec succès le cours", fonteNormal);
        texte2.setAlignment(Element.ALIGN_CENTER);
        texte2.setSpacingBefore(15f);
        texte2.setSpacingAfter(10f);
        document.add(texte2);

        // Titre du cours (mis en valeur)
        Paragraph titreCoursPara = new Paragraph("« " + titreCours + " »", fonteTitreCours);
        titreCoursPara.setAlignment(Element.ALIGN_CENTER);
        titreCoursPara.setSpacingAfter(20f);
        document.add(titreCoursPara);

        // === Date et instructeur ===
        String dateFormatee = dateEmission.format(
                DateTimeFormatter.ofPattern("dd MMMM yyyy",
                        java.util.Locale.FRENCH));

        Paragraph infos = new Paragraph(
                "Délivré le " + dateFormatee + "  •  Instructeur : " + nomInstructeur,
                fonteNormal);
        infos.setAlignment(Element.ALIGN_CENTER);
        infos.setSpacingAfter(30f);
        document.add(infos);

        // === Ligne de signature ===
        PdfPTable tableSignature = new PdfPTable(2);
        tableSignature.setWidthPercentage(70);
        tableSignature.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableSignature.setSpacingBefore(20f);

        // Cellule instructeur
        PdfPCell cellInstructeur = new PdfPCell();
        cellInstructeur.setBorder(Rectangle.TOP);
        cellInstructeur.setBorderColor(COULEUR_PRIMAIRE);
        cellInstructeur.setPadding(8f);
        Paragraph signInst = new Paragraph(nomInstructeur + "\nInstructeur", fonteNormal);
        signInst.setAlignment(Element.ALIGN_CENTER);
        cellInstructeur.addElement(signInst);
        tableSignature.addCell(cellInstructeur);

        // Cellule plateforme
        PdfPCell cellPlateforme = new PdfPCell();
        cellPlateforme.setBorder(Rectangle.TOP);
        cellPlateforme.setBorderColor(COULEUR_PRIMAIRE);
        cellPlateforme.setPadding(8f);
        Paragraph signPlat = new Paragraph("LMS Platform\nPlateforme d'apprentissage", fonteNormal);
        signPlat.setAlignment(Element.ALIGN_CENTER);
        cellPlateforme.addElement(signPlat);
        tableSignature.addCell(cellPlateforme);

        document.add(tableSignature);

        // === Code de vérification (en bas) ===
        Paragraph codePara = new Paragraph(
                "Code de vérification : " + codeVerification, fonteCode);
        codePara.setAlignment(Element.ALIGN_CENTER);
        codePara.setSpacingBefore(20f);
        document.add(codePara);

        Paragraph urlVerif = new Paragraph(
                "Vérifiez l'authenticité sur : localhost:8088/lms-platform/verify?code="
                        + codeVerification, fontePetit);
        urlVerif.setAlignment(Element.ALIGN_CENTER);
        document.add(urlVerif);

        document.close();
        return baos.toByteArray();
    }

    /**
     * Ajoute une ligne décorative centrée.
     */
    private static void ajouterLigneDecorative(Document document,
                                               BaseColor couleur,
                                               float largeur)
            throws DocumentException {
        PdfPTable ligne = new PdfPTable(1);
        ligne.setWidthPercentage(largeur / 8.42f);
        ligne.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(couleur);
        cell.setBorderWidth(2f);
        cell.setPaddingBottom(5f);
        ligne.addCell(cell);

        document.add(ligne);
    }
}