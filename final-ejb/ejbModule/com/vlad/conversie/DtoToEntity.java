package com.vlad.conversie;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.vlad.dto.LicitatieDTO;
import com.vlad.dto.OfertaDTO;
import com.vlad.dto.ProdusDTO;
import com.vlad.dto.UtilizatorDTO;
import com.vlad.entity.Carte;
import com.vlad.entity.Licitatie;
import com.vlad.entity.Oferta;
import com.vlad.entity.Produs;
import com.vlad.entity.Utilizator;

/**
 * Session Bean implementation class DtoToEntity
 */
@LocalBean
@Stateless
public class DtoToEntity {
    
    private Logger logger = Logger.getLogger(DtoToEntity.class);

    public Utilizator toEntity(UtilizatorDTO utilizatorDto) {
        Utilizator result = null;
        if (utilizatorDto == null) {
            logger.error("Nu putem converti un utilizatorDTO null la entitate");
        } else {
            result = new Utilizator();
            result.setId(utilizatorDto.getId());
            result.setNume(utilizatorDto.getNume());
            result.setEmail(utilizatorDto.getEmail());
        }
        return result;
    }
    
    public Produs toEntityProdus(ProdusDTO produsDto) {
        Produs result = null;
        if (produsDto == null) {
            logger.error("Nu putem converti un produsDTO null la entitate produs");
        } else {
            result = new Produs();
            result.setId(produsDto.getId());
            result.setDescriere(produsDto.getDescriere());
            result.setImagine(produsDto.getImagine());
        }
        return result;
    }
    
    public Carte toEntityCarte(ProdusDTO produsDto) {
        Carte result = null;
        if (produsDto == null) {
            logger.error("Nu putem converti un produsDTO null la entitatea carte");
        } else {
            result = new Carte();
            result.setId(produsDto.getId());
            result.setDescriere(produsDto.getDescriere());
            result.setImagine(produsDto.getImagine());
            result.setTitlu(produsDto.getTitlu());
            result.setAutor(produsDto.getAutor());
        }
        return result;
    }
    
    public Licitatie toEntity(LicitatieDTO licitatieDto) {
        Licitatie result = null;
        if (licitatieDto == null) {
            logger.error("Nu putem converti o licitatieDTO nula la entitate");
        } else {
            result = new Licitatie();
            result.setId(licitatieDto.getId());
            result.setValoareStart(licitatieDto.getValoareStart());
            result.setValoareCrestere(licitatieDto.getValoareCrestere());
            result.setStatus(licitatieDto.getStatus());
            result.setTimpDeschidere(licitatieDto.getTimpDeschidere());
            result.setTimpInchidere(licitatieDto.getTimpInchidere());
            if (licitatieDto.getVanzator() != null) {
                Utilizator utilizator = new Utilizator();
                utilizator.setId(licitatieDto.getVanzator().getId());
                result.setVanzator(utilizator);
            }
        }
        return result;
    }
    
    public Oferta toEntity(OfertaDTO ofertaDto) {
        Oferta result = null;
        if (ofertaDto == null) {
            logger.error("Nu putem converti o ofertaDTO nula la entitate");
        } else {
            result = new Oferta();
            result.setId(ofertaDto.getId());
            result.setValoare(ofertaDto.getValoare());
            result.setData(ofertaDto.getData());
            result.setAprobat(ofertaDto.isAprobat());
            result.setUtilizator(this.toEntity(ofertaDto.getOfertant()));
            result.setLicitatie(this.toEntity(ofertaDto.getLicitatie()));
        }
        return result;
    }
}
