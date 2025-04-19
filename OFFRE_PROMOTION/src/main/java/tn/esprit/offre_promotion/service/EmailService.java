package tn.esprit.offre_promotion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.esprit.offre_promotion.entities.Offer;

import java.util.logging.Logger;

@Service
public class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.to}")
    private String toEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNewOfferEmail(Offer offer) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Nouvelle Offre Ajoutée");
            message.setText("Bonjour Oday,\n\n" +
                    "Une nouvelle offre a été ajoutée avec les détails suivants :\n" +
                    "ID: " + offer.getId() + "\n" +
                    "Nom: " + offer.getName() + "\n" +
                    "Description: " + offer.getDescription() + "\n" +
                    "Date de début: " + offer.getDateDebut() + "\n" +
                    "Date de fin: " + offer.getDateFin() + "\n" +
                    "Remise (%): " + offer.getRemisePourcentage() + "\n\n" +
                    "Cordialement,\n" +
                    "Votre application Offre Promotion");

            mailSender.send(message);
            LOGGER.info("E-mail envoyé avec succès à " + toEmail + " pour la nouvelle offre ID: " + offer.getId());
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
            e.printStackTrace();
        }
    }
}