package tn.esprit.offre_promotion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.offre_promotion.entities.Offer;
import tn.esprit.offre_promotion.entities.StockDTO;
import tn.esprit.offre_promotion.service.EmailService;
import tn.esprit.offre_promotion.service.IService;
import tn.esprit.offre_promotion.service.StockClient;
import tn.esprit.offre_promotion.util.PdfGenerator;

import java.util.List;

@RestController
public class ServiceContrl {

    private final IService service;
    private final EmailService emailService;
    private final StockClient stockClient;

    @Autowired
    public ServiceContrl(IService service, EmailService emailService, StockClient stockClient) {
        this.service = service;
        this.emailService = emailService;
        this.stockClient = stockClient;
    }

    @GetMapping("/")
    public String home() {
        return "Bienvenue sur l'API Offre Promotion 🚀";
    }

    @PostMapping("/addOffer")
    public Offer addOffer(@RequestBody Offer offer) {
        Offer savedOffer = service.addOffer(offer);
        emailService.sendNewOfferEmail(savedOffer);
        return savedOffer;
    }

    @GetMapping("/AllOffer")
    public List<Offer> getAllOffer() {
        return service.getAllOffer();
    }

    @GetMapping("/GetOffer/{id}")
    public Offer getOffer(@PathVariable int id) {
        return service.findById(id);
    }

    @DeleteMapping("/DeleteOffer/{id}")
    public void deleteOffer(@PathVariable int id) {
        service.deleteById(id);
    }

    @PutMapping("/UpdateOffer/{id}")
    public Offer updateOffer(@PathVariable int id, @RequestBody Offer offer) {
        return service.updateById(id, offer);
    }

    @GetMapping("/generateOfferPdf/{id}")
    public ResponseEntity<byte[]> generateOfferPdf(@PathVariable int id) throws Exception {
        Offer offer = service.findById(id);
        if (offer == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] pdfBytes = PdfGenerator.generateOfferPdf(offer);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "offer_" + id + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    // Nouvelle méthode pour récupérer tous les stocks
    @GetMapping("/stocks")
    public ResponseEntity<List<StockDTO>> getAllStocks() {
        return ResponseEntity.ok(stockClient.getAllStocks());
    }

    // Nouvelle méthode pour récupérer un stock par ID
    @GetMapping("/stocks/{id}")
    public ResponseEntity<StockDTO> getStockById(@PathVariable String id) {
        StockDTO stock = stockClient.getStockById(id);
        return stock != null ? ResponseEntity.ok(stock) : ResponseEntity.notFound().build();
    }
}
