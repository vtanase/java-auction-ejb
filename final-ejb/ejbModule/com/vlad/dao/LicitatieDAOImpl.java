package com.vlad.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.vlad.conversie.DtoToEntity;
import com.vlad.conversie.EntityToDto;
import com.vlad.dto.LicitatieDTO;
import com.vlad.dto.UtilizatorDTO;
import com.vlad.entity.Licitatie;
import com.vlad.entity.Utilizator;
import com.vlad.util.LicitatieConstants;

/**
 * Session Bean implementation class LicitatieDAOImpl
 */
@Stateless
public class LicitatieDAOImpl implements LicitatieDAO {

    private Logger logger = Logger.getLogger(LicitatieDAOImpl.class);

    @EJB
    private DtoToEntity converterFromDto;
    @EJB
    private EntityToDto converterFromEntity;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public LicitatieDTO save(LicitatieDTO licitatieDto) {
        LicitatieDTO result = null;
        if (licitatieDto == null) {
            logger.error("Nu putem salva o licitatie nula");
        } else {
            Licitatie licitatie = converterFromDto.toEntity(licitatieDto);
            entityManager.persist(licitatie);
            // nu avem nevoie de flush, pentru ca licitatia se insereaza cu id
            // prestabilit
            logger.info("Am adaugat licitatia cu id-ul: " + licitatie.getId());
            result = licitatieDto;
        }
        return result;
    }

    public List<LicitatieDTO> getLicitatiiForVanzator(UtilizatorDTO vanzator) {
        List<LicitatieDTO> result = new ArrayList<LicitatieDTO>();
        if (vanzator == null) {
            logger.error("Nu putem obtine lista de licitatii, vanzatorul primit ca parametru este null");
        } else {
            String sql = "select l from Licitatie l join fetch l.produs where l.vanzator.id = :idVanzator";
            Query query = entityManager.createQuery(sql);
            query.setParameter("idVanzator", vanzator.getId());
            logger.debug("Executing JPQL: " + sql);
            List<Licitatie> licitatii = query.getResultList();
            result = converterFromEntity.toDtoListLicitatie(licitatii);
            if (result != null) {
                logger.info("Am gasit " + result.size() + " licitatii pentru vanzator-ul cu id: " + vanzator.getId());
            }
        }
        return result;
    }

    @Override
    public List<LicitatieDTO> getLicitatiiActive() {
        List<LicitatieDTO> result = new ArrayList<LicitatieDTO>();
        String sql = "select l from Licitatie l where l.status = :status";
        Query query = entityManager.createQuery(sql);
        query.setParameter("status", LicitatieConstants.STATUS_LICITATIE_DESCHIS);
        List<Licitatie> licitatii = query.getResultList();
        logger.debug("Executing JPQL: " + sql);
        result = converterFromEntity.toDtoListLicitatie(licitatii);
        if (result != null) {
            logger.info("Am gasit " + result.size() + " licitatii active.");
        }
        return result;
    }

    @Override
    public LicitatieDTO find(Long idLicitatie) {
        LicitatieDTO result = null;
        if (idLicitatie == null || idLicitatie == 0L) {
            logger.error("Nu putem aduce o licitatie pentru un id inexistent");
        } else {
            Licitatie licitatie = entityManager.find(Licitatie.class, idLicitatie);
            result = converterFromEntity.toDto(licitatie);
        }
        return result;
    }

    @Override
    public void modificaStatusLicitatie(Long idLicitatie, Integer status) {
        if (idLicitatie == null) {
            logger.error("Nu putem inchide o licitatie fara un id");
        } else {
            Licitatie licitatie = entityManager.find(Licitatie.class, idLicitatie);
            if (licitatie == null) {
                logger.error("Licitatia cu id-ul " + idLicitatie + " nu a putut fi inchisa pentru ca nu exista in BD");
            } else {
                logger.info("Setam status-ul " + status + " pentru licitatia cu id: " + idLicitatie);
                licitatie.setStatus(status);
            }
        }
    }

    @Override
    public List<LicitatieDTO> getLicitatiiFaraStatus() {
        List<LicitatieDTO> result = new ArrayList<LicitatieDTO>();
        String sql = "select l from Licitatie l where l.status is null";
        Query query = entityManager.createQuery(sql);
        List<Licitatie> licitatii = query.getResultList();
        logger.debug("Executing JPQL: " + sql);
        result = converterFromEntity.toDtoListLicitatie(licitatii);
        if (result != null) {
            logger.info("Am gasit " + result.size() + " licitatii fara status.");
        }
        return result;
    }

    @Override
    public void modificaStatusLicitatii(List<LicitatieDTO> licitatii, Integer newStatus) {
        List<Long> ids = new ArrayList<Long>();
        if (licitatii == null) {
            logger.error("Nu putem actualiza statusul licitatiilor. Lista primita a fost null");
        } else {
            for (LicitatieDTO licitatie : licitatii) {
                ids.add(licitatie.getId());
            }
            if (ids.size() > 0) {
                String sql = "update Licitatie l set l.status = :status where l.id IN :ids";
                Query query = entityManager.createQuery(sql);
                query.setParameter("status", newStatus);
                query.setParameter("ids", ids);
                query.executeUpdate();
            }
        }
    }
}
