package twisk.monde;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class GestionnaireEtapes implements Iterable<Etape> {
    private final ArrayList<Etape> etapes;

    public GestionnaireEtapes() {
        etapes = new ArrayList<>();
    }

    public ArrayList<Etape> getEtapes(){
        return etapes;
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

    public Etape getEtapeByID(int id) {
        for (Etape etape : etapes) {
            if (etape.getNum() == id) {
                return etape;
            }
        }
        throw new NoSuchElementException("No Etape found with ID: " + id);
    }

    public String getNomByID(int id) {
        return getEtapeByID(id).getNom();
    }
}
