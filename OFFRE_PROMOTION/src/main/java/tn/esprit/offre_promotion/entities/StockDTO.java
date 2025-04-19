package tn.esprit.offre_promotion.entities;

import lombok.Data;

@Data
public class StockDTO {
    private String idStock;
    private String stockName;
    private int stockQty;
    private double stockPrice;


}