package tn.esprit.examen.nomPrenomClasseExamen.entities;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OfferDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int remisePourcentage;

}