package twisk.simulation;

import twisk.monde.Monde;
import twisk.outils.KitC;

public class Simulation {
    public Monde monde;
    private final KitC kitC;
    private int nbClients;

    public Simulation() {
        monde = new Monde();
        kitC = new KitC();
        kitC.creerEnvironnement();
    }

    public void simuler(Monde  monde) {
        System.out.println(monde);
        System.out.println(monde.toC());
        kitC.creerFichier(monde.toC());
        kitC.compiler();
        kitC.construireLaBibliotheque();
        System.load("/tmp/twisk/libTwisk.so") ;

        int[] tabJetonsGuichet = new int[monde.nbGuichets()];
        for(int i=0;i<monde.nbGuichets();i++)
        {
            tabJetonsGuichet[i] = i+3;
        }
        setNbClients(6);
        int[] resultat = start_simulation(monde.nbEtapes(), monde.nbGuichets(), nbClients, tabJetonsGuichet);
        System.out.print("les clients : ");
        for(int i=0; i<nbClients; i++)
        {
            System.out.print(resultat[i]+" ");
        }
        System.out.println();

        int[] where_clients;
        String sortie;
        do {
            where_clients = ou_sont_les_clients(monde.nbEtapes(), nbClients);
            System.out.println();
            for(int i=1; i<=(nbClients+1)*monde.nbEtapes(); i=i+nbClients+1) {
                if(i / (nbClients + 1) != 1) {
                    System.out.print("étape " + i / (nbClients + 1) + " : " + where_clients[i - 1] + " clients : ");
                    for (int j = i; j < i + where_clients[i - 1]; j++) {
                        System.out.print(where_clients[j] + " ");
                    }
                    System.out.println();
                }
            }
            System.out.print("étape " + 8 / (nbClients + 1) + " : " + where_clients[8 - 1] + " clients : ");
            for (int j = 8; j < 8 + where_clients[8 - 1]; j++) {
                System.out.print(where_clients[j] + " ");
            }
            System.out.println();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while(where_clients[(nbClients+1)] != nbClients);
        nettoyage();
    }

    public void setNbClients(int clients) {
        this.nbClients = clients;
    }

    public native int[] start_simulation(int nbEtapes, int nbGuichets, int nbClients, int[] tabJetonsGuichets);
    public native int[] ou_sont_les_clients(int nbEtapes, int nbClients);
    public native void nettoyage();

}
