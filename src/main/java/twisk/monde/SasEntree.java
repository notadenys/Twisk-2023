package twisk.monde;

import javax.naming.InvalidNameException;

public class SasEntree extends Activite {
    public SasEntree() throws InvalidNameException {
        super("SASENTREE");
        this.temps = 6;
        this.ecartTemps = 3;
    }

    @Override
    public boolean estUneEntree() { return true; }
}
