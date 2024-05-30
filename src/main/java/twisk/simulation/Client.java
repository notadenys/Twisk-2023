package twisk.simulation;

import twisk.monde.Etape;

/**
 * Represents a client in the simulation.
 */
public class Client {

    private final int numeroClient;
    private int rang;
    private Etape etape;
    private boolean isUpdated;
    private int xBefore;
    private int yBefore;

    /**
     * Constructs a new Client with the given number.
     *
     * @param numero The client number.
     */
    public Client(int numero) {
        this.numeroClient = numero;
        this.rang = 0;
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

    /**
     * Checks if the client has been updated.
     *
     * @return True if the client has been updated, false otherwise.
     */
    public boolean isUpdated() {
        return isUpdated;
    }

    /**
     * Sets the updated status of the client.
     *
     * @param updated The updated status to set.
     */
    public void setUpdated(boolean updated) {
        this.isUpdated = updated;
    }

    /**
     * Gets the previous X coordinate of the client.
     *
     * @return The previous X coordinate.
     */
    public int getXBefore() {
        return xBefore;
    }

    /**
     * Gets the previous Y coordinate of the client.
     *
     * @return The previous Y coordinate.
     */
    public int getYBefore() {
        return yBefore;
    }

    /**
     * Sets the previous X coordinate of the client.
     *
     * @param x The previous X coordinate to set.
     */
    public void setxBefore(int x) {
        xBefore = x;
    }

    /**
     * Sets the previous Y coordinate of the client.
     *
     * @param y The previous Y coordinate to set.
     */
    public void setyBefore(int y) {
        yBefore = y;
    }
}
