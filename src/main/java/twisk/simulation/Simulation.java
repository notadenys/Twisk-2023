package twisk.simulation;

import twisk.monde.Etape;
import twisk.monde.Guichet;
import twisk.monde.Monde;
import twisk.mondeIG.SujetObserve;
import twisk.outils.KitC;

import java.util.Arrays;
import java.util.Iterator;

public class Simulation extends SujetObserve implements Iterable<Client>  {
    public Monde monde;
    private final KitC kitC;
    private int nbClients;
    private final GestionnaireClients gestClients;
    private volatile boolean running;

    public Simulation() {
        monde = new Monde();
        kitC = new KitC();
        kitC.creerEnvironnement();
        nbClients = 1;
        gestClients = new GestionnaireClients();
    }

    public void simuler(Monde monde) {
        running = true;
        System.out.println(monde);
        System.out.println(monde.toC());
        kitC.creerFichier(monde.toC());
        kitC.compiler();
        kitC.construireLaBibliotheque();
        System.load("/tmp/twisk/libTwisk.so") ;

        int[] tabJetonsGuichet = creationTabJeton(monde);
        int[] resultat = start_simulation(monde.nbEtapes(), monde.nbGuichets(), nbClients, tabJetonsGuichet);
        System.out.print("les clients : ");
        for(int i=0; i<nbClients; i++) {
            System.out.print(resultat[i]+" ");
        }
        System.out.println();

        gestClients.setClients(resultat); // clients are in gest with null as Etape

        notifierObservateurs();

        int[] where_clients;
        do {
            where_clients = ou_sont_les_clients(monde.nbEtapes(), nbClients);
            Etape etape = monde.getSasEntree();
            System.out.println("###################################");
            for (int i = 0; i < monde.nbEtapes(); i++) {

                System.out.println("etape " + etape.getNum() + " (" + etape.getNom() + ") : " + where_clients[etape.getNum() * (nbClients + 1)] + "  clients : ");

                for (int j = 1; j < where_clients[etape.getNum() * (nbClients + 1)] + 1; j++) {
                    gestClients.allerA(where_clients[j + etape.getNum() * (nbClients + 1)], etape, j);
                    System.out.print(where_clients[j + etape.getNum() * (nbClients + 1)] + " ");
                }
               System.out.println();

                if (i < monde.nbEtapes() - 1) {
                    etape = etape.getSuccesseur();
                }
            }
            notifierObservateurs();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } while(where_clients[(nbClients+1)] != nbClients && running);
        nettoyage();
        notifierObservateurs();
    }

    public void setNbClients(int clients) {
        this.nbClients = clients;
    }

    public void stopSimulation() {
        kitC.stopAllProcesses(this);
        running = false;
    }

    public void startSimulation() {
        running = true;
    }

    private int[] creationTabJeton(Monde monde) {
        int[] tab = new int[monde.nbEtapes()];
        Iterator<Etape> iterator = monde.iterator() ;
        int i = 0 ;

        while (iterator.hasNext()){
            Etape e = iterator.next();
            if (e.estUnGuichet()){
                Guichet g = (Guichet) e ;
                tab[i++] = g.getNbJetons() ;
            }
        }
        return tab ;
    }

    public GestionnaireClients getGestionnaireClients() {return gestClients;}

    public native int[] start_simulation(int nbEtapes, int nbGuichets, int nbClients, int[] tabJetonsGuichets);
    public native int[] ou_sont_les_clients(int nbEtapes, int nbClients);
    public native void nettoyage();
    @Override
    public Iterator<Client> iterator() {
        return gestClients.iterator();
    }

    public GestionnaireClients getGestClients() {
        if(gestClients == null) {
            System.out.println("GestClient IS empty");
        } else {
            System.out.println("GestClient is NOT empty");
        }
        return gestClients;
    }
}

