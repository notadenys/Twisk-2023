package twisk.monde;

import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the world (Monde) which contains all the stages (Etapes) of a process.
 */
public class Monde implements Iterable<Etape> {
    private GestionnaireEtapes ge;
    private SasEntree se;
    private SasSortie ss;

    /**
     * Constructs a Monde with an entry (SasEntree) and an exit (SasSortie).
     */
    public Monde() {
        try {
            se = new SasEntree();
            ss = new SasSortie();
            ge = new GestionnaireEtapes();
            ge.ajouter(se, ss);
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the entry stages of the world.
     *
     * @param etapes the stages to set as entries.
     */
    public void aCommeEntree(Etape... etapes) {
        se.ajouterSuccesseur(etapes);
    }

    /**
     * Sets the exit stages of the world.
     *
     * @param etapes the stages to set as exits.
     */
    public void aCommeSortie(Etape... etapes) {
        for (Etape etape : etapes) {
            etape.ajouterSuccesseur(ss);
        }
    }

    /**
     * Adds stages to the world.
     *
     * @param etapes the stages to add.
     */
    public void ajouter(Etape... etapes) {
        ge.ajouter(etapes);
    }

    /**
     * Returns the number of existing stages.
     *
     * @return the number of stages.
     */
    public int nbEtapes() {
        return ge.nbEtapes();
    }

    /**
     * Returns the number of counters (Guichets).
     *
     * @return the number of counters.
     */
    public int nbGuichets() {
        return ge.nbGuichets();
    }

    /**
     * Returns an iterator over the stages in this world.
     *
     * @return an iterator over the stages.
     */
    public Iterator<Etape> iterator() {
        return ge.iterator();
    }

    /**
     * Returns the entry stage (SasEntree).
     *
     * @return the entry stage.
     */
    public SasEntree getEntree() {
        return se;
    }

    /**
     * Returns the exit stage (SasSortie).
     *
     * @return the exit stage.
     */
    public SasSortie getSortie() {
        return ss;
    }

    /**
     * Returns a string representation of the world.
     *
     * @return a string representation of the stages.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Etape etape : this) {
            sb.append(etape).append('\n');
        }
        return sb.toString();
    }

    /**
     * Generates the C code representation of the world.
     *
     * @return the C code representation.
     */
    public String toC() {
        StringBuilder str = new StringBuilder();
        str.append("#include <stdio.h> \n" +
                "#include <stdlib.h>\n" +
                "#include <time.h>\n" +
                "#include \"def.h\"\n\n");
        for (Etape etape : ge.getEtapes()) {
            str.append(etape.toDefine());
            str.append("\n");
        }
        str.append("\n\nvoid simulation(int ids) {\n");
        if (!isLinear()) str.append("    int choice;\n");

        ArrayList<Etape> etapes = ge.getEtapes();
        str.append(etapes.get(0).toC());
        str.append("} ");
        return str.toString();
    }

    /**
     * Checks if the process flow is linear.
     *
     * @return true if the flow is linear, false otherwise.
     */
    private boolean isLinear() {
        for (Etape etape : ge.getEtapes()) {
            if (etape.getSuccesseurs().size() > 1) {
                return false;
            }
        }
        return true;
    }
}
