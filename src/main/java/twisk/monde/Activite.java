package twisk.monde;

import javax.naming.InvalidNameException;
import java.util.Iterator;

/**
 * Represents an activity step in the workflow.
 */
public class Activite extends Etape {
    protected int temps = 0;
    protected int ecartTemps = 0;

    /**
     * Constructor for creating an activity with a given name.
     *
     * @param nom the name of the activity.
     * @throws InvalidNameException if the name is invalid.
     */
    public Activite(String nom) throws InvalidNameException {
        super(nom);
    }

    /**
     * Constructor for creating an activity with a given name, time, and time variance.
     *
     * @param nom       the name of the activity.
     * @param temps     the duration of the activity.
     * @param ecartTemps the variance in the duration of the activity.
     */
    public Activite(String nom, int temps, int ecartTemps) {
        super(nom);
        this.temps = temps;
        this.ecartTemps = ecartTemps;
    }

    /**
     * Checks if this step is an activity.
     *
     * @return true if it is an activity, false otherwise.
     */
    @Override
    public boolean estUneActivite() {
        return true;
    }

    /**
     * Checks if this step is a guichet (booth).
     *
     * @return false as this is not a guichet.
     */
    @Override
    public boolean estUnGuichet() {
        return false;
    }

    /**
     * Checks if this step is a sortie (exit).
     *
     * @return false as this is not a sortie.
     */
    @Override
    public boolean estUneSortie() {
        return false;
    }

    /**
     * Gets the duration of the activity.
     *
     * @return the duration of the activity.
     */
    public int getTemps() {
        return temps;
    }

    /**
     * Gets the variance in the duration of the activity.
     *
     * @return the variance in the duration of the activity.
     */
    public int getEcartTemps() {
        return ecartTemps;
    }

    /**
     * Generates the C code representation of the activity step.
     *
     * @return the C code as a String.
     */
    @Override
    public String toC() {
        if (this.getSuccesseurs() == null || this.getSuccesseurs().isEmpty()) {
            return "";
        }

        Iterator<Etape> successeurs = this.getSuccesseurs().iterator();
        Etape successeur;

        StringBuilder str = new StringBuilder();
        if (estUneEntree()) {
            String s0 = "    entrer(" + this.getConstNom() + ");\n";
            str.append(s0);
        }
        String s1 = "    delai(" + this.temps + "," + this.ecartTemps + ");\n";
        str.append(s1);

        if (getSuccesseurs().size() > 1) {
            str.append("    choice = (int)((rand() / (float) RAND_MAX ) * ").append(nbSuccesseurs()).append(");\n");
            str.append("    switch(choice) {\n");
            for (int i = 0; i < getSuccesseurs().size(); i++) {
                str.append("    case ").append(i).append(":\n");

                successeur = successeurs.next();
                String s2 = "    transfert(" + this.getConstNom() + ","+ successeur.getConstNom() +");\n" ;
                str.append(s2);
                str.append(successeur.toC());
                str.append("    break;\n");
            }
            str.append("    }\n");
        } else {
            successeur = successeurs.next();
            String s2 = "    transfert(" + this.getConstNom() + ","+ this.getSuccesseur().getConstNom() +");\n" ;
            str.append(s2);

            str.append(successeur.toC());
        }
        return str.toString();
    }

    /**
     * Gets the constant name representation of the activity.
     *
     * @return the constant name.
     */
    @Override
    public String getConstNom() {
        return getModifiedNom().toUpperCase();
    }

    /**
     * Gets the define directive for the activity.
     *
     * @return the define directive as a String.
     */
    @Override
    public String toDefine() {
        return "#define " + getConstNom() + " " + getNum();
    }

    /**
     * Checks if this activity is a restricted activity.
     *
     * @return false as this is not a restricted activity.
     */
    public boolean estUneActiviteRestreinte() {
        return false;
    }
}
