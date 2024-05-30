package twisk.monde;

import twisk.outils.FabriqueNumero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

/**
 * Represents a step in the workflow.
 */
public abstract class Etape implements Iterable<Etape> {
    private final String nom;
    private final String modifiedNom;
    private final ArrayList<Etape> successeurs;
    private final int numEtape;

    /**
     * Constructs an Etape with the specified name.
     *
     * @param nom the name of the step.
     */
    public Etape(String nom) {
        this.nom = nom;
        // Replaces all spaces with underscores and all non-word characters with a random letter.
        this.modifiedNom = nom.replace(' ', '_')
                .replaceAll("\\W", Character.toString((char)(97 + new Random().nextInt(25))))
                .replaceAll("^[0-9]", Character.toString((char)(97 + new Random().nextInt(25))));
        this.successeurs = new ArrayList<>();
        this.numEtape = FabriqueNumero.getInstance().getNumeroEtape();
    }

    /**
     * Adds successors to this step.
     *
     * @param successeurs the successors to add.
     */
    public void ajouterSuccesseur(Etape... successeurs) {
        Collections.addAll(this.successeurs, successeurs);
    }

    /**
     * Checks if this step is an activity.
     *
     * @return true if this step is an activity, false otherwise.
     */
    public abstract boolean estUneActivite();

    /**
     * Checks if this step is a ticket office (guichet).
     *
     * @return true if this step is a ticket office, false otherwise.
     */
    public abstract boolean estUnGuichet();

    /**
     * Checks if this step is an exit.
     *
     * @return true if this step is an exit, false otherwise.
     */
    public abstract boolean estUneSortie();

    /**
     * Checks if this step is an entry.
     *
     * @return true if this step is an entry, false otherwise.
     */
    public boolean estUneEntree() {
        return false;
    }

    /**
     * Returns the C code representation of this step.
     *
     * @return the C code representation of this step.
     */
    public abstract String toC();

    /**
     * Returns the C define representation of this step.
     *
     * @return the C define representation of this step.
     */
    public abstract String toDefine();

    /**
     * Returns an iterator over the successors of this step.
     *
     * @return an iterator over the successors of this step.
     */
    public Iterator<Etape> iterator() {
        return successeurs.iterator();
    }

    /**
     * Returns the name of this step.
     *
     * @return the name of this step.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Returns the modified name of this step.
     *
     * @return the modified name of this step.
     */
    public String getModifiedNom() {
        return modifiedNom;
    }

    /**
     * Returns the constant name of this step in C.
     *
     * @return the constant name of this step in C.
     */
    public abstract String getConstNom();

    /**
     * Returns the unique number of this step.
     *
     * @return the unique number of this step.
     */
    public int getNum() {
        return numEtape;
    }

    /**
     * Returns the list of successors of this step.
     *
     * @return the list of successors of this step.
     */
    public ArrayList<Etape> getSuccesseurs() {
        return successeurs;
    }

    /**
     * Returns the first successor of this step, if any.
     *
     * @return the first successor of this step, or null if there are no successors.
     */
    public Etape getSuccesseur() {
        if (successeurs.isEmpty()) {
            return null;
        }
        return successeurs.get(0);
    }

    /**
     * Returns the number of successors of this step.
     *
     * @return the number of successors of this step.
     */
    public int nbSuccesseurs() {
        return successeurs.size();
    }

    /**
     * Returns a string representation of this step.
     *
     * @return a string representation of this step.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getNom()).append(" (id : ").append(getNum()).append(") : ").append(nbSuccesseurs()).append(" successeur(s)");
        if (nbSuccesseurs() > 0) {
            sb.append(" - ");
            for (Etape etape : getSuccesseurs()) {
                sb.append(etape.getNom()).append(" ");
            }
        }
        return sb.toString();
    }
}
