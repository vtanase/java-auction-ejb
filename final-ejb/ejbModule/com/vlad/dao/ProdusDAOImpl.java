package com.vlad.dao;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import com.vlad.conversie.DtoToEntity;
import com.vlad.dto.ProdusDTO;
import com.vlad.entity.Carte;
import com.vlad.entity.Produs;

/**
 * Session Bean implementation class ProdusDAOImpl
 */
@Stateless
public class ProdusDAOImpl implements ProdusDAO {
    
    private Logger logger = Logger.getLogger(ProdusDAOImpl.class);
    @EJB
    private DtoToEntity converterFromDto;
    @PersistenceContext
    private EntityManager entityManager;

    public ProdusDTO save(ProdusDTO produsDto) {
        ProdusDTO result = null;
        if (produsDto == null) {
            logger.error("Can not save a null product");
        } else {
            if (produsDto.isCarte()) {
                result = this.saveCarte(produsDto);
            } else {
                logger.debug("Inseram produsul cu descrierea: " + produsDto.getDescriere());
                Produs produs = converterFromDto.toEntityProdus(produsDto);
                entityManager.persist(produs);
                entityManager.flush();
                if (produs.getId() != null) {
                    logger.debug("Am inserat produsul cu id-ul: " + produs.getId());
                    result = produsDto;
                    result.setId(produs.getId());
                }
            }
        }
        return result;
    }
    
    private ProdusDTO saveCarte(ProdusDTO produsDto) {
        ProdusDTO result = null;
        logger.debug("Inseram cartea cu titlul: " + produsDto.getTitlu());
        Carte carte = converterFromDto.toEntityCarte(produsDto);
        entityManager.persist(carte);
        entityManager.flush();
        if (carte.getId() != null) {
            logger.debug("Am inserat cartea cu id-ul: " + carte.getId());
            result = produsDto;
            result.setId(carte.getId());
        }
        return result;
    }
}
