package twisk.mondeIG;

import twisk.vues.Observateur;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a subject that can be observed by observers.
 */
public class SujetObserve {
    private final ArrayList<Observateur> observateurs;

    /**
     * Constructs a new subject.
     */
    public SujetObserve() {
        this.observateurs = new ArrayList<>();
    }

    /**
     * Adds an observer to the subject.
     *
     * @param obs The observer to add.
     */
    public void ajouterObservateur(Observateur obs) {
        this.observateurs.add(obs);
    }

    /**
     * Notifies all observers that the subject has changed.
     */
    public void notifierObservateurs() {
        for (Observateur obs : observateurs) {
            obs.reagir();
        }
    }
}
