package main.java.twisk.monde;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public abstract class Etape implements Iterable<Etape> {
    private final String nom;
    private final ArrayList<Etape> successeurs;
    private int nEtapes;

    /**
     * @param nom name of stage
     */
    public Etape(String nom) {
        this.nom = nom;
        this.successeurs = new ArrayList<>();
    }

    /**
     * @param successeurs adding successors
     */
    void ajouterSuccesseur(Etape... successeurs) {
        Collections.addAll(this.successeurs, successeurs);
    }

    /**
     * @return true if activity
     */
    public abstract boolean estUneActivite();

    /**
     * @return true if ticket office(guichet)
     */
    public abstract boolean estUnGuichet();

    public Iterator<Etape> iterator() {
        return successeurs.iterator();
    }

    public String getNom(){return nom;}

    @Override
    public String toString() {
        return "Etape{" +
                "nom='" + nom + '\'' +
                ", successeurs=" + successeurs +
                ", nEtapes=" + nEtapes +
                '}';
    }
}
