package twisk.mondeIG;

import twisk.vues.Observateur;

import java.util.concurrent.CopyOnWriteArrayList;

public class SujetObserve {
    private final CopyOnWriteArrayList<Observateur> observateurs ;

    public SujetObserve(){
        this.observateurs = new CopyOnWriteArrayList<>() ;
    }

    public void ajouterObservateur(Observateur obs){
        this.observateurs.add(obs) ;
    }

    public void notifierObservateurs(){
        for (Observateur obs : observateurs){
            obs.reagir() ;
        }
    }
}
