import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vlad.dto.LicitatieDTO;
import com.vlad.ejb.ManagerLicitatie;
import com.vlad.util.LicitatieConstants;


public class UpdateLicitatieStatusTask implements Runnable {
    
    private ManagerLicitatie bean;
    
    public UpdateLicitatieStatusTask(ManagerLicitatie bean) {
        this.bean = bean;
    }

    @Override
    public void run() {
        System.out.println("Updatam statusul licitatiilor...");
        List<LicitatieDTO> licitatiiFaraStatus = bean.listeazaToateLicitatiileFaraStatus();
        List<LicitatieDTO> licitatiiDeschise = bean.listeazaToateLicitatiileDeschise();
        List<LicitatieDTO> licitatiiDeDeschis = new ArrayList<LicitatieDTO>();
        List<LicitatieDTO> licitatiiDeInchis  = new ArrayList<LicitatieDTO>();
        Date now = new Date();
        if (licitatiiFaraStatus != null && licitatiiFaraStatus.size() > 0) {
            // verificam daca timpul curent este mai mare decat timpul de
            // inceput
            // daca da atunci punem status = DESCHIS pe licitatie
            for (LicitatieDTO licitatie : licitatiiFaraStatus) {
                if (licitatie.getTimpDeschidere() != null && now.getTime() > licitatie.getTimpDeschidere().getTime()) {
                    licitatiiDeDeschis.add(licitatie);
                }
            }
        }
        if (licitatiiDeschise != null && licitatiiDeschise.size() > 0) {
            // verificam daca timpul curent este mai mare decat timpul de
            // inchidere
            // daca da atunci punem status = INCHIS pe licitatie
            for (LicitatieDTO licitatie : licitatiiDeschise) {
                if (licitatie.getTimpInchidere() != null && now.getTime() > licitatie.getTimpInchidere().getTime()) {
                    licitatiiDeInchis.add(licitatie);
                }
            }
        }
        System.out.println("Incercam sa deschidem: " + licitatiiDeDeschis.size() + " licitatii");
        System.out.print("ID-uri: ["); 
        for (LicitatieDTO licitatie : licitatiiDeDeschis) {
            System.out.println(licitatie.getId() + " ");
        }
        System.out.println("]");
        System.out.println("Incercam sa inchidem: " + licitatiiDeInchis.size() + " licitatii");
        System.out.print("ID-uri: [");
        for (LicitatieDTO licitatie : licitatiiDeInchis) {
            System.out.println(licitatie.getId() + " ");
        }
        System.out.println("]");
        bean.modificaStatusLicitatii(licitatiiDeDeschis, LicitatieConstants.STATUS_LICITATIE_DESCHIS);
        bean.modificaStatusLicitatii(licitatiiDeInchis, LicitatieConstants.STATUS_LICITATIE_INCHIS);
    }

}
