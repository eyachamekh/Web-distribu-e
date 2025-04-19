package com.example.reclamation.dto;

import java.io.Serializable;

public class StockDTO implements Serializable {
    private String id;
    private String produit;
    private int quantite;
    private boolean disponible;

    public StockDTO() {}

    public StockDTO(String id, String produit, int quantite, boolean disponible) {
        this.id = id;
        this.produit = produit;
        this.quantite = quantite;
        this.disponible = disponible;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}