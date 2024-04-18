package twisk.simulation;

import twisk.monde.Etape;
import java.util.ArrayList;
import java.util.Iterator;

public class GestionnaireClients implements Iterable<Client> {

    private final ArrayList<Client> clients;

    public GestionnaireClients() {
        clients = new ArrayList<Client>();
    }

    public void setClients(int[] tabClients) {
        for (int num : tabClients) {
            clients.add(new Client(num));
        }
    }

    public void allerA(int numeroClient, Etape etape, int rang) {
        for(Client c : clients) {
            if (c.getNumeroClient() == numeroClient) {
                c.allerA(etape, rang);
            }
        }
    }

    public void nettoyer() {
        clients.clear();
    }

    public Iterator<Client> iterator() {
        return this.clients.iterator();
    }

    public int getNbClients() {
        return this.clients.size();
    }
}
