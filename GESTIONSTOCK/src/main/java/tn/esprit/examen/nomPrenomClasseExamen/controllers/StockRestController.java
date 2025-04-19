package tn.esprit.examen.nomPrenomClasseExamen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examen.nomPrenomClasseExamen.entities.ChatRequestDTO;
import tn.esprit.examen.nomPrenomClasseExamen.entities.ChatResponseDTO;
import tn.esprit.examen.nomPrenomClasseExamen.entities.OfferDTO;
import tn.esprit.examen.nomPrenomClasseExamen.entities.Stock;
import tn.esprit.examen.nomPrenomClasseExamen.repositories.StockRepo;
import tn.esprit.examen.nomPrenomClasseExamen.services.CohereService;
import tn.esprit.examen.nomPrenomClasseExamen.services.OfferClient;
import tn.esprit.examen.nomPrenomClasseExamen.services.PdfService;
import tn.esprit.examen.nomPrenomClasseExamen.services.QrCodeService;
import tn.esprit.examen.nomPrenomClasseExamen.services.StockService;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockRestController {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepo stockRepo;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private CohereService cohereService;

    @Autowired
    private OfferClient offerClient;

    // 🔹 Ajouter un stock
    @PostMapping("/addStock")
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        return new ResponseEntity<>(stockService.addStock(stock), HttpStatus.CREATED);
    }

    // 🔹 Mettre à jour un stock
    @PutMapping("/updateStock/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable String id, @RequestBody Stock stock) {
        Stock updatedStock = stockService.updateStock(id, stock);
        return updatedStock != null
                ? new ResponseEntity<>(updatedStock, HttpStatus.OK)
                : ResponseEntity.notFound().build();
    }

    // 🔹 Supprimer un stock
    @DeleteMapping("/deleteStock/{id}")
    public ResponseEntity<String> deleteStock(@PathVariable String id) {
        return stockService.deleteStock(id)
                .equals("Stock supprimé.")
                ? ResponseEntity.ok("Stock supprimé.")
                : ResponseEntity.notFound().build();
    }

    // 🔹 Récupérer tous les stocks
    @GetMapping("/getAllStocks")
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    // 🔹 Récupérer un stock par ID
    @GetMapping("/getStockById/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable String id) {
        return ResponseEntity.of(stockService.getStockById(id));
    }

    // 🔹 Compter les stocks
    @GetMapping("/count")
    public ResponseEntity<Long> countStocks() {
        return ResponseEntity.ok(stockService.countStocks());
    }

    // 🔹 Rechercher des stocks par nom
    @GetMapping("/search/{name}")
    public ResponseEntity<List<Stock>> searchStocks(@PathVariable String name) {
        return ResponseEntity.ok(stockService.searchStocks(name));
    }

    // 🔹 Trier les stocks par prix (ordre décroissant par défaut)
    @GetMapping("/sortByPrice")
    public ResponseEntity<List<Stock>> sortStocksByPrice() {
        return ResponseEntity.ok(stockService.sortStocksByPriceDesc());
    }

    // 🔹 Trier les stocks par prix
    @GetMapping("/sortByPriceByOrder/{order}")
    public ResponseEntity<List<Stock>> sortStocksByPrice(@PathVariable String order) {
        return ResponseEntity.ok(stockService.sortStocksByPrice(order));
    }

    // 🔹 Chatbot
    @PostMapping("/chatbot/ask")
    public ResponseEntity<ChatResponseDTO> askChatbot(@RequestBody ChatRequestDTO chatRequest) {
        String response = cohereService.getChatbotResponse(chatRequest.getText());
        return ResponseEntity.ok(new ChatResponseDTO(response));
    }

    @GetMapping(value = "/qr/{id}")
    public ResponseEntity<?> generateQrCode(@PathVariable String id) {
        return stockRepo.findById(id)
                .map(stock -> {
                    try {
                        byte[] image = qrCodeService.generateQrCode(
                                "Stock ID: " + stock.getIdStock() + ", Name: " + stock.getStockName() + ", Quantity: " + stock.getStockQty(),
                                250, 250);
                        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
                    } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generateStockPdf(@PathVariable String id) {
        return stockRepo.findById(id)
                .map(stock -> {
                    try {
                        byte[] pdf = pdfService.generateStockPdf(stock);
                        return ResponseEntity.ok()
                                .header("Content-Disposition", "attachment; filename=stock_" + id + ".pdf")
                                .body(pdf);
                    } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Nouvelle méthode pour récupérer toutes les offres
    @GetMapping("/offers")
    public ResponseEntity<List<OfferDTO>> getAllOffers() {
        return ResponseEntity.ok(offerClient.getAllOffers());
    }

    // 🔹 Nouvelle méthode pour récupérer une offre par ID
    @GetMapping("/offers/{id}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable int id) {
        OfferDTO offer = offerClient.getOfferById(id);
        return offer != null ? ResponseEntity.ok(offer) : ResponseEntity.notFound().build();
    }
}