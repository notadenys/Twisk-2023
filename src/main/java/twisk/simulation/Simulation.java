package twisk.simulation;

import twisk.monde.Etape;
import twisk.monde.Guichet;
import twisk.monde.Monde;
import twisk.mondeIG.SujetObserve;
import twisk.outils.KitC;
import twisk.outils.MutableBoolean;

import java.util.Iterator;

/**
 * The Simulation class manages the simulation of clients moving through the stages of the world.
 * It interfaces with native code for simulation execution.
 */
public class Simulation extends SujetObserve implements Iterable<Client> {
    private final KitC kitC;
    private int nbClients;
    private final GestionnaireClients gestClients;
    private final MutableBoolean inProgress;

    /**
     * Constructs a Simulation instance and initializes necessary components.
     */
    public Simulation() {
        kitC = new KitC();
        kitC.creerEnvironnement();
        nbClients = 1;
        gestClients = new GestionnaireClients();
        inProgress = new MutableBoolean();
    }

    /**
     * Starts the simulation process for the given world.
     *
     * @param monde The world to simulate.
     */
    public void simuler(Monde monde) {
        inProgress.setValue(true);
        kitC.creerFichier(monde.toC());
        kitC.compiler();
        kitC.construireLaBibliotheque();
        System.load("/tmp/twisk/libTwisk.so");

        int[] tabJetonsGuichet = creationTabJeton(monde);
        int[] resultat = start_simulation(monde.nbEtapes(), monde.nbGuichets(), nbClients, tabJetonsGuichet);

        gestClients.setClients(resultat);

        notifierObservateurs();
        int[] where_clients;
        do {
            try {
                where_clients = ou_sont_les_clients(monde.nbEtapes(), nbClients);

                // Process all stages except SasSortie
                for (Etape etape : monde) {
                    if (!etape.estUneSortie()) {
                        for (int j = 1; j < where_clients[etape.getNum() * (nbClients + 1)] + 1; j++) {
                            gestClients.allerA(where_clients[j + etape.getNum() * (nbClients + 1)], etape, j);
                        }
                    }
                }

                // Process SasSortie
                Etape sasSortie = monde.getSortie();
                for (int j = 1; j < where_clients[sasSortie.getNum() * (nbClients + 1)] + 1; j++) {
                    gestClients.allerA(where_clients[j + sasSortie.getNum() * (nbClients + 1)], sasSortie, j);
                }

                notifierObservateurs();

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                stopSimulation();
                nettoyage();
                notifierObservateurs();
                throw new RuntimeException(e);
            }
        } while (where_clients[nbClients + 1] != nbClients && inProgress.getValue());
        nettoyage();
        inProgress.setValue(false);
        notifierObservateurs();
    }

    /**
     * Sets the number of clients for the simulation.
     *
     * @param clients The number of clients.
     */
    public void setNbClients(int clients) {
        this.nbClients = clients;
    }

    /**
     * Stops the simulation.
     */
    public void stopSimulation() {
        kitC.killSimulation(gestClients);
        inProgress.setValue(false);
        notifierObservateurs();
    }

    /**
     * Gets the simulation progress status.
     *
     * @return The MutableBoolean representing whether the simulation is in progress.
     */
    public MutableBoolean getInProgress() {
        return inProgress;
    }

    /**
     * Creates an array of token counts for each stage in the world.
     *
     * @param monde The world to create the token array for.
     * @return The array of token counts.
     */
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

    /**
     * Gets the client manager for the simulation.
     *
     * @return The GestionnaireClients instance.
     */
    public GestionnaireClients getGestionnaireClients() {
        return gestClients;
    }

    /**
     * Native method to start the simulation.
     *
     * @param nbEtapes        The number of stages.
     * @param nbGuichets      The number of counters.
     * @param nbClients       The number of clients.
     * @param tabJetonsGuichets The token array for counters.
     * @return The initial client positions.
     */
    public native int[] start_simulation(int nbEtapes, int nbGuichets, int nbClients, int[] tabJetonsGuichets);

    /**
     * Native method to determine the positions of the clients.
     *
     * @param nbEtapes The number of stages.
     * @param nbClients The number of clients.
     * @return The array of client positions.
     */
    public native int[] ou_sont_les_clients(int nbEtapes, int nbClients);

    /**
     * Native method for cleanup after simulation.
     */
    public native void nettoyage();

    @Override
    public Iterator<Client> iterator() {
        return gestClients.iterator();
    }
}
