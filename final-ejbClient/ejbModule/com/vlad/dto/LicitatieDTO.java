package com.vlad.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.vlad.util.LicitatieConstants;
import com.vlad.validation.ValidationError;

public class LicitatieDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long valoareStart;
    private Long valoareCrestere;
    private Integer status;
    private Date timpDeschidere;
    private Date timpInchidere;
    private UtilizatorDTO vanzator;
    private ProdusDTO produs;
    private OfertaDTO ofertaMaxima;
    private List<ValidationError> validationErrors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getValoareStart() {
        return valoareStart;
    }

    public void setValoareStart(Long valoareStart) {
        this.valoareStart = valoareStart;
    }

    public Long getValoareCrestere() {
        return valoareCrestere;
    }

    public void setValoareCrestere(Long valoareCrestere) {
        this.valoareCrestere = valoareCrestere;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTimpDeschidere() {
        return timpDeschidere;
    }

    public void setTimpDeschidere(Date timpDeschidere) {
        this.timpDeschidere = timpDeschidere;
    }

    public Date getTimpInchidere() {
        return timpInchidere;
    }

    public void setTimpInchidere(Date timpInchidere) {
        this.timpInchidere = timpInchidere;
    }

    public void setVanzator(UtilizatorDTO vanzator) {
        this.vanzator = vanzator;
    }

    public UtilizatorDTO getVanzator() {
        return vanzator;
    }

    public void setProdus(ProdusDTO produs) {
        this.produs = produs;
    }

    public ProdusDTO getProdus() {
        return produs;
    }

    public void setOfertaMaxima(OfertaDTO ofertaMaxima) {
        this.ofertaMaxima = ofertaMaxima;
    }

    public OfertaDTO getOfertaMaxima() {
        return ofertaMaxima;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        sb.append("\n\nLicitatie:\n").append("-----------------\n");
        sb.append("id = ").append(this.getId()).append("\n");
        if (this.getStatus() != null) {
            if (this.getStatus().equals(
                    LicitatieConstants.STATUS_LICITATIE_DESCHIS)) {
                sb.append("status = DESCHIS\n");
            } else if (this.getStatus().equals(
                    LicitatieConstants.STATUS_LICITATIE_INCHIS)) {
                sb.append("status = INCHIS\n");
            } else {
                sb.append("status = OPRIT\n");
            }
        }
        sb.append("valoare start = ").append(this.getValoareStart()).append("\n");
        sb.append("valoare crestere = ").append(this.getValoareCrestere())
                .append("\n");
        sb.append("timp deschidere = ").append(
                df.format(this.getTimpDeschidere())).append("\n");
        sb.append("timp inchidere = ").append(
                df.format(this.getTimpInchidere())).append("\n");
        if (this.getProdus() != null) {
            sb.append(this.getProdus().toString());
        }

        return sb.toString();
    }
}
