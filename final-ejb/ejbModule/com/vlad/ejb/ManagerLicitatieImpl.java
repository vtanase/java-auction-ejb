package com.vlad.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import com.vlad.dao.LicitatieDAO;
import com.vlad.dao.OfertaDAO;
import com.vlad.dao.ProdusDAO;
import com.vlad.dao.UtilizatorDAO;
import com.vlad.dto.LicitatieDTO;
import com.vlad.dto.OfertaDTO;
import com.vlad.dto.ProdusDTO;
import com.vlad.dto.UtilizatorDTO;
import com.vlad.entity.Utilizator;
import com.vlad.util.LicitatieConstants;
import com.vlad.validation.LicitatieValidator;
import com.vlad.validation.ValidationError;

/**
 * Session Bean implementation class ManagerLicitatieImpl
 */
@Stateless
public class ManagerLicitatieImpl implements ManagerLicitatie {

    private Logger logger = Logger.getLogger(ManagerLicitatieImpl.class);
    @EJB
    private UtilizatorDAO utilizatorDAO;
    @EJB
    private ProdusDAO produsDAO;
    @EJB
    private LicitatieDAO licitatieDAO;
    @EJB
    private OfertaDAO ofertaDAO;
    
    @Resource(mappedName = "ofertaConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "ofertaTopic")
    private Topic ofertaTopic;

    public UtilizatorDTO populeazaUtilizator(UtilizatorDTO utilizator) {
        UtilizatorDTO result = new UtilizatorDTO();
        if (utilizator == null) {
            logger.error("Nu putem adauga utilizatorul, input-ul a fost null");
        } else {
            result = utilizatorDAO.save(utilizator);
        }
        return result;
    }

    @Override
    public ProdusDTO adaugareProdus(ProdusDTO produsDTO) {
        ProdusDTO result = new ProdusDTO();
        if (produsDTO == null) {
            logger.error("Nu putem adauga produs-ul, input-ul a fost null.");
        } else {
            result = produsDAO.save(produsDTO);
        }
        return result;
    }

    @Override
    public LicitatieDTO adaugareLicitatie(LicitatieDTO licitatieDTO) {
        LicitatieDTO result = new LicitatieDTO();
        if (licitatieDTO == null) {
            logger.error("Nu putem adauga licitatie, input-ul a fost null");
        } else {
            Long today = new Date().getTime();
            if (licitatieDTO.getTimpDeschidere() != null
                    && licitatieDTO.getTimpInchidere() != null) {
                if (today > licitatieDTO.getTimpDeschidere().getTime()) {
                    licitatieDTO
                            .setStatus(LicitatieConstants.STATUS_LICITATIE_DESCHIS);
                }
                if (today > licitatieDTO.getTimpInchidere().getTime()) {
                    licitatieDTO
                            .setStatus(LicitatieConstants.STATUS_LICITATIE_INCHIS);
                }
            }
            List<ValidationError> validationErrors = LicitatieValidator.validate(licitatieDTO);
            if (validationErrors == null || validationErrors.size() == 0) {
                result = licitatieDAO.save(licitatieDTO);
            } else {
                result = licitatieDTO;
                result.setValidationErrors(validationErrors);
            }
        }
        return result;
    }

    @Override
    public List<LicitatieDTO> listeazaLicitatiiPentruVanzator(
            UtilizatorDTO vanzator) {
        List<LicitatieDTO> result = new ArrayList<LicitatieDTO>();
        if (vanzator == null) {
            logger.error("Nu putem lista licitatiile pentru un vanzator null");
        } else {
            result = licitatieDAO.getLicitatiiForVanzator(vanzator);
        }
        return result;
    }

    @Override
    public List<LicitatieDTO> listeazaToateLicitatiileDeschise() {
        return licitatieDAO.getLicitatiiActive();
    }

    @Override
    public List<UtilizatorDTO> listeazaVanzatorii() {
        return utilizatorDAO.getVanzatori();
    }

    @Override
    public void plaseazaOferta(OfertaDTO oferta) {
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(ofertaTopic);
            ObjectMessage message = session.createObjectMessage();
            message.setObject(oferta);
            messageProducer.send(message);
            messageProducer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            logger.error("Am avut o eroare la plasarea ofertei prin JMS", e);
        }
    }
    
    public LicitatieDTO getLicitatieCuOfertaMaxima(Long idLicitatie) {
        LicitatieDTO result = null;
        if (idLicitatie == null || idLicitatie == 0) {
            logger.error("Nu putem aduce o licitatie si oferta maxima fara un id");
        } else {
            result= licitatieDAO.find(idLicitatie);
            OfertaDTO ofertaMaxima = ofertaDAO.getOfertaMaximaPentruLicitatie(result);
            result.setOfertaMaxima(ofertaMaxima);
        }
        return result;
    }

    @Override
    public void modificaStatusLicitatie(Long idLicitatie, Integer status) {
        licitatieDAO.modificaStatusLicitatie(idLicitatie, status);
    }

    @Override
    public List<LicitatieDTO> listeazaToateLicitatiileFaraStatus() {
        return licitatieDAO.getLicitatiiFaraStatus();
    }

    @Override
    public void modificaStatusLicitatii(List<LicitatieDTO> licitatii, Integer newStatus) {
        licitatieDAO.modificaStatusLicitatii(licitatii, newStatus);
    }

}
