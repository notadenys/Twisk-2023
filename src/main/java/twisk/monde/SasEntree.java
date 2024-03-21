package twisk.monde;

import javax.naming.InvalidNameException;

public class SasEntree extends Activite {
    public SasEntree() throws InvalidNameException {
        super("SASENTREE");
        this.temps = 6;
        this.ecartTemps = 3;
    }

    public String toC() {
        if(this.getSuccesseurs() == null) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        String s1 = "    entrer(" + this.getConstNom() + ");\n";
        String s2 = "    delai(" + this.getTemps() + ", " + this.getEcartTemps() + ");\n";
        String s3 = "    transfert(" + this.getConstNom() + ","+ this.getSuccesseur().getConstNom() +");\n";

        str.append(s1).append(s2).append(s3);
        return str.toString();
    }
}
