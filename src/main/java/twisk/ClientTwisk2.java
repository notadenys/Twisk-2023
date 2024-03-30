package twisk;

import twisk.monde.*;
import twisk.simulation.Simulation;

public class ClientTwisk2 {

    public static void main(String[] args) {
        Monde monde = new Monde();
        Activite zoo = new Activite("zoo", 4, 2);
        Guichet guichet = new Guichet("guichet",2);
        Activite another_activity = new Activite("another_activity", 4, 2);
        Guichet another_guichet = new Guichet("another_guichet", 2);
        Activite toboggan = new ActiviteRestreinte("toboggan", 2, 1);
        monde.ajouter(zoo, guichet, another_activity, another_guichet, toboggan);
        monde.getEntree().ajouterSuccesseur(zoo);
        monde.aCommeEntree(zoo);
        zoo.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(another_activity);
        another_activity.ajouterSuccesseur(another_guichet);
        another_guichet.ajouterSuccesseur(toboggan);
        monde.aCommeSortie(toboggan);
        Simulation sim = new Simulation();
        sim.simuler(monde);
    }
}