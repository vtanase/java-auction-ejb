import java.util.Date;

import org.joda.time.Duration;
import org.joda.time.Interval;

import com.vlad.dto.LicitatieDTO;
import com.vlad.dto.UtilizatorDTO;
import com.vlad.ejb.ManagerLicitatie;
import com.vlad.util.LicitatieConstants;

public class UrmaresteLicitatieTask implements Runnable {

    ManagerLicitatie bean;
    UtilizatorDTO utilizator;
    Long idLicitatie;
    boolean showPlaseazaOfertaMenu;

    public UrmaresteLicitatieTask(ManagerLicitatie bean, UtilizatorDTO utilizator, Long idLicitatie,
            boolean showPlaseazaOfertaMenu) {
        this.bean = bean;
        this.utilizator = utilizator;
        this.idLicitatie = idLicitatie;
        this.showPlaseazaOfertaMenu = showPlaseazaOfertaMenu;
    }

    @Override
    public void run() {
        urmaresteLicitatie(bean, utilizator, idLicitatie);
    }

    private void urmaresteLicitatie(ManagerLicitatie bean, UtilizatorDTO utilizator, Long idLicitatie) {
        LicitatieDTO licitatieActiva = bean.getLicitatieCuOfertaMaxima(idLicitatie);
        System.out.println("\n\nUrmariti licitatia cu id: " + idLicitatie);
        if (licitatieActiva != null) {
            Long valoareOfertaMaxima = 0L;
            UtilizatorDTO ofertant = null;
            if (licitatieActiva.getOfertaMaxima() != null && licitatieActiva.getOfertaMaxima().getValoare() != null) {
                valoareOfertaMaxima = licitatieActiva.getOfertaMaxima().getValoare();
                ofertant = licitatieActiva.getOfertaMaxima().getOfertant();
            }
            if (LicitatieConstants.STATUS_LICITATIE_INCHIS.equals(licitatieActiva.getStatus())) {
                System.out.println("Licitatia s-a incheiat.");
                if (ofertant != null) {
                    System.out.println("Licitatia a fost castigata de " + ofertant.getId() + "-" + ofertant.getNume()
                            + " cu o oferta de: " + valoareOfertaMaxima);
                    System.out.println("Apasati 0 pentru a iesi.");
                }
            } else if (LicitatieConstants.STATUS_LICITATIE_OPRIT.equals(licitatieActiva.getStatus())) {
                System.out.println("Licitatia a fost oprita de vanzator! Apasati 0 pentru a iesi.");
            } else {
                Date now = new Date();
                if (now.getTime() < licitatieActiva.getTimpInchidere().getTime()) {
                    Interval interval = new Interval(now.getTime(), licitatieActiva.getTimpInchidere().getTime());
                    Duration duration = interval.toDuration();
                    System.out.println("Licitatia se inchide in: " + duration.getStandardDays() + " zile "
                            + duration.getStandardHours() % 24 + " ore " + duration.getStandardMinutes() % 60
                            + " minute.");
                    System.out.println("Oferta maxima pentru produsul: " + licitatieActiva.getProdus().getDescriere()
                            + " este: " + valoareOfertaMaxima);
                    System.out.println("Rata de crestere este: " + licitatieActiva.getValoareCrestere());
                    System.out.println("Valoarea de start este: " + licitatieActiva.getValoareStart());
                    if (ofertant != null) {
                        System.out.println("Oferta maxima apartine utilizatorului: " + ofertant.getId() + "-"
                                + ofertant.getNume());
                    }
                    if (showPlaseazaOfertaMenu) {
                        printMenuPlaseazaOferta();
                    } else {
                        System.out.println("Apasati 0 pentru a intrerupe urmarirea ofertei");
                    }
                }
            }
        }
    }

    private static void printMenuPlaseazaOferta() {
        System.out.println("\n\nCe doriti sa faceti ?");
        System.out.println("1. Plaseaza o oferta manual.");
        System.out.println("2. Start/Stop oferte automate ");
        System.out.println("0. Inapoi");
        System.out.print("Optiune: ");
    }

}
