package twisk;
import twisk.monde.*;
import twisk.simulation.Simulation;
public class ClientTwisk {

    public static void main(String[] args) {
        Monde monde = new Monde();
        Etape zoo = new Activite("zoo", 4, 2);
        Etape guichet = new Guichet("guichet",2);
        Etape toboggan = new Activite("toboggan", 2, 1);
        monde.ajouter(zoo, guichet, toboggan);
        monde.getEntree().ajouterSuccesseur(zoo);
        monde.aCommeEntree(zoo);
        zoo.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(toboggan);
        monde.aCommeSortie(toboggan);
        Simulation sim = new Simulation();
        sim.simuler(monde);
    }
}