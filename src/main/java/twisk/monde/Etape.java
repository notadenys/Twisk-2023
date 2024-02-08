package main.java.twisk.monde;

public abstract class Etape {
    protected String nom;

    public Etape(String nom) {
        this.nom = nom;
    }

    void ajouterSucceseur(Etape...succeseurs) {

    }

    public abstract boolean estUneActivite();

    public abstract boolean estUnGuichet();

}
