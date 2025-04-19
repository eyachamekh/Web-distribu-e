package tn.esprit.examen.nomPrenomClasseExamen.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.examen.nomPrenomClasseExamen.entities.*;


import java.util.List;

public interface StockRepo extends MongoRepository<Stock, String> {

    List<Stock> findByStockNameContainingIgnoreCase(String s);
}
