package twisk.vues;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import twisk.simulation.Client;

/**
 * VueClient is a class representing the view of a client in the simulation.
 * It extends Circle and manages the graphical representation of a client.
 */
public class VueClient extends Circle {
    private final Client client;

    /**
     * Constructor of VueClient.
     *
     * @param client the client to be represented.
     */
    public VueClient(Client client) {
        super(7);
        this.client = client;
        switch (client.getColor()) {
            case 0:
                this.setFill(Color.RED);
                break;
            case 1:
                this.setFill(Color.CYAN);
                break;
            case 2:
                this.setFill(Color.BLUE);
                break;
            case 3:
                this.setFill(Color.MAGENTA);
                break;
            case 4:
                this.setFill(Color.ORANGE);
                break;
        }
        this.setVisible(true);
    }

    /**
     * Gets the client represented by this view.
     *
     * @return the client.
     */
    public Client getClient() { return client; }

    /**
     * Gets the rank of the client.
     *
     * @return the rank of the client.
     */
    public int getRang() { return client.getRang(); }
}
