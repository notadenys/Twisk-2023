package main.java.twisk.monde;

public class Guichet extends Etape {
    private int nbJetons;

    public Guichet(String nom) {
        super(nom);
    }

    public Guichet(String nom, int nb) {
        super(nom);
        this.nbJetons = nb;
    }

    public boolean estUnGuichet() {
        return true;
    }

    public boolean estUneActivite() {
        return false;
    }

    @Override
    public String toString() {
        return "Guichet{" +
                "nom=" + getNom() +
                "nbJetons=" + nbJetons +
                '}';
    }
}
