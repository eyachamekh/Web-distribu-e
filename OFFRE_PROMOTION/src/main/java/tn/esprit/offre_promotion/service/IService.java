package tn.esprit.offre_promotion.service;

import tn.esprit.offre_promotion.entities.Offer;

import java.util.List;

public interface IService {

     Offer addOffer(Offer offer);
     List<Offer> getAllOffer();
     Offer findById(int id);
     void deleteById(int id);
     Offer updateById(int id, Offer offer);
}
