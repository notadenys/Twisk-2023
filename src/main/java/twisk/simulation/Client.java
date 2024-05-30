package twisk.simulation;

import twisk.monde.Etape;

import java.util.Random;

public class Client {

    private final int numeroClient;
    private int rang;
    private Etape etape;
    private final int color;

    public Client(int numero) {
        this.numeroClient = numero;
        this.rang = 0;
        Random random = new Random();
        color = random.nextInt(5);
    }

    public void allerA(Etape etape, int rang) {
        this.rang = rang;
        this.etape = etape;
    }

    public int getNumeroClient() {
        return numeroClient;
    }
    public int getColor() { return color; }
    public Etape getEtapeActuelle() {
        return this.etape;
    }
    public int getRang() {
        return this.rang;
    }
}
