package com.vlad.dao;

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
import com.vlad.dto.OfertaDTO;
import com.vlad.entity.Oferta;

/**
 * Session Bean implementation class OfertaDAOImpl
 */
@Stateless
public class OfertaDAOImpl implements OfertaDAO {

    private final Logger logger = Logger.getLogger(OfertaDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private EntityToDto converterFromEntity;
    @EJB
    private DtoToEntity converterFromDto;

    @Override
    public void save(OfertaDTO oferta) {
        if (oferta == null) {
            logger.error("Nu putem plasa o oferta nula.");
        } else if (oferta.getLicitatie() == null) {
            logger.error("Nu putem plasa o oferta fara licitatie");
        } else {
            OfertaDTO maximulCurent = this
                    .getOfertaMaximaPentruLicitatie(oferta.getLicitatie());
            Long valoareMaxima = oferta.getLicitatie().getValoareStart();
            Long rataDeCrestere = oferta.getLicitatie().getValoareCrestere();
            if (maximulCurent != null) {
                valoareMaxima = maximulCurent.getValoare();
            }
            logger
                    .debug("Valoarea maxima pentru licitatia cu id = "
                            + oferta.getLicitatie().getId() + " este: "
                            + valoareMaxima);
            if (oferta.getValoare() > (valoareMaxima + rataDeCrestere)) {
                oferta.setAprobat(true);
            } else {
                oferta.setAprobat(false);
            }
            Oferta ofertaEntity = converterFromDto.toEntity(oferta);
            entityManager.persist(ofertaEntity);
            entityManager.flush();
            if (ofertaEntity.getId() != null) {
                logger.debug("Am plasat oferta cu id-ul: " + ofertaEntity.getId());
            }
        }
    }

    public OfertaDTO getOfertaMaximaPentruLicitatie(LicitatieDTO licitatie) {
        OfertaDTO result = null;
        if (licitatie == null) {
            logger
                    .error("Nu putem returna oferta maxima pentru o licitatie nula.");
        } else {
            String sql = "select o from Oferta o where o.licitatie.id = :idLicitatie and o.aprobat = true order by o.valoare desc";
            Query query = entityManager.createQuery(sql);
            query.setParameter("idLicitatie", licitatie.getId());
            query.setMaxResults(1);
            logger.debug("Executing JPQL: " + sql);
            List<Oferta> oferte = query.getResultList();
            if (oferte != null && oferte.size() > 0) {
                result = converterFromEntity.toDto(oferte.get(0));
            }
        }
        return result;
    }

}
