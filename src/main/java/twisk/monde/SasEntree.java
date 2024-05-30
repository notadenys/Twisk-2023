package twisk.monde;

import javax.naming.InvalidNameException;

/**
 * Represents the entry point (SasEntree) in the world.
 * It is a special type of activity with predefined time and time variation.
 */
public class SasEntree extends Activite {

    /**
     * Constructs a SasEntree with the name "SASENTREE" and predefined time values.
     *
     * @throws InvalidNameException if the name is invalid.
     */
    public SasEntree() throws InvalidNameException {
        super("SASENTREE");
        this.temps = 6;
        this.ecartTemps = 3;
    }

    /**
     * Indicates that this stage is an entry point.
     *
     * @return true, as this stage is an entry point.
     */
    @Override
    public boolean estUneEntree() {
        return true;
    }
}
