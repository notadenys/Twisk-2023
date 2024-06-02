package twisk.mondeIG;

import javafx.concurrent.Task;
import twisk.exceptions.MondeException;
import twisk.monde.*;
import twisk.outils.*;
import twisk.simulation.GestionnaireClients;
import twisk.vues.Observateur;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

/**
 * The SimulationIG class is responsible for verifying the world configuration and running the simulation.
 */
public class SimulationIG implements Observateur {
    private final MondeIG monde;

    /**
     * Constructs a SimulationIG with the given MondeIG.
     *
     * @param mondeIG the world to simulate.
     */
    public SimulationIG(MondeIG mondeIG) {
        monde = mondeIG;
    }

    /**
     * Verifies the world configuration to ensure all constraints are met.
     *
     * @throws MondeException if the world configuration is invalid.
     */
    private void verifierMondeIG() throws MondeException {
        if (monde.getEntrees().isEmpty()) throw new MondeException("Il n'y a aucun entree defini");

        for (EtapeIG etape : monde.getEntrees()) {
            if (!etape.getPredecesseurs().isEmpty()) {
                throw new MondeException("Erreur de connexion. Entree " + etape.getNom() + " a des predecesseurs");
            }
        }

        if (monde.getSorties().isEmpty()) throw new MondeException("Il n'y a aucun sortie defini");

        for (EtapeIG etape : monde.getEtapes()) {
            if (etape.getPredecesseurs().isEmpty() && !etape.estUneEntree())
                throw new MondeException("Etape " + etape.getNom() + " est deconnectee(pas de predecesseurs)");

            if (etape.getSuccesseurs().isEmpty() && !etape.estUneSortie())
                throw new MondeException("Etape " + etape.getNom() + " est deconnectee(pas de successeurs)");
        }

        for (EtapeIG etape : monde.getSorties()) {
            if (!etape.getSuccesseurs().isEmpty()) {
                throw new MondeException("Erreur de connexion. Sortie " + etape.getNom() + " a des successeurs");
            }
        }

        for (GuichetIG guichet : monde.getGuichets()) {
            if (guichet.getSuccesseurs().isEmpty())
                throw new MondeException("Guichet " + guichet.getNom() + " n'a pas de successeurs");
            if (guichet.getSuccesseurs().size() > 1)
                throw new MondeException("Guichet " + guichet.getNom() + " a plus que 1 successeur");
            if (guichet.getSuccesseurs().get(0).estUnGuichet())
                throw new MondeException("Guichet " + guichet.getNom() + " a un guichet comme successeur");
        }

        for (GuichetIG guichet : monde.getGuichets()) {
            ActiviteIG activite = (ActiviteIG) guichet.getSuccesseurs().get(0);
            activite.setRestrainte(true);
            if (activite.getPredecesseurs().size() > 1)
                throw new MondeException("Activite " + activite.getNom() + " a plus que 1 predecesseur");
        }

        for (EtapeIG etape : monde.getEtapes()) {
            if (etape.getNom().isEmpty()) throw new MondeException("Un ou plusieurs etapes ont les noms vides");
        }

        ArrayList<String> noms = new ArrayList<>();
        for (ActiviteIG activite : monde.getActivites()) {
            if (noms.contains(activite.getNom()))
                throw new MondeException("Etape " + activite.getNom() + " a un doublon");
            noms.add(activite.getNom());
        }

        noms = new ArrayList<>();
        for (GuichetIG guichet : monde.getGuichets()) {
            if (noms.contains(guichet.getNom()))
                throw new MondeException("Etape " + guichet.getNom() + " a un doublon");
            noms.add(guichet.getNom());
        }
    }

    /**
     * Creates a Monde instance based on the current MondeIG.
     *
     * @return a new Monde instance.
     * @throws MondeException if the world configuration is invalid.
     */
    private Monde creerMonde() throws MondeException {
        verifierMondeIG();
        FabriqueNumero.getInstance().reset();
        Monde mondeSim = new Monde();
        CorrespondancesEtapes correspondances = new CorrespondancesEtapes();
        GestionnaireEtapes gestionnaireEtapes = new GestionnaireEtapes();

        for (Entry<Integer, EtapeIG> entry : this.monde.entrySet()) {
            EtapeIG etapeIG = entry.getValue();
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
                if (actIG.estUneEntree()) {
                    mondeSim.aCommeEntree(act);
                }
                if (actIG.estUneSortie()) {
                    mondeSim.aCommeSortie(act);
                }
                mondeSim.ajouter(act);
            } else if (etapeIG.estUnGuichet()) {
                GuichetIG guichetIG = (GuichetIG) etapeIG;
                Guichet guichet = new Guichet(guichetIG.getNom(), guichetIG.getNbJetons());
                correspondances.ajouter(guichetIG, guichet);
                if (guichetIG.estUneEntree()) {
                    mondeSim.aCommeEntree(guichet);
                }
                mondeSim.ajouter(guichet);
            }
        }

        Iterator<EtapeIG[]> iterLiason = monde.iteratorliaison();
        while (iterLiason.hasNext()) {
            EtapeIG[] liaison = iterLiason.next();
            Etape etape1 = correspondances.getEtape(liaison[0]);
            Etape etape2 = correspondances.getEtape(liaison[1]);
            etape1.ajouterSuccesseur(etape2);
        }

        this.monde.setCorrespondance(correspondances);
        return mondeSim;
    }

    /**
     * Starts the simulation.
     *
     * @throws MondeException            if the world configuration is invalid.
     * @throws ClassNotFoundException    if a required class is not found.
     * @throws NoSuchMethodException     if a required method is not found.
     * @throws InvocationTargetException if there is an error invoking a method.
     * @throws InstantiationException    if a required class cannot be instantiated.
     * @throws IllegalAccessException    if there is an illegal access to a method.
     */
    public void simuler() throws MondeException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        monde.deselectionner();
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
                setNbClients.invoke(o, monde.getNbClients());
                Method ajouterObservateur = cl.getMethod("ajouterObservateur", Observateur.class);
                ajouterObservateur.invoke(o, sIG);
                Method getGestionnaireClients = cl.getMethod("getGestionnaireClients");
                monde.setGestionnaireClients((GestionnaireClients) getGestionnaireClients.invoke(o));
                Method getInProgress = cl.getMethod("getInProgress");
                monde.setSimulationInProgress((MutableBoolean) getInProgress.invoke(o));

                Method simuler = cl.getMethod("simuler", Monde.class);
                simuler.invoke(o, mondeSim);
                return null;
            }
        };
        ThreadsManager.getInstance().lancer(task);
    }

    /**
     * Reacts to changes by notifying observers.
     */
    @Override
    public void reagir() {
        monde.notifierObservateurs();
    }
}
