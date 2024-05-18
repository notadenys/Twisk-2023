package twisk.mondeIG;

import twisk.exceptions.MondeException;
import twisk.monde.Activite;
import twisk.monde.ActiviteRestreinte;
import twisk.monde.Guichet;
import twisk.monde.Monde;
import twisk.outils.CorrespondancesEtapes;
import twisk.outils.FabriqueNumero;

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

    private Monde creerMonde() throws InvalidNameException {
        FabriqueNumero.getInstance().reset();
        Monde mondeSim = new Monde();
        this.correspondances = new CorrespondancesEtapes();
        Iterator<Entry<Integer, EtapeIG>> iter = this.monde.iterator();
        while (this.monde.iterator().hasNext()) {
            EtapeIG etapeIG = iter.next().getValue();
            if(etapeIG.estUneActivite()) {
                ActiviteIG actIG = (ActiviteIG) etapeIG;
                Activite act = null;
                if(actIG.isRestrainte()) {
                    act = new ActiviteRestreinte(actIG.getNom(), actIG.getTemps(), actIG.getEcartTemps());
                }
                else {
                    act = new Activite(actIG.getNom(), actIG.getTemps(), actIG.getEcartTemps());
                }
                correspondances.ajouter(actIG, act);
                mondeSim.ajouter(act);
                if(actIG.estUneEntree()) {
                    mondeSim.aCommeEntree(act);
                }
                if(actIG.estUneSortie()) {
                    mondeSim.aCommeSortie(act);
                }
            }
            if(etapeIG.estUnGuichet()) {
                GuichetIG guichetIG = (GuichetIG) etapeIG;
                Guichet guichet = new Guichet(guichetIG.getNom(), guichetIG.getNbJetons());
                correspondances.ajouter(guichetIG, guichet);
                if(guichetIG.estUneEntree()) {
                    mondeSim.aCommeEntree(guichet);
                }
                if(guichetIG.estUneSortie()) {
                    mondeSim.aCommeSortie(guichet);
                }
            }
        }
        return mondeSim;
    }

    public void simuler() throws MondeException, InvalidNameException {
        this.verifierMondeIG();
        Monde mondeSim = creerMonde();
    }
}
