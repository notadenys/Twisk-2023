package main.java.twisk.monde;

public class ActiviteRestreinte extends Activite{
    public ActiviteRestreinte(String nom) {
        super(nom);
        this.nom = nom;
    }

    public ActiviteRestreinte(String nom, int temps, int ecartTemps) {
        super(nom);
        this.nom = nom;
        this.temps = temps;
        this.ecartTemps = ecartTemps;
    }
}
