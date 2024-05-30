package twisk.monde;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Manages a collection of steps (etapes) in the workflow.
 */
public class GestionnaireEtapes implements Iterable<Etape> {
    private final ArrayList<Etape> etapes;

    /**
     * Constructs an empty GestionnaireEtapes.
     */
    public GestionnaireEtapes() {
        etapes = new ArrayList<>();
    }

    /**
     * Returns the list of steps managed by this GestionnaireEtapes.
     *
     * @return the list of steps.
     */
    public ArrayList<Etape> getEtapes() {
        return etapes;
    }

    /**
     * Adds steps to the workflow.
     *
     * @param etapes the steps to add.
     */
    public void ajouter(Etape... etapes) {
        this.etapes.addAll(Arrays.asList(etapes));
    }

    /**
     * Returns the number of steps in the workflow.
     *
     * @return the number of steps.
     */
    public int nbEtapes() {
        return etapes.size();
    }

    /**
     * Returns the number of ticket offices (guichets) in the workflow.
     *
     * @return the number of ticket offices.
     */
    public int nbGuichets() {
        int nb = 0;
        for (Etape etape : etapes) {
            if (etape.estUnGuichet()) {
                nb++;
            }
        }
        return nb;
    }

    /**
     * Returns an iterator over the steps.
     *
     * @return an iterator over the steps.
     */
    @Override
    public Iterator<Etape> iterator() {
        return etapes.iterator();
    }

    /**
     * Returns a string representation of this GestionnaireEtapes.
     *
     * @return a string representation of this GestionnaireEtapes.
     */
    @Override
    public String toString() {
        return "GestionaireEtapes{" +
                "etapes=" + etapes +
                '}';
    }

    /**
     * Returns the step with the specified ID.
     *
     * @param id the ID of the step.
     * @return the step with the specified ID.
     * @throws NoSuchElementException if no step with the specified ID is found.
     */
    public Etape getEtapeByID(int id) {
        for (Etape etape : etapes) {
            if (etape.getNum() == id) {
                return etape;
            }
        }
        throw new NoSuchElementException("No Etape found with ID: " + id);
    }

    /**
     * Returns the name of the step with the specified ID.
     *
     * @param id the ID of the step.
     * @return the name of the step with the specified ID.
     */
    public String getNomByID(int id) {
        return getEtapeByID(id).getNom();
    }
}
