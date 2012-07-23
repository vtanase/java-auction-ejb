package com.vlad.dao;
import java.util.List;

import javax.ejb.Local;

import com.vlad.dto.LicitatieDTO;
import com.vlad.dto.UtilizatorDTO;

@Local
public interface LicitatieDAO {

    public LicitatieDTO save(LicitatieDTO licitatie);
    public LicitatieDTO find(Long idLicitatie);
    public List<LicitatieDTO> getLicitatiiForVanzator(UtilizatorDTO vanzator);
    public List<LicitatieDTO> getLicitatiiActive();
    public List<LicitatieDTO> getLicitatiiFaraStatus();
    public void modificaStatusLicitatie(Long idLicitatie, Integer status);
    public void modificaStatusLicitatii(List <LicitatieDTO> licitatii, Integer newStatus);
}
