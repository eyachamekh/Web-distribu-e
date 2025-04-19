package tn.esprit.offre_promotion.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.offre_promotion.entities.Offer;
import tn.esprit.offre_promotion.repositories.IOfferRepository;



import java.util.List;

@Service
public class ServiceImpl implements IService{

    private final IOfferRepository offerRepository;


    @Autowired
    public ServiceImpl(IOfferRepository offerRepository) {

        this.offerRepository = offerRepository;
    }
    @Override
    public Offer addOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    @Override
    public List<Offer> getAllOffer() {
        return offerRepository.findAll();
    }

    @Override
    public Offer findById(int id) {
        return offerRepository.findById(id).get();
    }

    @Override
    public void deleteById(int id) {
        offerRepository.deleteById(id);
    }

    @Override
    public Offer updateById(int id, Offer offer) {
        if (offerRepository.existsById(id)) {
            offer.setId((long) id);
            return offerRepository.save(offer);
        }
        return null;
    }


}
