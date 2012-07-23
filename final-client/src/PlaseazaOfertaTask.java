import java.util.Date;
import java.util.Scanner;

import com.vlad.dto.LicitatieDTO;
import com.vlad.dto.OfertaDTO;
import com.vlad.dto.UtilizatorDTO;
import com.vlad.ejb.ManagerLicitatie;
import com.vlad.util.LicitatieConstants;

public class PlaseazaOfertaTask implements Runnable {

    private ManagerLicitatie bean;
    private UtilizatorDTO utilizator;
    private LicitatieDTO licitatie;

    public PlaseazaOfertaTask(ManagerLicitatie bean, UtilizatorDTO utilizator, LicitatieDTO licitatie) {
        this.bean = bean;
        this.utilizator = utilizator;
        this.licitatie = licitatie;
    }

    @Override
    public void run() {
        plaseazaOfertaAutomat(bean, utilizator, licitatie);
    }

    private static void plaseazaOfertaAutomat(ManagerLicitatie bean, UtilizatorDTO utilizator, LicitatieDTO licitatie) {
        Long idLicitatie = licitatie.getId();
        LicitatieDTO licitatieCuOfertaMaxima = bean.getLicitatieCuOfertaMaxima(idLicitatie);
        if (LicitatieConstants.STATUS_LICITATIE_DESCHIS.equals(licitatieCuOfertaMaxima.getStatus())) {
            OfertaDTO ofertaMaxima = licitatieCuOfertaMaxima.getOfertaMaxima();
            OfertaDTO ofertaAutomata = new OfertaDTO();
            ofertaAutomata.setLicitatie(licitatie);
            ofertaAutomata.setOfertant(utilizator);
            ofertaAutomata.setData(new Date());
            Long valoare = licitatie.getValoareStart() + licitatie.getValoareCrestere() + 1;
            if (ofertaMaxima != null) {
                valoare = ofertaMaxima.getValoare() + licitatie.getValoareCrestere() + 1;
            }
            ofertaAutomata.setValoare(valoare);
            bean.plaseazaOferta(ofertaAutomata);
        }
    }

}
