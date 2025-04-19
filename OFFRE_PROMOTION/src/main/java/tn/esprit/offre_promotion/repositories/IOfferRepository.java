package tn.esprit.offre_promotion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.offre_promotion.entities.Offer;

import java.util.List;

public interface IOfferRepository extends JpaRepository<Offer, Integer> {

    List<Offer> findAll();
}
