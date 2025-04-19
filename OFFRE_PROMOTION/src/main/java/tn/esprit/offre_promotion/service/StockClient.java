package tn.esprit.offre_promotion.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.offre_promotion.entities.StockDTO;

import java.util.List;

@FeignClient(name = "GESTIONSTOCK")
public interface StockClient {

    @GetMapping("/api/stocks/getAllStocks")
    List<StockDTO> getAllStocks();

    @GetMapping("/api/stocks/getStockById/{id}")
    StockDTO getStockById(@PathVariable("id") String id);
}