package com.vlad.entity;

import com.vlad.entity.Produs;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Carte
 * 
 */
@Entity
@DiscriminatorValue("C")
public class Carte extends Produs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "titlu")
    private String titlu;
    @Column(name = "autor")
    private String autor;

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
}
