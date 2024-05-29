package twisk.monde;

import javax.naming.InvalidNameException;
import java.util.Iterator;

public class Activite extends Etape {
    protected int temps = 0;
    protected int ecartTemps = 0;
    /**
     * @param nom name of activity
     */
    public Activite(String nom) throws InvalidNameException {
        super(nom);
    }
    /**
     * @param nom name of activity
     * @param temps time on activity
     * @param ecartTemps +- time on activity
     */
    public Activite(String nom, int temps, int ecartTemps){
        super(nom);
        this.temps = temps;
        this.ecartTemps = ecartTemps;
    }

    public boolean estUneActivite() {
        return true;
    }

    public boolean estUnGuichet() {
        return false;
    }
    public boolean estUneSortie() {
        return false;
    }

    /**
     * @return time of activity
     */
    public int getTemps() {
        return temps;
    }

    /**
     * @return +- to time of activity
     */
    public int getEcartTemps() {
        return ecartTemps;
    }

    /**
     * @return code C of activity step
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
        return str.toString() ;
    }

    @Override
    public String getConstNom()
    {
        return getModifiedNom().toUpperCase();
    }

    @Override
    public String toDefine()
    {
        return "#define " + getConstNom() + " " + getNum();
    }

    public boolean estUneActiviteRestreinte() {
        return false;
    }
}
