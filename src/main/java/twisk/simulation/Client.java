package twisk.simulation;

import twisk.monde.Etape;

import java.util.Random;

/**
 * Represents a client in the simulation.
 */
public class Client {

    private final int numeroClient;
    private int rang;
    private Etape etape;
    private final int color;

    /**
     * Constructs a new Client with the given number.
     *
     * @param numero The client number.
     */
    public Client(int numero) {
        this.numeroClient = numero;
        this.rang = 0;
        Random random = new Random();
        color = random.nextInt(5);
    }

    /**
     * Moves the client to the specified stage at the given rank.
     *
     * @param etape The stage to move to.
     * @param rang  The rank of the client.
     */
    public void allerA(Etape etape, int rang) {
        this.rang = rang;
        this.etape = etape;
    }

    /**
     * Gets the client number.
     *
     * @return The client number.
     */
    public int getNumeroClient() {
        return numeroClient;
    }
    public int getColor() { return color; }

    /**
     * Gets the current stage of the client.
     *
     * @return The current stage.
     */
    public Etape getEtapeActuelle() {
        return this.etape;
    }

    /**
     * Gets the rank of the client.
     *
     * @return The rank of the client.
     */
    public int getRang() {
        return this.rang;
    }
}
