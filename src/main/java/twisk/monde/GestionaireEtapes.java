package main.java.twisk.monde;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class GestionaireEtapes implements Iterable<Etape> {
    private final ArrayList<Etape> etapes;

    public GestionaireEtapes() {
        etapes = new ArrayList<>();
    }

    public void ajouter(Etape...etapes) {
        this.etapes.addAll(Arrays.asList(etapes));
    }

    public int nbEtapes() {
        return etapes.size();
    }

    public int nbGuichets() {
        int nb = 0;
        for (Etape etape : etapes)
        {
            if (etape.estUnGuichet())
            {
                nb++;
            }
        }
        return nb;
    }

    @Override
    public Iterator<Etape> iterator()
    {
        return etapes.iterator();
    }

    @Override
    public String toString() {
        return "GestionaireEtapes{" +
                "etapes=" + etapes +
                '}';
    }
}
