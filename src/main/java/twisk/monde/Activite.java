package twisk.monde;

import javax.naming.InvalidNameException;

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
    public String toC() {
        if(this.getSuccesseur() == null && this.estUneActivite()) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        String s1 = "    transfert(" + this.getConstNom() + ","+ this.getSuccesseur().getConstNom() +");\n" ;
        String s2 = "    delai(" + this.temps + "," + this.ecartTemps + ");\n";

        str.append(s2);
        str.append(s1);
        str.append(this.getSuccesseur().toC());
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
}
