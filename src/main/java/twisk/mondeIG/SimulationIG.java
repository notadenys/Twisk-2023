package twisk.mondeIG;

import twisk.exceptions.MondeException;
import twisk.monde.*;
import twisk.outils.CorrespondancesEtapes;
import twisk.outils.FabriqueNumero;
import twisk.simulation.Simulation;

import javax.naming.InvalidNameException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
public class SimulationIG {
    private final MondeIG monde;
    private CorrespondancesEtapes correspondances;
    private GestionnaireEtapes gestionnaireEtapes;

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
        this.correspondances = new CorrespondancesEtapes();
        this.gestionnaireEtapes = new GestionnaireEtapes();

        System.out.println("Initial state of monde: " + this.monde);
        Iterator<Entry<Integer, EtapeIG>> iter = this.monde.entrySet().iterator();
        System.out.println("Gestionnaire :" + gestionnaireEtapes.toString());

        while (iter.hasNext()) {
            Entry<Integer, EtapeIG> entry = iter.next();
            EtapeIG etapeIG = entry.getValue();
            System.out.println("Processing EtapeIG: " + etapeIG.getNom());
            if (etapeIG.estUneActivite()) {
                ActiviteIG actIG = (ActiviteIG) etapeIG;
                Activite act;
                if (actIG.isRestrainte()) {
                    act = new ActiviteRestreinte(actIG.getNom(), actIG.getTemps(), actIG.getEcartTemps());
                } else {
                    act = new Activite(actIG.getNom(), actIG.getTemps(), actIG.getEcartTemps());
                }
                correspondances.ajouter(actIG, act);
                gestionnaireEtapes.ajouter(act);
                System.out.println("Gestionnaire :" + gestionnaireEtapes.toString());
                if (actIG.estUneEntree()) {
                    mondeSim.aCommeEntree(act);
                } else if (actIG.estUneSortie()) {
                    mondeSim.aCommeSortie(act);
                }
                Set<EtapeIG> uniqueSuccessors = new HashSet<>(actIG.getSuccesseurs()); // Ensure successors are only added once
                for (EtapeIG succ : uniqueSuccessors) {
                    System.out.println("Processing successor of " + actIG.getNom() + ": " + succ.getNom() + " id: " + succ.getId() +" act id: "+ act.getNum());
                    try {
                        Etape etapeSuccesseur = gestionnaireEtapes.getEtapeByID(succ.getId());
                        act.ajouterSuccesseur(etapeSuccesseur);
                    } catch (NoSuchElementException e) {
                        System.err.println("Error adding successor for " + actIG.getNom() + ": " + e.getMessage());
                    }
                }
                mondeSim.ajouter(act);
            } else if (etapeIG.estUnGuichet()) {
                GuichetIG guichetIG = (GuichetIG) etapeIG;
                Guichet guichet = new Guichet(guichetIG.getNom(), guichetIG.getNbJetons());
                correspondances.ajouter(guichetIG, guichet);

                if (guichetIG.estUneEntree()) {
                    mondeSim.aCommeEntree(guichet);
                } else if (guichetIG.estUneSortie()) {
                    mondeSim.aCommeSortie(guichet);
                }
                Set<EtapeIG> uniqueSuccessors = new HashSet<>(guichetIG.getSuccesseurs());
                for (EtapeIG succ : uniqueSuccessors) {
                    System.out.println("Processing successor of " + guichetIG.getNom() + ": " + succ.getNom());
                    try {
                        Etape etapeSuccesseur = gestionnaireEtapes.getEtapeByID(succ.getId());
                        guichet.ajouterSuccesseur(etapeSuccesseur);
                    } catch (NoSuchElementException e) {
                        System.err.println("Error adding successor for " + guichetIG.getNom() + ": " + e.getMessage());
                    }
                }
                mondeSim.ajouter(guichet);
            }
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
