package twisk.simulation;

import twisk.monde.Monde;
import twisk.outils.KitC;

import java.io.File;

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
        kitC.creerFichier(monde.toC());
    }
}
