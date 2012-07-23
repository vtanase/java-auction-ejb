import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.vlad.dto.LicitatieDTO;
import com.vlad.dto.OfertaDTO;
import com.vlad.dto.ProdusDTO;
import com.vlad.dto.UtilizatorDTO;
import com.vlad.ejb.ManagerLicitatie;
import com.vlad.util.LicitatieConstants;
import com.vlad.validation.ValidationError;

public class Client {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Client is working");
        ManagerLicitatie bean = managerLicitatieBeanLookup("java:global/final-main/final-ejb/ManagerLicitatieImpl!com.vlad.ejb.ManagerLicitatie");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti un nume de utilizator: ");
        String userName = scanner.nextLine();
        UtilizatorDTO utilizator = new UtilizatorDTO();
        utilizator.setNume(userName);
        utilizator = bean.populeazaUtilizator(utilizator);
        System.out.println("Bun venit " + utilizator.getNume()
                + "! Id-ul tau de utilizator este: " + utilizator.getId());
        printMenu();
        int option = scanner.nextInt();
        while (option != 0) {
            switch (option) {
            case 1:
                adaugaLicitatie(bean, utilizator);
                break;
            case 2:
                listeazaLicitatiileMele(bean, utilizator);
                break;
            case 3:
                listeazaLicitatiileActive(bean, utilizator);
                break;
            case 4:
                listeazaVanzatorii(bean);
                break;
            case 0:
                break;
            default:
                break;
            }
            printMenu();
            option = scanner.nextInt();
        }

    }
    
    private static void plaseazaOferta(ManagerLicitatie bean, UtilizatorDTO utilizator, LicitatieDTO licitatie) {
        Scanner scanner = new Scanner(System.in);
        List<LicitatieDTO> licitatii = bean.listeazaToateLicitatiileDeschise();
        System.out.println("Valoarea ofertei: ");
        Long valoare = scanner.nextLong();
        OfertaDTO oferta = new OfertaDTO();
        oferta.setValoare(valoare);
        oferta.setData(new Date());
        oferta.setOfertant(utilizator);
        oferta.setLicitatie(licitatie);
        bean.plaseazaOferta(oferta);
    }
    
    private static void listeazaVanzatorii(ManagerLicitatie bean) {
        System.out.println("\n\nVanzatorii sunt: ");
        List<UtilizatorDTO> vanzatori = bean.listeazaVanzatorii();
        if (vanzatori == null) {
            System.out.println("\n\nNu exista vanzatori in momentul de fata.");
        } else {
            for (UtilizatorDTO vanzator : vanzatori) {
                System.out.println("\nVanzator:");
                System.out.println("-----------");
                System.out.println("id = " + vanzator.getId());
                System.out.println("nume = " + vanzator.getNume());
            }
        }
    }
    
    private static void listeazaLicitatiileActive(ManagerLicitatie bean, UtilizatorDTO utilizator) {
        System.out.println("\n\nLicitatiil active: ");
        List<LicitatieDTO> licitatii = bean.listeazaToateLicitatiileDeschise();
        if (licitatii == null || licitatii.size() == 0) {
            System.out.println("\n\nNu exista licitatii active momentan.");
        } else {
            for (LicitatieDTO licitatie : licitatii) {
                System.out.println(licitatie.toString());
            }
            printLicitatiiActiveMenu();
            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            while(option != 0) {
                // we only have 1 option no point to use a switch
                System.out.print("Numarul licitatiei pe care doriti sa o urmariti: ");
                Long idLicitatie = scanner.nextLong();
                boolean isValidId = false;
                for (LicitatieDTO licitatie : licitatii) {
                    if (licitatie.getId().equals(idLicitatie)) {
                        isValidId = true;
                    }
                }
                if (!isValidId) {
                    System.out.println("Id-ul introdus nu este corect!");
                } else {
                    LicitatieDTO licitatie = bean.getLicitatieCuOfertaMaxima(idLicitatie);
                    Runnable urmaresteLicitatieTask = new UrmaresteLicitatieTask(bean, utilizator, idLicitatie, true);
                    ScheduledFuture<?> urmaresteLicitatieHandler = scheduler.scheduleAtFixedRate(
                            urmaresteLicitatieTask, 0, 5, TimeUnit.SECONDS);
                    ScheduledFuture<?> plaseazaOfertaHandler = null;
                    boolean oferteAutomate = false;
                    int optionPlaseaza = scanner.nextInt();
                    while (optionPlaseaza != 0) {
                        urmaresteLicitatieHandler.cancel(true);
                        switch (optionPlaseaza) {
                        case 1:
                            plaseazaOferta(bean, utilizator, licitatie);
                            break;
                        case 2:
                            if (!oferteAutomate) {
                                PlaseazaOfertaTask task = new PlaseazaOfertaTask(bean, utilizator, licitatie);
                                plaseazaOfertaHandler = scheduler.scheduleAtFixedRate(task, 0, 30, TimeUnit.SECONDS);
                                oferteAutomate = true;
                                System.out.println("Am pornit plasarea automata de oferte");
                            } else {
                                plaseazaOfertaHandler.cancel(true);
                                oferteAutomate = false;
                                System.out.println("Am oprit plasarea automata de oferte.");
                            }
                            break;
                        default:
                            break;
                        }
                        urmaresteLicitatieHandler = scheduler.scheduleAtFixedRate(urmaresteLicitatieTask, 0, 5,
                                TimeUnit.SECONDS);
                        optionPlaseaza = scanner.nextInt();
                    }
                    urmaresteLicitatieHandler.cancel(true);
                    plaseazaOfertaHandler.cancel(true);
                }
                printLicitatiiActiveMenu();
                option = scanner.nextInt();
            }
        }
    }
    
    private static void listeazaLicitatiileMele(ManagerLicitatie bean, UtilizatorDTO utilizator) {
        System.out.println("\n\nLicitatiile utilizatorului: " + utilizator.getNume() + " cu id-ul: " + utilizator.getId());
        List<LicitatieDTO> licitatii = bean.listeazaLicitatiiPentruVanzator(utilizator);
        if (licitatii == null || licitatii.size() == 0) {
            System.out.println("\nNu ati introdus nici o licitatie.");
            System.out.println("Folositi meniul pentru a adauga o noua licitatie.");
        } else {
            for (LicitatieDTO licitatie : licitatii) {
                System.out.println(licitatie.toString());
            }
            printLicitatiileMeleMenu();
            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            while (option != 0) {
                switch (option) {
                case 1:
                    urmaresteLicitatieCaVanzator(bean, licitatii, utilizator);
                    break;
                case 2:
                    inchideLicitatie(bean, licitatii);
                    break;
                default: break;
                }
                printLicitatiileMeleMenu();
                option = scanner.nextInt();
            }
        }
    }
    
    private static void urmaresteLicitatieCaVanzator(ManagerLicitatie bean, List<LicitatieDTO> licitatiileMele, UtilizatorDTO utilizator) {
        System.out.println("\nNumarul licitatiei pe care doriti sa o urmariti: ");
        Scanner scanner = new Scanner(System.in);
        Long idLicitatie = scanner.nextLong(); 
        boolean isValidId = false;
        for (LicitatieDTO licitatie : licitatiileMele) {
            if (licitatie.getId().equals(idLicitatie)) {
                isValidId = true;
            }
        }
        if (isValidId) {
            Runnable urmaresteLicitatieTask = new UrmaresteLicitatieTask(bean, utilizator, idLicitatie, false);
            ScheduledFuture<?> urmaresteLicitatieHandler = scheduler.scheduleAtFixedRate(
                    urmaresteLicitatieTask, 0, 5, TimeUnit.SECONDS);
            int optionPlaseaza = scanner.nextInt();
            if (optionPlaseaza == 0) {
                urmaresteLicitatieHandler.cancel(true);
            }
        } else {
            System.out.println("Nu puteti urmari licitatia cu id-ul respectiv.");
        }
    }
    
    private static void inchideLicitatie(ManagerLicitatie bean, List<LicitatieDTO> licitatiileMele) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n\nId-ul licitatiei pe care doriti sa o inchideti: ");
        Long idLicitatie = scanner.nextLong();
        boolean isValidId = false;
        for (LicitatieDTO licitatie : licitatiileMele) {
            if (licitatie.getId().equals(idLicitatie)) {
                isValidId = true;
            }
        }
        if (isValidId) {
            bean.modificaStatusLicitatie(idLicitatie, LicitatieConstants.STATUS_LICITATIE_OPRIT);
            System.out.println("Licitatia cu id-ul " + idLicitatie + " a fost oprita");
        } else {
            System.out.println("Id-ul introdus nu este corect!");
        }
    }

    private static void adaugaLicitatie(ManagerLicitatie bean,
            UtilizatorDTO utilizator) {
        ProdusDTO produs = citesteProdus();
        System.out.println("\n\nAdaugare licitatie:");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Valoarea de start:");
        Long valoareStart = scanner.nextLong();
        System.out.print("Rata de crestere:");
        Long rataCrestere = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Timp deschidere(zz/ll/aaaa HH:mm): ");
        String timpDeschidere = scanner.nextLine();
        System.out.print("Timp inchidere(zz/ll/aaaa HH:mm): ");
        String timpInchidere = scanner.nextLine();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date timpDeschidereData = null;
        Date timpInchidereData = null;
        try {
            timpDeschidereData = df.parse(timpDeschidere);
            timpInchidereData = df.parse(timpInchidere);
            produs = bean.adaugareProdus(produs);
            System.out.println("Am adaugat produsul cu id-ul: " + produs.getId());
            LicitatieDTO licitatie = new LicitatieDTO();
            licitatie.setId(produs.getId());
            licitatie.setValoareStart(valoareStart);
            licitatie.setValoareCrestere(rataCrestere);
            licitatie.setTimpDeschidere(timpDeschidereData);
            licitatie.setTimpInchidere(timpInchidereData);
            licitatie.setVanzator(utilizator);
            licitatie = bean.adaugareLicitatie(licitatie);
            if (licitatie.getValidationErrors() == null || licitatie.getValidationErrors().size() == 0) {
                System.out.println("S-a adaugat licitatia cu id-ul: " + licitatie.getId());
                System.out.println("O puteti gasi in lista dvs. de licitatii.");
            } else {
                System.out.println("Licitatia nu a putut fi creata, am intalnit urmatoarele probleme: ");
                for (ValidationError error : licitatie.getValidationErrors()) {
                    System.out.println(error.getErrorMessage());
                }
            }
        } catch (ParseException e) {
            System.out.println("Datele de inceput/sfarsit nu sunt valide, reintroduceti licitatia");
        }
    }

    private static ProdusDTO citesteProdus() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nAdaugare produs: ");
        System.out.print("Descriere produs: ");
        String descriere = scanner.nextLine();
        System.out.print("Locatie imagine: ");
        String imagine = scanner.nextLine();
        System.out.print("Produsul este o carte?(d/n)");
        String isCarte = scanner.nextLine();
        ProdusDTO produs = new ProdusDTO();
        if (isCarte != null && isCarte.toLowerCase().equals("d")) {
            // avem o carte, citim campurile suplimentare
            System.out.print("Titlul cartii: ");
            String titlu = scanner.nextLine();
            System.out.print("Autorul cartii: ");
            String autor = scanner.nextLine();

            produs.setDescriere(descriere);
            produs.setImagine(imagine);
            produs.setCarte(true);
            produs.setTitlu(titlu);
            produs.setAutor(autor);
        } else {
            // nu e carte
            produs.setDescriere(descriere);
            produs.setImagine(imagine);
            produs.setCarte(false);
        }
        return produs;
    }

    private static void printMenu() {
        System.out.println("\n\nAlege o optiune: ");
        System.out.println("1. Adauga licitatie");
        System.out.println("2. Licitatiile mele");
        System.out.println("3. Licitatiile active");
        System.out.println("4. Listeaza vanzatorii");
        System.out.println("0. Iesire");
        System.out.print("Optiune: ");
    }
    
    private static void printLicitatiiActiveMenu() {
        System.out.println("\n\nAlege o optiune: ");
        System.out.println("1. Urmareste licitatia");
        System.out.println("0. Iesire");
        System.out.print("Optiune: ");
    }
    
    private static void printLicitatiileMeleMenu() {
        System.out.println("\n\nAlege o optiune: ");
        System.out.println("1. Urmareste licitatia");
        System.out.println("2. Inchide o licitatie");
        System.out.println("0. Inapoi");
        System.out.print("Optiune: ");
    }
    

    private static ManagerLicitatie managerLicitatieBeanLookup(String name) {
        ManagerLicitatie bean = null;
        try {
            InitialContext ctx = new InitialContext();
            Object obj = ctx.lookup(name);
            bean = (ManagerLicitatie) obj;
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return bean;
    }

}
