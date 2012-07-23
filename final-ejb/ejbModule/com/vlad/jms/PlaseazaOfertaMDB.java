package com.vlad.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.vlad.dao.OfertaDAO;
import com.vlad.dto.OfertaDTO;

/**
 * Message-Driven Bean implementation class for: PlaseazaOfertaMDB
 * 
 */
@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic") }, mappedName = "ofertaTopic")
public class PlaseazaOfertaMDB implements MessageListener {
    
    private Logger logger = Logger.getLogger(PlaseazaOfertaMDB.class);
    @EJB
    private OfertaDAO ofertaDAO;

    public void onMessage(Message message) {
        try {
            if (message != null && message instanceof ObjectMessage) {
                logger.debug("Received message with id: " + message.getJMSMessageID());
                OfertaDTO oferta = (OfertaDTO) ((ObjectMessage)message).getObject();
                ofertaDAO.save(oferta);
            }
        } catch (JMSException e) {
            logger.error("Am avut o eroare la receptionarea unei oferte plasate prin JMS", e);
        }
    }

}
