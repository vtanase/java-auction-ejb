package com.vlad.ejb;
import java.util.List;

import javax.ejb.Remote;

import com.vlad.dto.LicitatieDTO;
import com.vlad.dto.OfertaDTO;
import com.vlad.dto.ProdusDTO;
import com.vlad.dto.UtilizatorDTO;

@Remote
public interface ManagerLicitatie {

    public UtilizatorDTO populeazaUtilizator(UtilizatorDTO utilizator);
    public ProdusDTO adaugareProdus(ProdusDTO produsDTO);
    public LicitatieDTO adaugareLicitatie(LicitatieDTO licitatieDTO);
    public List<LicitatieDTO> listeazaLicitatiiPentruVanzator(UtilizatorDTO vanzator);
    public List<LicitatieDTO> listeazaToateLicitatiileFaraStatus();
    public List<LicitatieDTO> listeazaToateLicitatiileDeschise();
    public List<UtilizatorDTO> listeazaVanzatorii();
    public void plaseazaOferta(OfertaDTO oferta);
    public LicitatieDTO getLicitatieCuOfertaMaxima(Long idLicitatie);
    public void modificaStatusLicitatie(Long idLicitatie, Integer status);
    public void modificaStatusLicitatii(List<LicitatieDTO> licitatii, Integer newStatus);
}
