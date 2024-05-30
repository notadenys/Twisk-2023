package twisk.simulation;

import twisk.monde.Etape;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Manages a collection of clients in the simulation.
 */
public class GestionnaireClients implements Iterable<Client> {

    private final ArrayList<Client> clients;

    /**
     * Constructs a new GestionnaireClients.
     */
    public GestionnaireClients() {
        clients = new ArrayList<>();
    }

    /**
     * Sets the clients based on the given array of client numbers.
     *
     * @param tabClients The array of client numbers.
     */
    public void setClients(int[] tabClients) {
        for (int num : tabClients) {
            clients.add(new Client(num));
        }
    }

    /**
     * Moves a client to the specified stage at the given rank.
     *
     * @param numeroClient The number of the client to move.
     * @param etape        The stage to move the client to.
     * @param rang         The rank of the client.
     */
    public void allerA(int numeroClient, Etape etape, int rang) {
        for(Client c : clients) {
            if (c.getNumeroClient() == numeroClient) {
                c.allerA(etape, rang);
            }
        }
    }

    /**
     * Clears all clients from the gestionnaire.
     */
    public void nettoyer() {
        clients.clear();
    }

    /**
     * Returns an iterator over the clients in this gestionnaire.
     *
     * @return An iterator over the clients.
     */
    @Override
    public Iterator<Client> iterator() {
        return this.clients.iterator();
    }

    /**
     * Gets the number of clients in the gestionnaire.
     *
     * @return The number of clients.
     */
    public int getNbClients() {
        return this.clients.size();
    }
}
