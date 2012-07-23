package com.vlad.dto;

import java.io.Serializable;
import java.util.Date;

public class OfertaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long valoare;
    private Date data;
    private boolean aprobat;
    private UtilizatorDTO ofertant;
    private LicitatieDTO licitatie;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getValoare() {
        return valoare;
    }
    public void setValoare(Long valoare) {
        this.valoare = valoare;
    }
    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    }
    public boolean isAprobat() {
        return aprobat;
    }
    public void setAprobat(boolean aprobat) {
        this.aprobat = aprobat;
    }
    public void setOfertant(UtilizatorDTO ofertant) {
        this.ofertant = ofertant;
    }
    public UtilizatorDTO getOfertant() {
        return ofertant;
    }
    public void setLicitatie(LicitatieDTO licitatie) {
        this.licitatie = licitatie;
    }
    public LicitatieDTO getLicitatie() {
        return licitatie;
    }
    
    
}
