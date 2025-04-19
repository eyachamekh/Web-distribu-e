package tn.esprit.examen.nomPrenomClasseExamen.services;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Stock;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    private final QrCodeService qrCodeService;

    public PdfService(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    public byte[] generateStockPdf(Stock stock) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Titre du document
        document.add(new Paragraph("Stock Details Report")
                .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Generated on: " + java.time.LocalDate.now())
                .setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph("\n"));

        // Détails du Stock (Section structurée)
        document.add(new Paragraph("Stock Information:")
                .setBold().setFontSize(14));

        // ID du Stock
        document.add(new Paragraph("Stock ID: " + stock.getIdStock()));

        // Nom du Stock
        document.add(new Paragraph("Stock Name: " + stock.getStockName()));

        // Prix du Stock
        document.add(new Paragraph("Stock Price: " + stock.getStockPrice() + " TND"));

        // Quantité du Stock
        document.add(new Paragraph("Stock Quantity: " + stock.getStockQty()));

        // Type du Stock
        document.add(new Paragraph("Stock Type: " + stock.getStockType()));

        // QR Code (Ajout d'une image de QR Code générée pour ce Stock)
        String qrText = "Stock ID: " + stock.getIdStock() + ", Name: " + stock.getStockName();
        byte[] qrImage = qrCodeService.generateQrCode(qrText, 150, 150);
        Image qr = new Image(ImageDataFactory.create(qrImage));
        qr.setAutoScale(true);  // Ajuster la taille automatiquement pour éviter tout débordement.
        document.add(new Paragraph("QR Code:"));
        document.add(qr);

        document.add(new Paragraph("\n"));

        // Fin du document
        document.close();

        return out.toByteArray();
    }
}
