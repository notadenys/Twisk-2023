package twisk.simulation;

import twisk.monde.Monde;
import twisk.outils.KitC;

public class Simulation {
    public Monde monde;
    private final KitC kitC;
    public Simulation() {
        monde = new Monde();
        kitC = new KitC();
        kitC.creerEnvironnement();
    }
    public void simuler(Monde  monde) {
        System.out.println(monde);
        System.out.println(monde.toC());
        kitC.creerFichier(monde.toC());
        kitC.compiler();
        kitC.construireLaBibliotheque();
    }
}
