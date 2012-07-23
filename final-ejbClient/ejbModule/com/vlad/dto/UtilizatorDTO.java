package com.vlad.dto;

import java.io.Serializable;

public class UtilizatorDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String nume;
    private String email;

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
}
