package twisk.simulation;

import twisk.monde.Etape;

public class Client {

    private final int numeroClient;
    private int rang;
    private Etape etape;

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
}
