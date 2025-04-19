package com.example.reclamation;

import com.example.reclamation.dto.StockDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "GESTIONSTOCK", fallback = StockClientFallback.class)
public interface StockClient {

    @GetMapping("/api/stocks/getAllStocks")
    List<StockDTO> getAllStocks();

    @GetMapping("/api/stocks/getStockById/{id}")
    StockDTO getStockById(@PathVariable("id") String id);
}

class StockClientFallback implements StockClient {
    @Override
    public List<StockDTO> getAllStocks() {
        return List.of();
    }

    @Override
    public StockDTO getStockById(String id) {
        return new StockDTO(id, "Produit inconnu", 0, false);
    }
}