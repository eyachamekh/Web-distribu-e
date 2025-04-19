package tn.esprit.examen.nomPrenomClasseExamen.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.examen.nomPrenomClasseExamen.entities.OfferDTO;

import java.util.List;

@FeignClient(name = "OFFREPROMOTION")
public interface OfferClient {

    @GetMapping("/AllOffer")
    List<OfferDTO> getAllOffers();

    @GetMapping("/GetOffer/{id}")
    OfferDTO getOfferById(@PathVariable("id") int id);

}