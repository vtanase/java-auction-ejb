package com.vlad.dao;
import javax.ejb.Local;

import com.vlad.dto.LicitatieDTO;
import com.vlad.dto.OfertaDTO;

@Local
public interface OfertaDAO {

    public void save(OfertaDTO oferta);
    public OfertaDTO getOfertaMaximaPentruLicitatie(LicitatieDTO licitatie);
}
