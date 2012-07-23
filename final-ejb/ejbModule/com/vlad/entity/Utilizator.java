package com.vlad.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Utilizator
 * 
 */
@Entity
@Table(name = "utilizator")
public class Utilizator implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "nume")
    private String nume;
    @Column(name = "email")
    private String email;
    @OneToMany(mappedBy = "vanzator")
    private List<Licitatie> licitatii;
    @OneToMany(mappedBy = "utilizator")
    private List<Oferta> oferte;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<Licitatie> getLicitatii() {
        return licitatii;
    }
    public void setLicitatii(List<Licitatie> licitatii) {
        this.licitatii = licitatii;
    }
    public List<Oferta> getOferte() {
        return oferte;
    }
    public void setOferte(List<Oferta> oferte) {
        this.oferte = oferte;
    }
}
