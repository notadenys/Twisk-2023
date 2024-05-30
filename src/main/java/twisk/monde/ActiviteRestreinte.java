package twisk.monde;

import javax.naming.InvalidNameException;

/**
 * Represents a restricted activity step in the workflow.
 */
public class ActiviteRestreinte extends Activite {

    /**
     * Constructor for creating a restricted activity with a given name.
     *
     * @param nom the name of the restricted activity.
     * @throws InvalidNameException if the name is invalid.
     */
    public ActiviteRestreinte(String nom) throws InvalidNameException {
        super(nom);
    }

    /**
     * Constructor for creating a restricted activity with a given name, time, and time variance.
     *
     * @param nom        the name of the restricted activity.
     * @param temps      the duration of the restricted activity.
     * @param ecartTemps the variance in the duration of the restricted activity.
     */
    public ActiviteRestreinte(String nom, int temps, int ecartTemps) {
        super(nom, temps, ecartTemps);
    }

    /**
     * Checks if this step is a restricted activity.
     *
     * @return true as this is a restricted activity.
     */
    @Override
    public boolean estUneActiviteRestreinte() {
        return true;
    }
}
