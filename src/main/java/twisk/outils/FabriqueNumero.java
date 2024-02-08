package main.java.twisk.outils;

public class FabriqueNumero {
    private int cptEtape;

    private static FabriqueNumero ourInstance = new FabriqueNumero();

    public static FabriqueNumero getInstance() {
        return ourInstance;
    }

    private FabriqueNumero() {

    }
    public int getNumeroEtape() {
        return 0;
    }
    public void reset() {

    }
}
