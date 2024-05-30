package twisk.simulation;

import twisk.monde.Etape;
import twisk.monde.Guichet;
import twisk.monde.Monde;
import twisk.mondeIG.SujetObserve;
import twisk.outils.KitC;
import twisk.outils.MutableBoolean;

import java.util.Iterator;

public class Simulation extends SujetObserve implements Iterable<Client> {
    private final KitC kitC;
    private int nbClients;
    private final GestionnaireClients gestClients;
    private final MutableBoolean inProgress;

    public Simulation() {
        kitC = new KitC();
        kitC.creerEnvironnement();
        nbClients = 1;
        gestClients = new GestionnaireClients();
        inProgress = new MutableBoolean();
    }

    public void simuler(Monde monde) {
        inProgress.setValue(true);
        System.out.println(monde);
        System.out.println(monde.toC());
        kitC.creerFichier(monde.toC());
        kitC.compiler();
        kitC.construireLaBibliotheque();
        System.load("/tmp/twisk/libTwisk.so");

        int[] tabJetonsGuichet = creationTabJeton(monde);
        int[] resultat = start_simulation(monde.nbEtapes(), monde.nbGuichets(), nbClients, tabJetonsGuichet);
        System.out.print("les clients : ");
        for (int i = 0; i < nbClients; i++) {
            System.out.print(resultat[i] + " ");
        }
        System.out.println();

        gestClients.setClients(resultat);

        notifierObservateurs();
        int[] where_clients;
        do {
            try {
                where_clients = ou_sont_les_clients(monde.nbEtapes(), nbClients);
                System.out.println("###################################");

                // Process all stages except SasSortie
                for (Etape etape : monde) {
                    if (!etape.estUneSortie()) {
                        System.out.println("etape " + etape.getNum() + " (" + etape.getNom() + ") : " +
                                where_clients[etape.getNum() * (nbClients + 1)] + " clients : ");

                        for (int j = 1; j < where_clients[etape.getNum() * (nbClients + 1)] + 1; j++) {
                            gestClients.allerA(where_clients[j + etape.getNum() * (nbClients + 1)], etape, j);
                            System.out.print(where_clients[j + etape.getNum() * (nbClients + 1)] + " ");
                        }
                        System.out.println();
                    }
                }

                // Process SasSortie
                Etape sasSortie = monde.getSortie();
                System.out.println("etape " + sasSortie.getNum() + " (" + sasSortie.getNom() + ") : " +
                        where_clients[sasSortie.getNum() * (nbClients + 1)] + " clients : ");
                for (int j = 1; j < where_clients[sasSortie.getNum() * (nbClients + 1)] + 1; j++) {
                    gestClients.allerA(where_clients[j + sasSortie.getNum() * (nbClients + 1)], sasSortie, j);
                    System.out.print(where_clients[j + sasSortie.getNum() * (nbClients + 1)] + " ");
                }
                System.out.println();

                notifierObservateurs();

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("INTERRUPTED");
                stopSimulation();
                nettoyage();
                notifierObservateurs();
                throw new RuntimeException(e);
            }
        } while (where_clients[nbClients + 1] != nbClients && inProgress.getValue());
        nettoyage();
        inProgress.setValue(false);
        System.out.println("STOP");
        notifierObservateurs();
    }

    public void setNbClients(int clients) {
        this.nbClients = clients;
    }

    public void stopSimulation() {
        kitC.killSimulation(gestClients);
        inProgress.setValue(false);
        notifierObservateurs();
    }

    public MutableBoolean getInProgress() { return inProgress; }

    private int[] creationTabJeton(Monde monde) {
        int[] tab = new int[monde.nbEtapes()];
        Iterator<Etape> iterator = monde.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            Etape e = iterator.next();
            if (e.estUnGuichet()) {
                Guichet g = (Guichet) e;
                tab[i++] = g.getNbJetons();
            }
        }
        return tab;
    }

    public GestionnaireClients getGestionnaireClients() {
        return gestClients;
    }

    public native int[] start_simulation(int nbEtapes, int nbGuichets, int nbClients, int[] tabJetonsGuichets);
    public native int[] ou_sont_les_clients(int nbEtapes, int nbClients);
    public native void nettoyage();

    @Override
    public Iterator<Client> iterator() {
        return gestClients.iterator();
    }
}