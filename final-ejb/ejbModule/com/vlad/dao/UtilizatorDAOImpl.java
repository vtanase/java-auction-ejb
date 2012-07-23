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
import com.vlad.dto.UtilizatorDTO;
import com.vlad.entity.Utilizator;

/**
 * Session Bean implementation class UtilizatorDAOImpl
 */
@Stateless
public class UtilizatorDAOImpl implements UtilizatorDAO {

    private Logger logger = Logger.getLogger(UtilizatorDAOImpl.class);

    @EJB
    private DtoToEntity converterFromDto;
    @EJB
    private EntityToDto converterFromEntity;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UtilizatorDTO save(UtilizatorDTO utilizatorDto) {
        UtilizatorDTO result = null;
        if (utilizatorDto == null) {
            logger.error("Can not save a null user");
        } else {
            Utilizator utilizator = converterFromDto.toEntity(utilizatorDto);
            if (utilizator != null) {
                entityManager.persist(utilizator);
                entityManager.flush();
                if (utilizator.getId() != null) {
                    logger.info("Am inserat utilizatorul cu id: "
                            + utilizator.getId());
                    result = utilizatorDto;
                    result.setId(utilizator.getId());
                }
            }
        }
        return result;
    }

    @Override
    public List<UtilizatorDTO> getVanzatori() {
        List<UtilizatorDTO> result = new ArrayList<UtilizatorDTO>();
        String sql = "select distinct l.vanzator from Licitatie l";
        Query query = entityManager.createQuery(sql);
        List<Utilizator> vanzatori = query.getResultList();
        result = converterFromEntity.toDtoListUtilizator(vanzatori);
        if (result != null) {
            logger.info("Am gasit " + result.size() + " de vanzatori");
        }
        return result;
    }
}
