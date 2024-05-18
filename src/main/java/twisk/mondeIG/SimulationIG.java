package twisk.mondeIG;

import twisk.exceptions.MondeException;
import twisk.monde.Activite;
import twisk.monde.ActiviteRestreinte;
import twisk.monde.Guichet;
import twisk.monde.Monde;
import twisk.outils.CorrespondancesEtapes;
import twisk.outils.FabriqueNumero;
import twisk.simulation.Simulation;

import javax.naming.InvalidNameException;
import java.util.Iterator;
import java.util.Map.Entry;

public class SimulationIG {
    private final MondeIG monde;
    private CorrespondancesEtapes correspondances;

    public SimulationIG(MondeIG mondeIG) {
        monde = mondeIG;
    }

    // verifies the world to meet all the constraints
    private void verifierMondeIG() throws MondeException {
        // etapes deconnectees
        for (EtapeIG etape : monde.getEtapes())
        {
            if (etape.getPredecesseurs().isEmpty() && !etape.estUneEntree())
                throw new MondeException("Etape " + etape.getNom() + " est deconnectee");

            if (etape.getSuccesseurs().isEmpty() && !etape.estUneSortie())
                throw new MondeException("Etape " + etape.getNom() + " est deconnectee");
        }

        // pas d'entrees
        if (monde.getEntrees().isEmpty())
            throw new MondeException("Il n'y a aucun entree defini");

        // pas de sorties
        if (monde.getSorties().isEmpty())
            throw new MondeException("Il n'y a aucun sortie defini");

        // guichets ont mauvais nombre de successeurs
        for (GuichetIG guichet : monde.getGuichets()) {
            if (guichet.getSuccesseurs().isEmpty())
                throw new MondeException("Guichet " + guichet.getNom() + " n'a pas de successeurs");
            if (guichet.getSuccesseurs().size() > 1)
                throw new MondeException("Guichet " + guichet.getNom() + " a plus que 1 successeur");
            if (guichet.getSuccesseurs().get(0).estUnGuichet())
                throw new MondeException("Guichet " + guichet.getNom() + " a un guichet comme successeur");
        }

        // mettre a jour les activites restraintes et verifier s'ils ont que 1 predecesseur
        for (GuichetIG guichet : monde.getGuichets())
        {
            ActiviteIG activite = (ActiviteIG)guichet.getSuccesseurs().get(0);
            activite.setRestrainte(true);
            if (activite.getPredecesseurs().size() > 1)
                throw new MondeException("Activite " + activite.getNom() + " a plus que 1 predecesseur");
        }

    }

    private Monde creerMonde() {
        FabriqueNumero.getInstance().reset();
        Monde mondeSim = new Monde();
        this.correspondances = new CorrespondancesEtapes();
        Iterator<Entry<Integer, EtapeIG>> iter = this.monde.iterator();
        while (this.monde.iterator().hasNext()) { // iterating through all etaps of MondeIG
            EtapeIG etapeIG = iter.next().getValue();
            if(etapeIG.estUneActivite()) { // case we have activity
                ActiviteIG actIG = (ActiviteIG) etapeIG;
                Activite act;
                if(actIG.isRestrainte()) { // checks activity for being Restrainte
                    act = new ActiviteRestreinte(actIG.getNom(), actIG.getTemps(), actIG.getEcartTemps());
                } else {
                    act = new Activite(actIG.getNom(), actIG.getTemps(), actIG.getEcartTemps());
                }
                correspondances.ajouter(actIG, act);
                if(actIG.estUneEntree()) { // checks for being first/last etape
                    mondeSim.aCommeEntree(act);
                } else if(actIG.estUneSortie()) {
                    mondeSim.aCommeSortie(act);
                }
                for(EtapeIG succ : actIG.getSuccesseurs()) { // adding next steps depending on their type
                    if(succ.estUnGuichet()) {
                        GuichetIG guichetIG1 = (GuichetIG) succ;
                        act.ajouterSuccesseur(new Guichet(guichetIG1.getNom(), guichetIG1.getNbJetons()));
                    } else if(succ.estUneActivite()){
                        ActiviteIG activiteIG1 = (ActiviteIG) succ;
                        act.ajouterSuccesseur(new Activite(activiteIG1.getNom(), activiteIG1.getTemps(), activiteIG1.getEcartTemps()));
                    }
                }
                mondeSim.ajouter(act);
            } else if(etapeIG.estUnGuichet()) { // case we have guichet
                GuichetIG guichetIG = (GuichetIG) etapeIG;
                Guichet guichet = new Guichet(guichetIG.getNom(), guichetIG.getNbJetons());
                correspondances.ajouter(guichetIG, guichet);
                if(guichetIG.estUneEntree()) { // checks for being first/last etape
                    mondeSim.aCommeEntree(guichet);
                } else if(guichetIG.estUneSortie()) {
                    mondeSim.aCommeSortie(guichet);
                }
                for(EtapeIG succ : guichetIG.getSuccesseurs()) { // adding next steps depending on their type
                    if(succ.estUnGuichet()) {
                        GuichetIG guichetIG1 = (GuichetIG) succ;
                        guichet.ajouterSuccesseur(new Guichet(guichetIG1.getNom(), guichetIG1.getNbJetons()));
                    } else if(succ.estUneActivite()){
                        ActiviteIG activiteIG1 = (ActiviteIG) succ;
                        guichet.ajouterSuccesseur(new Activite(activiteIG1.getNom(), activiteIG1.getTemps(), activiteIG1.getEcartTemps()));
                    }
                }
                mondeSim.ajouter(guichet);
            }
        }
        return mondeSim;
    }

    public void simuler() throws MondeException, InvalidNameException {
        this.verifierMondeIG();
        Monde mondeSim = creerMonde();
        Simulation sim = new Simulation();
        sim.simuler(mondeSim);
    }
}
