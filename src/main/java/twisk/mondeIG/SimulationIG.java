package twisk.mondeIG;

import twisk.exceptions.MondeException;
import twisk.monde.*;
import twisk.outils.CorrespondancesEtapes;
import twisk.outils.FabriqueNumero;
import twisk.simulation.Simulation;
import java.util.*;
import java.util.Map.Entry;

public class SimulationIG {
    private final MondeIG monde;
    public SimulationIG(MondeIG mondeIG) {
        monde = mondeIG;
    }

    // verifies the world to meet all the constraints
    private void verifierMondeIG() throws MondeException {
        // etapes deconnectees
        for (EtapeIG etape : monde.getEtapes())
        {
            if (etape.getPredecesseurs().isEmpty() && !etape.estUneEntree())
                throw new MondeException("Etape " + etape.getNom() + " est deconnectee(pas de predecesseurs)");

            if (etape.getSuccesseurs().isEmpty() && !etape.estUneSortie())
                throw new MondeException("Etape " + etape.getNom() + " est deconnectee(pas de successeurs");
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
        CorrespondancesEtapes correspondances = new CorrespondancesEtapes();
        GestionnaireEtapes gestionnaireEtapes = new GestionnaireEtapes();

        System.out.println("Initial state of monde: " + this.monde);
        Iterator<Entry<Integer, EtapeIG>> iter = this.monde.entrySet().iterator();
        System.out.println("Gestionnaire :" + gestionnaireEtapes);

        while (iter.hasNext()) { // iterating through all etaps of MondeIG
            Entry<Integer, EtapeIG> entry = iter.next();
            EtapeIG etapeIG = entry.getValue();
            System.out.println("Processing EtapeIG: " + etapeIG.getNom());
            if (etapeIG.estUneActivite()) { // case we have activity
                ActiviteIG actIG = (ActiviteIG) etapeIG;
                Activite act;
                if (actIG.isRestrainte()) { // checks activity for being Restrainte
                    act = new ActiviteRestreinte(actIG.getNom(), actIG.getTemps(), actIG.getEcartTemps());
                } else {
                    act = new Activite(actIG.getNom(), actIG.getTemps(), actIG.getEcartTemps());
                }
                correspondances.ajouter(actIG, act);
                gestionnaireEtapes.ajouter(act);
                System.out.println("Gestionnaire :" + gestionnaireEtapes);
                if (actIG.estUneEntree()) { // checks for being first/last etape
                    mondeSim.aCommeEntree(act);
                } else if (actIG.estUneSortie()) {
                    mondeSim.aCommeSortie(act);
                }
                mondeSim.ajouter(act);
            } else if (etapeIG.estUnGuichet()) { // case we have guichet
                GuichetIG guichetIG = (GuichetIG) etapeIG;
                Guichet guichet = new Guichet(guichetIG.getNom(), guichetIG.getNbJetons());
                correspondances.ajouter(guichetIG, guichet);
                if (guichetIG.estUneEntree()) { // checks for being first/last etape
                    mondeSim.aCommeEntree(guichet);
                } else if (guichetIG.estUneSortie()) {
                    mondeSim.aCommeSortie(guichet);
                }
                mondeSim.ajouter(guichet);
            }
        }
        Iterator<EtapeIG[]> iterLiason = monde.iteratorliaison(); // adding next steps
        while(iterLiason.hasNext()) {
            EtapeIG[] liaison = iterLiason.next();
            Etape etape1 = correspondances.get(liaison[0]);
            Etape etape2 = correspondances.get(liaison[1]);
            etape1.ajouterSuccesseur(etape2);
        }
        return mondeSim;
    }


    public void simuler() throws MondeException {
        this.verifierMondeIG();
        Monde mondeSim = creerMonde();
        Simulation sim = new Simulation();
        sim.simuler(mondeSim);
    }
}
