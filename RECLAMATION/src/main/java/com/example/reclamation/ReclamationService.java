package com.example.reclamation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReclamationService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private ReclamationRepository reclamationRepository;
    @Autowired
    private EmailSmsService smsService;

    public Reclamation addReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }

    public Reclamation addReclamationWithFile(Reclamation reclamation, MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // Ensure the upload directory exists
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                // Generate unique file name
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.write(filePath, file.getBytes());

                // Set the file name in the reclamation entity
                reclamation.setFileName(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de l'enregistrement du fichier", e);
            }
        }
        return reclamationRepository.save(reclamation);
    }

    public Reclamation updateReclamation(Long id, Reclamation newReclamation) {
        Optional<Reclamation> optionalReclamation = reclamationRepository.findById(id);
        if (optionalReclamation.isPresent()) {
            Reclamation existingReclamation = optionalReclamation.get();
            existingReclamation.setClientNom(newReclamation.getClientNom());
            existingReclamation.setProduit(newReclamation.getProduit());
            existingReclamation.setMessage(newReclamation.getMessage());
            existingReclamation.setStatut(newReclamation.getStatut());

            // Send SMS when status changes
            if (!existingReclamation.getStatut().equals(newReclamation.getStatut())) {
                smsService.sendSms(existingReclamation.getPhoneNumber(),
                        "Your reclamation status has been updated to: " + newReclamation.getStatut());
            }

            return reclamationRepository.save(existingReclamation);
        }
        throw new RuntimeException("Réclamation introuvable pour l'ID : " + id);
    }

    public String deleteReclamation(Long id) {
        if (reclamationRepository.existsById(id)) {
            reclamationRepository.deleteById(id);
            return "Réclamation supprimée avec succès.";
        }
        throw new RuntimeException("Réclamation introuvable pour l'ID : " + id);
    }

    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    public Reclamation getReclamationById(Long id) {
        return reclamationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation introuvable pour l'ID : " + id));
    }

    public List<Reclamation> searchByClientNomOrProduit(String keyword) {
        return reclamationRepository.findByClientNomContainingIgnoreCaseOrProduitContainingIgnoreCase(keyword, keyword);
    }

    public List<Reclamation> filterByStatus(String status) {
        return reclamationRepository.findByStatutContainingIgnoreCase(status);
    }
}