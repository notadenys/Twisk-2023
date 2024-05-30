package twisk.vues;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import twisk.simulation.Client;

public class VueClient extends Circle {
    private final Client client;


    public VueClient(Client client) {
        super(5);
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

    public Client getClient() { return client; }
    public int getRang() {  return client.getRang(); }
}
