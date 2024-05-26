package twisk.mondeIG;

import javafx.concurrent.Task;
import twisk.exceptions.MondeException;
import twisk.monde.*;
import twisk.outils.ClassLoaderPerso;
import twisk.outils.CorrespondancesEtapes;
import twisk.outils.FabriqueNumero;
import twisk.outils.ThreadsManager;
import twisk.simulation.GestionnaireClients;
import twisk.vues.Observateur;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

public class SimulationIG implements Observateur {
    private final MondeIG monde;


    public SimulationIG(MondeIG mondeIG) {
        monde = mondeIG;
    }

    // verifie le monde pour repondre a toutes les contraintes
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

        // verifier si un ou plusieurs etapes ont les noms vides
        for (EtapeIG etape : monde.getEtapes()) {
            if (etape.getNom().isEmpty()) throw new MondeException("Un ou plusieurs etapes ont les noms vides");
        }

        // verifier si un ou plusieurs activites ont les memes noms
        ArrayList<String> noms = new ArrayList<>();
        for (ActiviteIG activite : monde.getActivites()) {
            if (noms.contains(activite.getNom())) throw new MondeException ("Etape " + activite.getNom() + " a un doublon");
            noms.add(activite.getNom());
        }

        // verifier si un ou plusieurs guichets ont les memes noms
        noms = new ArrayList<>();
        for (GuichetIG guichet : monde.getGuichets()) {
            if (noms.contains(guichet.getNom())) throw new MondeException ("Etape " + guichet.getNom() + " a un doublon");
            noms.add(guichet.getNom());
        }
    }

    private Monde creerMonde() throws MondeException {
        verifierMondeIG();
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
                }
                if (actIG.estUneSortie()) {
                    mondeSim.aCommeSortie(act);
                }
                mondeSim.ajouter(act);
            } else if (etapeIG.estUnGuichet()) { // case we have guichet
                GuichetIG guichetIG = (GuichetIG) etapeIG;
                Guichet guichet = new Guichet(guichetIG.getNom(), guichetIG.getNbJetons());
                correspondances.ajouter(guichetIG, guichet);
                if (guichetIG.estUneEntree()) { // checks for being first/last etape
                    mondeSim.aCommeEntree(guichet);
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

    public void simuler() throws MondeException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Monde mondeSim = creerMonde();
        System.gc();
        ClassLoaderPerso classLoader = new ClassLoaderPerso(SimulationIG.class.getClassLoader());
        Class<?> cl = classLoader.loadClass("twisk.simulation.Simulation");
        Constructor<?> constr = cl.getConstructor();
        Object o = constr.newInstance();
        SimulationIG sIG = this;
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Method setNbClients = cl.getMethod("setNbClients", int.class);
                setNbClients.invoke(o, 5);
                Method ajouterObservateur = cl.getMethod("ajouterObservateur", Observateur.class);
                ajouterObservateur.invoke(o, sIG);
                Method getGestionnaireClients = cl.getMethod("getGestionnaireClients");
                monde.setGestionnaireClients((GestionnaireClients) getGestionnaireClients.invoke(o));
                Method simuler = cl.getMethod("simuler", Monde.class);
                simuler.invoke(o, mondeSim);
                return null;
            }
        };
        ThreadsManager.getInstance().lancer(task);
    }

    @Override
    public void reagir() {
        monde.notifierObservateurs();
    }
}
