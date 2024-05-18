package twisk.monde;

import javax.naming.InvalidNameException;

public class ActiviteRestreinte extends Activite{
    public ActiviteRestreinte(String nom) throws InvalidNameException {
        super(nom);
    }
    public boolean estUneActiviteRestreinte(){
        return true;
    }

    public ActiviteRestreinte(String nom, int temps, int ecartTemps) {
        super(nom, temps, ecartTemps);
    }
}
