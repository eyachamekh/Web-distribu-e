package tn.esprit.examen.nomPrenomClasseExamen.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {
    private int idStock;
    private String stockName;
    private int stockPrice;
    private float stockQty;
    private String stockType;
}

