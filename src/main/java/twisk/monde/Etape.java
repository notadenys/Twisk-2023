package twisk.monde;

import twisk.outils.FabriqueNumero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public abstract class Etape implements Iterable<Etape> {
    private final String nom;
    private final ArrayList<Etape> successeurs;
    private final int numEtape;

    /**
     * @param nom name of stage
     */
    public Etape(String nom) {
        this.nom = nom;
        this.successeurs = new ArrayList<>();
        numEtape = FabriqueNumero.getInstance().getNumeroEtape();
    }

    /**
     * @param successeurs adding successors
     */
    public void ajouterSuccesseur(Etape... successeurs) {
        Collections.addAll(this.successeurs, successeurs);
    }

    public int nbSuccesseurs()
    {
        return successeurs.size();
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
    public int getNum(){return numEtape;}

    public ArrayList<Etape> getSuccesseurs() {
        return successeurs;
    }

    @Override
    public String toString() {
        return getNom();
    }
}
