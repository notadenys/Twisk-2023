package main.java.twisk.simulation;
import main.java.twisk.monde.Monde;
public class Simulation {
    public Monde monde;
    public Simulation() {
        monde = new Monde();
    }
    public void simuler(Monde monde) {
        System.out.println(monde);
        System.out.println("\n########## Code C ##########\n");
        System.out.println(monde.toC());
        System.out.println("\n############################\n");
    }
}
