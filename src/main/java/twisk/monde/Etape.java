package main.java.twisk.monde;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Etape implements Iterable<Etape> {
    protected String nom;
    protected ArrayList<Etape> successeur;
    protected int nEtape;
    /**
     * @param nom name of stage
     */
    public Etape(String nom) {
        this.nom = nom;
        this.nEtape = 0;
        this.successeur = new ArrayList<>();
        this.successeur.add(this);
    }
    /**
     * @param successeurs adding stages
     */
    void ajouterSuccesseur(Etape[] successeurs) {
        for(Etape etape : successeurs) {
            successeur.add(etape);
            nEtape++;
        }
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
        return successeur.iterator();
    }
}
