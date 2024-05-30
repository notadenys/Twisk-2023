package twisk.monde;

import javax.naming.InvalidNameException;

/**
 * Represents the exit point (SasSortie) in the world.
 * It is a special type of activity with a predefined name.
 */
public class SasSortie extends Activite {

    /**
     * Constructs a SasSortie with the name "SASSORTIE".
     *
     * @throws InvalidNameException if the name is invalid.
     */
    public SasSortie() throws InvalidNameException {
        super("SASSORTIE");
    }

    /**
     * Indicates that this stage is an exit point.
     *
     * @return true, as this stage is an exit point.
     */
    @Override
    public boolean estUneSortie() {
        return true;
    }
}
