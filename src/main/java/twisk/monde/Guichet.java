package twisk.monde;

import twisk.outils.FabriqueNumero;

/**
 * Represents a ticket office (Guichet) in the workflow.
 */
public class Guichet extends Etape {
    private final int nbJetons;
    private int nSemaphore;

    /**
     * Constructs a Guichet with the specified name and no tokens.
     *
     * @param nom the name of the Guichet.
     */
    public Guichet(String nom) {
        super(nom);
        nbJetons = 0;
    }

    /**
     * Constructs a Guichet with the specified name and number of tokens.
     *
     * @param nom the name of the Guichet.
     * @param nb  the number of tokens.
     */
    public Guichet(String nom, int nb) {
        super(nom);
        this.nbJetons = nb;
        this.nSemaphore = FabriqueNumero.getInstance().getNumeroSemaphore();
    }

    /**
     * Returns the number of tokens (jetons) in this Guichet.
     *
     * @return the number of tokens.
     */
    public int getNbJetons() {
        return nbJetons;
    }

    /**
     * Indicates whether this Etape is a Guichet.
     *
     * @return true, since this Etape is a Guichet.
     */
    public boolean estUnGuichet() {
        return true;
    }

    /**
     * Indicates whether this Etape is an Activite.
     *
     * @return false, since this Etape is not an Activite.
     */
    public boolean estUneActivite() {
        return false;
    }

    /**
     * Indicates whether this Etape is a Sortie.
     *
     * @return false, since this Etape is not a Sortie.
     */
    public boolean estUneSortie() {
        return false;
    }

    /**
     * Generates the C code representation of this Guichet.
     *
     * @return the C code representation of this Guichet.
     */
    public String toC() {
        if (this.estUnGuichet()) {
            StringBuilder str = new StringBuilder();
            String s1 = "    P(ids," + nSemaphore + ");\n";
            if (this.getSuccesseur() != null) {
                String s2 = "    transfert(" + this.getConstNom() + "," + this.getSuccesseur().getConstNom() + ");\n";
                String s4 = "    delai(3,1);\n";
                String s3 = "    V(ids," + nSemaphore + ");\n";
                str.append(s1).append(s2).append(s4).append(s3);
                str.append(this.getSuccesseur().toC());
            }
            return str.toString();
        }
        return "";
    }

    /**
     * Returns the constant name of this Guichet.
     *
     * @return the constant name of this Guichet.
     */
    @Override
    public String getConstNom() {
        return "GUICHET_" + getModifiedNom().toUpperCase();
    }

    /**
     * Returns the constant semaphore name of this Guichet.
     *
     * @return the constant semaphore name of this Guichet.
     */
    public String getConstSem() {
        return "SEM_TICKET_" + getModifiedNom().toUpperCase();
    }

    /**
     * Returns the definition of the constants for this Guichet.
     *
     * @return the definition of the constants.
     */
    @Override
    public String toDefine() {
        return "#define " + getConstNom() + " " + getNum() + "\n#define " + getConstSem() + " " + nSemaphore;
    }
}
