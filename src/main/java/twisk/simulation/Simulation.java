package twisk.simulation;

import twisk.monde.Etape;
import twisk.monde.Guichet;
import twisk.monde.Monde;
import twisk.outils.KitC;

import java.util.Iterator;

public class Simulation {
    public Monde monde;
    private final KitC kitC;
    private int nbClients;

    public Simulation() {
        monde = new Monde();
        kitC = new KitC();
        kitC.creerEnvironnement();
        nbClients = 1;
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
        for(int i=0; i<nbClients; i++)
        {
            System.out.print(resultat[i]+" ");
        }
        System.out.println();

        int[] where_clients;
        do {
            where_clients = ou_sont_les_clients(monde.nbEtapes(), nbClients);
            System.out.println();
            for(int i=1; i<=(nbClients+1)*monde.nbEtapes(); i=i+nbClients+1) {
                if(i / (nbClients + 1) != 1) {
                    int padding = 0;
                    if(monde.getName(i / (nbClients + 1)).length() < 20) { // without this, if name longer than 20, code falls
                        padding = 20 - monde.getName(i / (nbClients + 1)).length();
                    }
                    System.out.print("étape " + i / (nbClients + 1) + " (" + monde.getName(i / (nbClients + 1)) + ") : " + " ".repeat(padding) + where_clients[i - 1] + " clients : ");
                    for (int j = i; j < i + where_clients[i - 1]; j++) {
                        System.out.print(where_clients[j] + " ");
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

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while(where_clients[(nbClients+1)] != nbClients);
        nettoyage();
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

    public native int[] start_simulation(int nbEtapes, int nbGuichets, int nbClients, int[] tabJetonsGuichets);
    public native int[] ou_sont_les_clients(int nbEtapes, int nbClients);
    public native void nettoyage();

}

/*
    Adding another queue to the simulation introduces a looping problem that we have not been able to identify.
    If you select the comment on line 19 of this file, the C code will be output to the terminal,
    which will show exactly where the problem is.
    As a result, in the presence of at least two queues, the client jumps through one stage,
    due to which at the next transition it does not find itself in the desired one, stopping the simulation.
 */
