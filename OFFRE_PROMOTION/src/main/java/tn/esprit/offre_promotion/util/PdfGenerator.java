package tn.esprit.offre_promotion.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import tn.esprit.offre_promotion.entities.Offer;

import java.io.ByteArrayOutputStream;
import java.util.logging.Logger;

public class PdfGenerator {

    private static final Logger LOGGER = Logger.getLogger(PdfGenerator.class.getName());

    public static byte[] generateOfferPdf(Offer offer) throws Exception {
        // Créer un flux de sortie pour le PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Ajouter un titre
        document.add(new Paragraph(new Text("Détails de l'Offre").setBold().setFontSize(16)));

        // Ajouter les informations de l'offre
        document.add(new Paragraph("ID: " + offer.getId()));
        document.add(new Paragraph("Nom: " + offer.getName()));
        document.add(new Paragraph("Description: " + offer.getDescription()));
        document.add(new Paragraph("Date de début: " + offer.getDateDebut()));
        document.add(new Paragraph("Date de fin: " + offer.getDateFin()));
        document.add(new Paragraph("Remise (%): " + offer.getRemisePourcentage()));

        // Générer un code QR
        try {
            String qrContent = "Offre ID: " + offer.getId() + "\n" +
                    "Nom: " + offer.getName() + "\n" +
                    "Description: " + offer.getDescription() + "\n" +
                    "Date de début: " + offer.getDateDebut() + "\n" +
                    "Date de fin: " + offer.getDateFin() + "\n" +
                    "Remise: " + offer.getRemisePourcentage() + "%";
            LOGGER.info("Génération du code QR avec le contenu : " + qrContent);

            byte[] qrCodeBytes = generateQRCode(qrContent, 150, 150); // Réduisez la taille pour éviter les problèmes de mise en page
            LOGGER.info("Code QR généré, taille des bytes : " + qrCodeBytes.length);

            ImageData imageData = ImageDataFactory.create(qrCodeBytes);
            Image qrCodeImage = new Image(imageData);
            qrCodeImage.setMarginTop(20); // Ajouter une marge pour l'espacement

            // Ajouter le code QR au PDF
            document.add(new Paragraph("Code QR de l'Offre :"));
            document.add(qrCodeImage);
            LOGGER.info("Code QR ajouté au PDF");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la génération ou de l'ajout du code QR : " + e.getMessage());
            e.printStackTrace();
            document.add(new Paragraph("Erreur : Impossible de générer le code QR."));
        }

        // Fermer le document
        document.close();

        return baos.toByteArray();
    }

    private static byte[] generateQRCode(String text, int width, int height) throws WriterException, Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}