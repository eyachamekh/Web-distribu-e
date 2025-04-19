package tn.esprit.examen.nomPrenomClasseExamen.entities;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Document(collection = "Stock")
public class Stock {
    @Id
    private String idStock;
    private String stockName;
    private int stockPrice;
    @Setter
    private float stockQty;
    @Setter
    private String stockType;

    public void setIdStock(String idStock) {
        this.idStock = idStock;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setStockPrice(int stockPrice) {
        this.stockPrice = stockPrice;
    }
}
