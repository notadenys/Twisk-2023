package main.java.twisk.monde;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class GestionaireEtapes implements Iterable<Etape> {
    private final ArrayList<Etape> etapes;

    public GestionaireEtapes() {
        etapes = new ArrayList<>();
    }

    /**
     * add stages to the world
     * @param etapes stages to add
     */
    public void ajouter(Etape...etapes) {
        this.etapes.addAll(Arrays.asList(etapes));
    }

    /**
     * @return amount of existing stages
     */
    public int nbEtapes() {
        return etapes.size();
    }

    /**
     * @return amount of counters
     */
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
