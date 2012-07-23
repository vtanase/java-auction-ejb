package com.vlad.dto;

import java.io.Serializable;

public class ProdusDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private boolean isCarte;
    private String descriere;
    private String imagine;
    private String titlu;
    private String autor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getImagine() {
        return imagine;
    }

    public void setImagine(String imagine) {
        this.imagine = imagine;
    }

    public boolean isCarte() {
        return isCarte;
    }

    public void setCarte(boolean isCarte) {
        this.isCarte = isCarte;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("descriere produs: ").append(this.getDescriere()).append("\n");
        sb.append("url produs: ").append(this.getImagine()).append("\n");
        if (isCarte) {
            sb.append("produsul este o carte cu titlul: ").append(this.getTitlu()).append(" scrisa de ").append(
                    this.getAutor()).append("\n");
        }
        return sb.toString();
    }
}
