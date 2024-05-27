package twisk.simulation;

import twisk.monde.Etape;
import twisk.monde.Guichet;
import twisk.monde.Monde;
import twisk.mondeIG.SujetObserve;
import twisk.outils.KitC;

import java.util.Iterator;

public class Simulation extends SujetObserve implements Iterable<Client>  {
    public Monde monde;
    private final KitC kitC;
    private int nbClients;
    private final GestionnaireClients gestClients;

    public Simulation() {
        monde = new Monde();
        kitC = new KitC();
        kitC.creerEnvironnement();
        nbClients = 1;
        gestClients = new GestionnaireClients();
    }

    public void simuler(Monde monde) {
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

        gestClients.setClients(resultat);

        notifierObservateurs();


        int[] where_clients;
        do {
            where_clients = ou_sont_les_clients(monde.nbEtapes(), nbClients);
            Etape etape = monde.getSasEntree();
            System.out.println();
            for(int i=1; i<=(nbClients+1)*monde.nbEtapes(); i=i+nbClients+1) {
                if(i / (nbClients + 1) != 1) {
                    gestClients.allerA(where_clients[i], etape, i / (nbClients + 1));
                    int padding = 0;
                    if(monde.getName(i / (nbClients + 1)).length() < 20) {
                        padding = 20 - monde.getName(i / (nbClients + 1)).length();
                    }
                    System.out.print("étape " + i / (nbClients + 1) + " (" + monde.getName(i / (nbClients + 1)) + ") : " + " ".repeat(padding) + where_clients[i - 1] + " clients : ");
                    for (int j = i; j < i + where_clients[i - 1]; j++) {
                        System.out.print(where_clients[j] + " ");
                    }
                    if (i < monde.nbEtapes() - 1) {
                        etape = etape.getSuccesseur();
                    }
                    System.out.println();
                }
            }
            int padding = 0;
            if(monde.getName((nbClients + 2) / (nbClients + 1)).length() < 20) {
                padding = 20 - monde.getName((nbClients + 2) / (nbClients + 1)).length();
            }
            System.out.print("étape " + (nbClients + 2) / (nbClients + 1)  + " (" + monde.getName((nbClients + 2) / (nbClients + 1)) + ") : " + " ".repeat(padding) + where_clients[(nbClients + 2) - 1] + " clients : ");
            for (int j = (nbClients + 2); j < (nbClients + 2) + where_clients[(nbClients + 2) - 1]; j++) {
                System.out.print(where_clients[j] + " ");
            }
            System.out.println();

            notifierObservateurs();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } while(where_clients[(nbClients+1)] != nbClients);
        nettoyage();
        notifierObservateurs();
    }

    public void setNbClients(int clients) {
        this.nbClients = clients;
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

