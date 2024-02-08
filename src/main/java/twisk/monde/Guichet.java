package main.java.twisk.monde;

public class Guichet extends Etape {
    private int nbJetons;

    public Guichet(String nom) {
        super(nom);
        this.nom = nom;
    }

    public Guichet(String nom, int nb) {
        super(nom);
        this.nom = nom;
        this.nbJetons = nb;
    }

    public boolean estUnGuichet() {
        return true;
    }

    public boolean estUneActivite() {
        return false;
    }
}
