package twisk.monde;

public class Activite extends Etape {
    protected int temps = 0;
    protected int ecartTemps = 0;
    /**
     * @param nom name of activity
     */
    public Activite(String nom) {
        super(nom);
    }
    /**
     * @param nom name of activity
     * @param temps time on activity
     * @param ecartTemps +- time on activity
     */
    public Activite(String nom, int temps, int ecartTemps) {
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
        if(this.getSuccesseur() == null) {
            System.out.println("0/n");
            return "";
        }
        StringBuilder str = new StringBuilder();
        String s1 = "transfert(" + this.getNum() + ","+ this.getSuccesseur().getNum() +");\n" ;
        String s2 = "";

        if (this.getSuccesseur().estUnGuichet()) {
            s2 = "";
        } else {
            s2 = "delai(" + this.temps + "," + this.ecartTemps + ");\n";
        }
        str.append(s1).append(s2);
        str.append(this.getSuccesseur().toC());
        return str.toString() ;
    }
}
