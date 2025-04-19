package com.example.reclamation;

import jakarta.persistence.*;

@Entity
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientNom;
    private String produit;
    private String message;
    private String statut; // En attente, Traitée, Rejetée
    private String fileName;
    private String phoneNumber;

    public Reclamation() {}

    public Reclamation(String clientNom, String produit, String message, String statut, String phoneNumber) {
        this.clientNom = clientNom;
        this.produit = produit;
        this.message = message;
        this.statut = statut;
        this.phoneNumber = phoneNumber;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientNom() {
        return clientNom;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}