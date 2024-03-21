package twisk.monde;

import javax.naming.InvalidNameException;

public class SasSortie extends Activite {
    public SasSortie() throws InvalidNameException {
        super("SASSORTIE");
    }

    public boolean estUneSortie(){
        return true;
    }
}
