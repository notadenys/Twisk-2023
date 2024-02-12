package main.java.twisk.monde;

public class Activite extends Etape {
    private int temps = 0;
    private int ecartTemps = 0;
    /**
     * @param nom name of activity
     */
    public Activite(String nom) {
        super(nom);
    }
    /**
     * @param nom name of activity
     * @param temps time on activity
     * @param ecartTemps !!! потребує уточнення !!!
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

    @Override
    public String toString() {
        return "Activite{" +
                "nom=" + getNom() +
                "temps=" + temps +
                ", ecartTemps=" + ecartTemps +
                '}';
    }
}
