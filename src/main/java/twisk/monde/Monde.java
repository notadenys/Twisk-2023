package main.java.twisk.monde;

import java.util.Iterator;


public class Monde implements Iterable<Etape>{
    private final GestionaireEtapes ge;
    private final SasEntree se;
    private final SasSortie ss;

    public Monde() {
        se = new SasEntree();
        ss = new SasSortie();
        ge = new GestionaireEtapes();
        ge.ajouter(se, ss);
    }

    public void aCommeEntree(Etape... etapes) {
        se.ajouterSuccesseur(etapes);
    }

    public void aCommeSortie(Etape... etapes) {
        forEach(etape -> etape.ajouterSuccesseur(ss));
    }

    public void ajouter(Etape...etapes) {
        ge.ajouter(etapes);
    }

    public int nbEtapes() {
        return ge.nbEtapes();
    }

    public int nbGuichets() {
        return ge.nbGuichets();
    }

    public Iterator<Etape> iterator()
    {
        return ge.iterator();
    }
}
