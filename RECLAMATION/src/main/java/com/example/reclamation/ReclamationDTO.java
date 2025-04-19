package com.example.reclamation;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReclamationDTO implements Serializable {
    private Long id;
    private String clientNom;
    private String produit;
    private String message;
    private String statut;
    private String fileName;
    private String phoneNumber;

    public ReclamationDTO() {}

    public ReclamationDTO(Long id, String clientNom, String produit, String message, String statut, String phoneNumber, String fileName) {
        this.id = id;
        this.clientNom = clientNom;
        this.produit = produit;
        this.message = message;
        this.statut = statut;
        this.phoneNumber = phoneNumber;
        this.fileName = fileName;
    }
}