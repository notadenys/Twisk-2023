package main.java.twisk.monde;
import java.security.Provider;
import java.util.ArrayList;
public class Monde {
    private ArrayList<Etape> etapesList;
    private int nbEtapes;
    private int nbGuichets;

    public Monde() {
        this.etapesList = new ArrayList<Etape>();
    }

    void aCommeEntree(Etape...etapes) {

    }

    void aCommeSortie(Etape...etapes) {

    }

    void ajouter(Etape...etapes) {
        for (Etape e : etapes) {
            if(etapesList.contains(e)) {
                etapesList.add(etapesList.size()-1, e);
                nbEtapes++;
            }
            if(e instanceof Guichet) {
                etapesList.add((Guichet)e);
                nbGuichets++;
            }
        }
    }

    int nbEtapes() {
        return 0;
    }

    int nbGuichets() {
        return 0;
    }

}
