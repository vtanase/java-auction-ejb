package com.vlad.conversie;

import java.util.ArrayList;
import java.util.List;

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
 * Session Bean implementation class EntityToDto
 */
@LocalBean
@Stateless
public class EntityToDto {

    private final Logger logger = Logger.getLogger(EntityToDto.class);

    public List<LicitatieDTO> toDtoListLicitatie(List<Licitatie> entities) {
        List<LicitatieDTO> result = new ArrayList<LicitatieDTO>();
        if (entities == null) {
            logger
                    .error("Lista de entitati licitatie a fost nula, nu putem converti la DTO");
        } else {
            logger.debug("Incercam sa convertim " + entities.size()
                    + " licitatii la DTO-uri.");
            for (Licitatie entity : entities) {
                LicitatieDTO dto = this.toDto(entity);
                if (dto != null) {
                    result.add(dto);
                }
            }
            logger.debug("Am convertit " + result.size() + " din "
                    + entities.size() + " licitatii la DTO-uri.");
        }
        return result;
    }

    public List<UtilizatorDTO> toDtoListUtilizator(List<Utilizator> entities) {
        List<UtilizatorDTO> result = new ArrayList<UtilizatorDTO>();
        if (entities == null) {
            logger
                    .error("Lista de entitati utilizator a fost nula, nu putem converti la DTO-uri");
        } else {
            logger.debug("Incercam sa convertim " + entities.size()
                    + " utilizatori la DTO");
            for (Utilizator entity : entities) {
                UtilizatorDTO dto = this.toDto(entity);
                if (dto != null) {
                    result.add(dto);
                }
            }
            logger.debug("Am convertit " + result.size() + " din "
                    + entities.size() + " utilizatori la DTO-uri");
        }
        return result;
    }

    public LicitatieDTO toDto(Licitatie entity) {
        LicitatieDTO result = null;
        if (entity == null) {
            logger.error("Nu putem transforma o licitatie nula in DTO.");
        } else {
            result = new LicitatieDTO();
            result.setId(entity.getId());
            result.setStatus(entity.getStatus());
            result.setTimpDeschidere(entity.getTimpDeschidere());
            result.setTimpInchidere(entity.getTimpInchidere());
            result.setValoareStart(entity.getValoareStart());
            result.setValoareCrestere(entity.getValoareCrestere());
            result.setProdus(this.toDto(entity.getProdus()));
        }
        return result;
    }

    public ProdusDTO toDto(Produs entity) {
        ProdusDTO result = null;
        if (entity == null) {
            logger.error("Nu putem transforma un produs null in DTO");
        } else {
            result = new ProdusDTO();
            result.setDescriere(entity.getDescriere());
            result.setImagine(entity.getImagine());
            result.setCarte(false);
            if (entity instanceof Carte) {
                result.setCarte(true);
                result.setTitlu(((Carte) entity).getTitlu());
                result.setAutor(((Carte) entity).getAutor());
            }
        }
        return result;
    }

    public UtilizatorDTO toDto(Utilizator entity) {
        UtilizatorDTO result = null;
        if (entity == null) {
            logger.error("Nu putem transforma un utilizator null in DTO");
        } else {
            result = new UtilizatorDTO();
            result.setId(entity.getId());
            result.setNume(entity.getNume());
            result.setEmail(entity.getEmail());
        }
        return result;
    }
    
    public OfertaDTO toDto(Oferta entity) {
        OfertaDTO result = null;
        if (entity == null) {
            logger.error("Nu putem transforma o oferta null in DTO");
        } else {
            result = new OfertaDTO();
            result.setId(entity.getId());
            result.setValoare(entity.getValoare());
            result.setData(entity.getData());
            result.setAprobat(entity.isAprobat());
            result.setOfertant(this.toDto(entity.getUtilizator()));
        }
        return result;
    }
}
