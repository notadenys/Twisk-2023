package main.java.twisk;
import main.java.twisk.monde.Activite;
import main.java.twisk.monde.Etape;
import main.java.twisk.monde.Guichet;
import main.java.twisk.monde.Monde;
import main.java.twisk.simulation.Simulation;
public class ClientTwisk {

    public static void main(String[] args) {
        Monde monde = new Monde();
        Etape zoo = new Activite("zoo", 4, 2);
        Etape guichet = new Guichet("guichet",2);
        Etape toboggan = new Activite("toboggan", 2, 1);
        monde.ajouter(zoo, guichet, toboggan);
        monde.aCommeEntree(zoo);
        zoo.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(toboggan);
        monde.aCommeSortie(toboggan);
        Simulation sim = new Simulation();
        sim.simuler(monde);
    }
}