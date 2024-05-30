package twisk.simulation;

import twisk.monde.Etape;

public class Client {

    private final int numeroClient;
    private int rang;
    private Etape etape;
    private boolean isUpdated;
    private int xBefore;
    private int yBefore;

    public Client(int numero) {
        this.numeroClient = numero;
        this.rang = 0;
    }

    public void allerA(Etape etape, int rang) {
        this.rang = rang;
        this.etape = etape;
    }

    public int getNumeroClient() {
        return numeroClient;
    }

    public Etape getEtapeActuelle() {
        return this.etape;
    }
    public int getRang() {
        return this.rang;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        this.isUpdated = updated;
    }

    public int getXBefore() {
        return xBefore;
    }

    public int getYBefore() {
        return yBefore;
    }

    public void setxBefore(int x) {
        xBefore = x;
    }

    public void setyBefore(int y) {
        yBefore = y;
    }
}
