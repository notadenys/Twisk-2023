package main.java.twisk.monde;

public class Activite extends Etape {
    protected int temps;
    protected int ecartTemps;

    public Activite(String nom) {
        super(nom);
        this.nom = nom;
    }

    public Activite(String nom, int temps, int ecartTemps) {
        super(nom);
        this.nom = nom;
        this.temps = temps;
        this.ecartTemps = ecartTemps;
    }

    public boolean estUneActivite() {
        return true;
    }

    public boolean estUnGuichet() {
        return false;
    }
}
