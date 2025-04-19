package com.example.reclamation;

import com.example.reclamation.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
    List<Reclamation> findByClientNomContainingIgnoreCaseOrProduitContainingIgnoreCase(String clientNom, String produit);
    List<Reclamation> findByStatutContainingIgnoreCase(String statut);

}