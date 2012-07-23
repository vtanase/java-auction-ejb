package com.vlad.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Licitatie
 * 
 */
@Entity
@Table(name = "licitatie")
public class Licitatie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "valoare_start")
    private Long valoareStart;
    @Column(name = "valoare_crestere")
    private Long valoareCrestere;
    @Column(name = "status")
    private Integer status;
    @Column(name = "timp_deschidere")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timpDeschidere;
    @Column(name = "timp_inchidere")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timpInchidere;
    @ManyToOne
    @JoinColumn(name = "id_utilizator")
    private Utilizator vanzator;
    @OneToMany(mappedBy = "licitatie", fetch = FetchType.LAZY)
    private List<Oferta> oferte;
    @OneToOne
    @PrimaryKeyJoinColumn
    private Produs produs;

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
    public Utilizator getVanzator() {
        return vanzator;
    }
    public void setVanzator(Utilizator vanzator) {
        this.vanzator = vanzator;
    }
    public List<Oferta> getOferte() {
        return oferte;
    }
    public void setOferte(List<Oferta> oferte) {
        this.oferte = oferte;
    }
    public void setProdus(Produs produs) {
        this.produs = produs;
    }
    public Produs getProdus() {
        return produs;
    }
}
