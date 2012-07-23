package com.vlad.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Oferta
 * 
 */
@Entity
@Table(name = "oferta")
public class Oferta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "valoare")
    private Long valoare;
    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Column(name = "aprobat")
    private boolean aprobat;
    @ManyToOne
    @JoinColumn(name = "id_licitatie")
    private Licitatie licitatie;
    @ManyToOne
    @JoinColumn(name = "id_utilizator")
    private Utilizator utilizator;

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
    public Licitatie getLicitatie() {
        return licitatie;
    }
    public void setLicitatie(Licitatie licitatie) {
        this.licitatie = licitatie;
    }
    public Utilizator getUtilizator() {
        return utilizator;
    }
    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
    }
}
