package twisk.mondeIG;

import twisk.exceptions.JetonsException;
import twisk.outils.TailleComposants;

/**
 * Represents a graphical ticket office in the interactive world.
 */
public class GuichetIG extends EtapeIG {
    private int nbJetons;

    /**
     * Constructs a GuichetIG with the specified number of tokens.
     *
     * @param nbJetons The number of tokens at the ticket office.
     */
    public GuichetIG(int nbJetons) {
        super();
        this.nbJetons = nbJetons;
        setNom("Guichet" + getId());
        setLargeur(TailleComposants.getInstance().getGuichetW());
        setHauteur(TailleComposants.getInstance().getGuichetH());
    }

    /**
     * Gets the number of tokens at the ticket office.
     *
     * @return The number of tokens.
     */
    public int getNbJetons() {
        return nbJetons;
    }

    /**
     * Sets the number of tokens at the ticket office.
     *
     * @param nb The number of tokens to set.
     * @throws JetonsException If the number of tokens is invalid (less than 1).
     */
    public void setNbJetons(int nb) throws JetonsException {
        if (nb < 1) throw new JetonsException("Invalid number of tokens");
        nbJetons = nb;
    }

    /**
     * Checks if this stage is a ticket office.
     *
     * @return True, as this is a ticket office.
     */
    @Override
    public boolean estUnGuichet() {
        return true;
    }

    /**
     * Checks if this stage is an activity.
     *
     * @return False, as this is not an activity.
     */
    @Override
    public boolean estUneActivite() {
        return false;
    }

    /**
     * Checks if this stage is an exit.
     *
     * @return False, as this is not an exit.
     */
    @Override
    public boolean estUneSortie() {
        return false;
    }

    /**
     * Returns a string representation of the ticket office, including its name and number of tokens.
     *
     * @return A string representation of the ticket office.
     */
    @Override
    public String toString() {
        return getNom() + " : " + getNbJetons() + " jetons";
    }
}
