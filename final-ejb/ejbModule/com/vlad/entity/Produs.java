package com.vlad.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Produs
 * 
 */
@Entity
@Table(name = "produs")
@DiscriminatorColumn(name="disc", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("P")
public class Produs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "descriere")
    private String descriere;
    @Column(name = "imagine")
    private String imagine;
    @OneToOne(mappedBy = "produs")
    private Licitatie licitatie;

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
    public void setLicitatie(Licitatie licitatie) {
        this.licitatie = licitatie;
    }
    public Licitatie getLicitatie() {
        return licitatie;
    }
}
