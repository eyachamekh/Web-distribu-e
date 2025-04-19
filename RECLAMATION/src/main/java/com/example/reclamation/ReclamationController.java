package com.example.reclamation;

import com.example.reclamation.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reclamations")
public class ReclamationController {

    @Autowired
    private ReclamationService reclamationService;

    @Autowired
    private EmailSmsService emailSmsService;

    @Autowired
    private StockClient stockClient;

    @PostMapping("/add")
    public ResponseEntity<Reclamation> createReclamation(@RequestBody Reclamation reclamation) {
        // Vérifier si le produit existe dans le stock
        List<StockDTO> stocks = stockClient.getAllStocks();
        boolean produitExiste = stocks.stream()
                .anyMatch(stock -> stock.getProduit().equalsIgnoreCase(reclamation.getProduit()) && stock.isDisponible());
        if (!produitExiste) {
            throw new RuntimeException("Produit " + reclamation.getProduit() + " non disponible ou inexistant en stock");
        }

        // Enregistrer la réclamation
        Reclamation savedReclamation = reclamationService.addReclamation(reclamation);

        // Envoyer notification par email
        String adminEmail = "chamekheya1@gmail.com";
        String emailMessage = "New reclamation added: " + savedReclamation.getClientNom() + " - " + savedReclamation.getProduit();
        emailSmsService.sendEmail(adminEmail, "New Reclamation Notification", emailMessage);

        // Envoyer notification par SMS
        String adminPhoneNumber = "93377210";
        String smsMessage = "New reclamation added: " + savedReclamation.getClientNom() + " - " + savedReclamation.getProduit();
        emailSmsService.sendSms(adminPhoneNumber, smsMessage);

        return ResponseEntity.ok(savedReclamation);
    }

    @PostMapping("/addWithFile")
    public ResponseEntity<Reclamation> createReclamationWithFile(
            @RequestParam("clientNom") String clientNom,
            @RequestParam("produit") String produit,
            @RequestParam("message") String message,
            @RequestParam("statut") String statut,
            @RequestParam("file") MultipartFile file,
            @RequestParam("phoneNumber") String phoneNumber) {

        // Vérifier si le produit existe dans le stock
        List<StockDTO> stocks = stockClient.getAllStocks();
        boolean produitExiste = stocks.stream()
                .anyMatch(stock -> stock.getProduit().equalsIgnoreCase(produit) && stock.isDisponible());
        if (!produitExiste) {
            throw new RuntimeException("Produit " + produit + " non disponible ou inexistant en stock");
        }

        Reclamation reclamation = new Reclamation(clientNom, produit, message, statut, phoneNumber);
        Reclamation savedReclamation = reclamationService.addReclamationWithFile(reclamation, file);

        // Envoyer notification par email
        String adminEmail = "chamekheya1@gmail.com";
        String emailMessage = "New reclamation with file added: " + savedReclamation.getClientNom() + " - " + savedReclamation.getProduit();
        emailSmsService.sendEmail(adminEmail, "New Reclamation Notification", emailMessage);

        // Envoyer notification par SMS
        String adminPhoneNumber = "93377210";
        String smsMessage = "New reclamation with file added: " + savedReclamation.getClientNom() + " - " + savedReclamation.getProduit();
        emailSmsService.sendSms(adminPhoneNumber, smsMessage);

        return ResponseEntity.ok(savedReclamation);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get("uploads").resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new FileNotFoundException("File not found: " + filename);
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Reclamation> updateReclamation(@PathVariable Long id, @RequestBody Reclamation reclamation) {
        // Vérifier si le produit existe dans le stock
        List<StockDTO> stocks = stockClient.getAllStocks();
        boolean produitExiste = stocks.stream()
                .anyMatch(stock -> stock.getProduit().equalsIgnoreCase(reclamation.getProduit()) && stock.isDisponible());
        if (!produitExiste) {
            throw new RuntimeException("Produit " + reclamation.getProduit() + " non disponible ou inexistant en stock");
        }

        Reclamation updatedReclamation = reclamationService.updateReclamation(id, reclamation);
        return ResponseEntity.ok(updatedReclamation);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReclamation(@PathVariable Long id) {
        return ResponseEntity.ok(reclamationService.deleteReclamation(id));
    }

    @GetMapping
    public ResponseEntity<List<Reclamation>> getAllReclamations() {
        return ResponseEntity.ok(reclamationService.getAllReclamations());
    }

    @GetMapping("/api/stocks/getAllStocks")
    public ResponseEntity<List<ReclamationDTO>> getAllStocks() {
        List<Reclamation> reclamations = reclamationService.getAllReclamations();
        List<ReclamationDTO> reclamationDTOs = reclamations.stream()
                .map(r -> new ReclamationDTO(
                        r.getId(),
                        r.getClientNom(),
                        r.getProduit(),
                        r.getMessage(),
                        r.getStatut(),
                        r.getPhoneNumber(),
                        r.getFileName()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(reclamationDTOs);
    }

    @GetMapping("/api/stocks/getStockById/{id}")
    public ResponseEntity<ReclamationDTO> getStockById(@PathVariable("id") String id) {
        Long reclamationId = Long.parseLong(id);
        Reclamation reclamation = reclamationService.getReclamationById(reclamationId);
        ReclamationDTO reclamationDTO = new ReclamationDTO(
                reclamation.getId(),
                reclamation.getClientNom(),
                reclamation.getProduit(),
                reclamation.getMessage(),
                reclamation.getStatut(),
                reclamation.getPhoneNumber(),
                reclamation.getFileName()
        );
        return ResponseEntity.ok(reclamationDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Reclamation>> searchReclamations(@RequestParam("q") String query) {
        List<Reclamation> results = reclamationService.searchByClientNomOrProduit(query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/filterByStatus")
    public ResponseEntity<List<Reclamation>> filterReclamationsByStatus(@RequestParam("status") String status) {
        List<Reclamation> results = reclamationService.filterByStatus(status);
        return ResponseEntity.ok(results);
    }
}