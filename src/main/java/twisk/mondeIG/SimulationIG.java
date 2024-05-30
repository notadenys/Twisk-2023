package twisk.mondeIG;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
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

public class SimulationIG implements Observateur {
    private final MondeIG monde;
    private volatile boolean interrupted; // flag to indicate if the simulation should be interrupted

    public SimulationIG(MondeIG mondeIG) {
        monde = mondeIG;
        interrupted = false;
    }

    // verifies the world for all constraints
    private void verifierMondeIG() {
        // check disconnected steps
        for (EtapeIG etape : monde.getEtapes()) {
            if (etape.getPredecesseurs().isEmpty() && !etape.estUneEntree()) {
                showErrorAlert("Erreur de connexion", "Etape " + etape.getNom() + " est deconnectee (pas de predecesseurs)");
                interruptSimulation();
                return;
            }
            if (etape.getSuccesseurs().isEmpty() && !etape.estUneSortie()) {
                showErrorAlert("Erreur de connexion", "Etape " + etape.getNom() + " est deconnectee (pas de successeurs)");
                interruptSimulation();
                return;
            }
        }

        // check if there are no entries
        if (monde.getEntrees().isEmpty()) {
            showErrorAlert("Erreur de simulation", "Il n'y a aucun entrée définie");
            interruptSimulation();
            return;
        }

        // check if there are no exits
        if (monde.getSorties().isEmpty()) {
            showErrorAlert("Erreur de simulation", "Il n'y a aucune sortie définie");
            interruptSimulation();
            return;
        }

        // check guichets for incorrect number of successors
        for (GuichetIG guichet : monde.getGuichets()) {
            if (guichet.getSuccesseurs().isEmpty()) {
                showErrorAlert("Erreur de simulation", "Guichet " + guichet.getNom() + " n'a pas de successeurs");
                interruptSimulation();
                return;
            }
            if (guichet.getSuccesseurs().size() > 1) {
                showErrorAlert("Erreur de simulation", "Guichet " + guichet.getNom() + " a plus que 1 successeur");
                interruptSimulation();
                return;
            }
            if (guichet.getSuccesseurs().get(0).estUnGuichet()) {
                showErrorAlert("Erreur de simulation", "Guichet " + guichet.getNom() + " a un guichet comme successeur");
                interruptSimulation();
                return;
            }
        }

        // update restrained activities and check if they have only 1 predecessor
        for (GuichetIG guichet : monde.getGuichets()) {
            ActiviteIG activite = (ActiviteIG) guichet.getSuccesseurs().get(0);
            activite.setRestrainte(true);
            if (activite.getPredecesseurs().size() > 1) {
                showErrorAlert("Erreur de simulation", "Activite " + activite.getNom() + " a plus que 1 predecesseur");
                interruptSimulation();
                return;
            }
        }

        // check if any steps have empty names
        for (EtapeIG etape : monde.getEtapes()) {
            if (etape.getNom().isEmpty()) {
                showErrorAlert("Erreur de simulation", "Un ou plusieurs étapes ont des noms vides");
                interruptSimulation();
                return;
            }
        }

        // check if any activities have duplicate names
        ArrayList<String> noms = new ArrayList<>();
        for (ActiviteIG activite : monde.getActivites()) {
            if (noms.contains(activite.getNom())) {
                showErrorAlert("Erreur de simulation", "Etape " + activite.getNom() + " a un doublon");
                interruptSimulation();
                return;
            }
            noms.add(activite.getNom());
        }

        // check if any guichets have duplicate names
        noms = new ArrayList<>();
        for (GuichetIG guichet : monde.getGuichets()) {
            if (noms.contains(guichet.getNom())) {
                showErrorAlert("Erreur de simulation", "Etape " + guichet.getNom() + " a un doublon");
                interruptSimulation();
                return;
            }
            noms.add(guichet.getNom());
        }
    }

    private Monde creerMonde() {
        verifierMondeIG();
        if (interrupted) {
            return null; // return null if the simulation was interrupted during verification
        }
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
        while (iterLiason.hasNext()) {
            EtapeIG[] liaison = iterLiason.next();
            Etape etape1 = correspondances.getEtape(liaison[0]);
            Etape etape2 = correspondances.getEtape(liaison[1]);
            etape1.ajouterSuccesseur(etape2);
        }
        this.monde.setCorrespondance(correspondances);
        return mondeSim;
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void interruptSimulation() {
        interrupted = true; // set the interruption flag
        monde.setSimulationInProgress(new MutableBoolean(false)); // indicate that the simulation is not in progress
        ThreadsManager.getInstance().detruireTout(); // destroy all running threads
        monde.notifierObservateurs(); // notify observers to update the UI
    }

    public void simuler() throws MondeException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Monde mondeSim = creerMonde(); // create the simulation world

        if (interrupted || mondeSim == null) {
            System.out.println("Simulation interrupted or mondeSim is null");
            return; // exit if the simulation was interrupted during the world creation phase
        }

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
                Method getInProgress = cl.getMethod("getInProgress");
                monde.setSimulationInProgress((MutableBoolean) getInProgress.invoke(o));

                Method simuler = cl.getMethod("simuler", Monde.class);
                simuler.invoke(o, mondeSim);
                return null;
            }
        };

        ThreadsManager.getInstance().lancer(task);
    }

    @Override
    public void reagir() {
        System.out.println(monde.isSimulationInProgress());
        monde.notifierObservateurs();
    }
}
