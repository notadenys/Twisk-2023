package twisk.mondeIG;

import twisk.exceptions.ArcException;
import twisk.exceptions.MondeException;
import twisk.outils.CorrespondancesEtapes;
import twisk.outils.MutableBoolean;
import twisk.outils.TailleComposants;
import twisk.simulation.GestionnaireClients;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * The MondeIG class represents the model of the interactive graphical world in the Twisk application.
 * It manages the steps (etapes), arcs, and the state of the simulation.
 */
public class MondeIG extends SujetObserve implements Iterable<Map.Entry<Integer, EtapeIG>> {
    private final Map<Integer, EtapeIG> etapes;
    private final ArrayList<ArcIG> arcs;
    private final ArrayList<EtapeIG> etapesSelectionnes;
    private final ArrayList<ArcIG> arcsSelectionnes;
    private PointDeControleIG pointMemorise;
    private boolean isEnAttente;
    private final ArrayList<EtapeIG[]> liaisons;
    private GestionnaireClients gestionnaireClients;
    private MutableBoolean simulationInProgress;
    private int nbClients;

    /**
     * Constructs a new MondeIG with initial parameters.
     */
    public MondeIG() {
        simulationInProgress = new MutableBoolean();
        etapes = new HashMap<>();
        arcs = new ArrayList<>();
        isEnAttente = false;
        etapesSelectionnes = new ArrayList<>();
        arcsSelectionnes = new ArrayList<>();
        liaisons = new ArrayList<>();
        gestionnaireClients = new GestionnaireClients();
        nbClients = 7;
    }

    /**
     * Adds an activity to the world.
     * @param activite the activity to add.
     */
    public void ajouter(ActiviteIG activite) {
        etapes.put(activite.getId(), activite);
        notifierObservateurs();
    }

    /**
     * Adds a guichet to the world.
     * @param guichet the guichet to add.
     */
    public void ajouter(GuichetIG guichet) {
        etapes.put(guichet.getId(), guichet);
        notifierObservateurs();
    }

    /**
     * Deletes the specified steps from the world.
     * @param etapes the steps to delete.
     */
    public void delete(EtapeIG... etapes) {
        for (EtapeIG etapeIG : etapes) {
            this.etapes.remove(etapeIG.getId());
            etapesSelectionnes.remove(etapeIG);
            checkArcs(etapeIG);
        }
        notifierObservateurs();
    }

    /**
     * Marks the selected steps as entry points.
     */
    public void marquerCommeEntree() {
        for (EtapeIG etape : etapesSelectionnes) {
            etape.setEntree(!etape.estUneEntree());
        }
        notifierObservateurs();
    }

    /**
     * Marks the selected steps as exit points.
     */
    public void marquerCommeSortie() {
        for (EtapeIG etape : etapesSelectionnes) {
            etape.setSortie(!etape.estUneSortie());
        }
        notifierObservateurs();
    }

    /**
     * Gets all the steps in the world.
     * @return a list of all steps.
     */
    public ArrayList<EtapeIG> getEtapes() {
        return new ArrayList<>(etapes.values());
    }

    /**
     * Gets all the entry points in the world.
     * @return a list of all entry points.
     */
    public ArrayList<EtapeIG> getEntrees() {
        ArrayList<EtapeIG> entrees = new ArrayList<>();
        for (EtapeIG etape : etapes.values()) {
            if (etape.estUneEntree()) {
                entrees.add(etape);
            }
        }
        return entrees;
    }

    /**
     * Gets all the exit points in the world.
     * @return a list of all exit points.
     */
    public ArrayList<EtapeIG> getSorties() {
        ArrayList<EtapeIG> sorties = new ArrayList<>();
        for (EtapeIG etape : etapes.values()) {
            if (etape.estUneSortie()) {
                sorties.add(etape);
            }
        }
        return sorties;
    }

    /**
     * Gets all the guichets in the world.
     * @return a list of all guichets.
     */
    public ArrayList<GuichetIG> getGuichets() {
        ArrayList<GuichetIG> guichets = new ArrayList<>();
        for (EtapeIG etape : etapes.values()) {
            if (etape.estUnGuichet()) {
                guichets.add((GuichetIG) etape);
            }
        }
        return guichets;
    }

    /**
     * Gets all the activities in the world.
     * @return a list of all activities.
     */
    public ArrayList<ActiviteIG> getActivites() {
        ArrayList<ActiviteIG> activites = new ArrayList<>();
        for (EtapeIG etape : etapes.values()) {
            if (etape.estUneActivite()) {
                activites.add((ActiviteIG) etape);
            }
        }
        return activites;
    }

    /**
     * Adds an arc between two control points.
     * @param point1 the ID of the first control point.
     * @param point2 the ID of the second control point.
     * @throws ArcException if the arc cannot be added due to constraints.
     */
    public void ajouter(String point1, String point2) throws ArcException {
        PointDeControleIG p1 = getPoint(point1);
        PointDeControleIG p2 = getPoint(point2);

        if (p1.getEtape() == p2.getEtape() && p1 != p2) {
            throw new ArcException("Impossible d'ajouter l'arc sur le meme etape");
        } else if (p1.getEtape().getSuccesseurs().contains(p2.getEtape()) || p2.getEtape().getSuccesseurs().contains(p1.getEtape())) {
            throw new ArcException("Impossible d'ajouter l'arc sur etape : deja connecte");
        } else if (p1 == p2) {
            setEnAttente(false);
            notifierObservateurs();
        } else if (estAccessibleDepuis(p1.getEtape(), p2.getEtape())) {
            throw new ArcException("Impossible d'ajouter l'arc : cela créerait une boucle");
        } else {
            arcs.add(new ArcIG(this, point1, point2));
            setEnAttente(false);
            notifierObservateurs();
        }
        updateLiaison();
    }

    /**
     * Deletes the specified arcs from the world.
     * @param arcs the arcs to delete.
     */
    public void delete(ArcIG... arcs) {
        for (ArcIG arcIG : arcs) {
            this.arcs.remove(arcIG);
            arcsSelectionnes.remove(arcIG);
            arcIG.getP1().getEtape().supprimerSuccesseur(arcIG.getP2().getEtape());
            arcIG.getP2().getEtape().supprimerPredecesseur(arcIG.getP1().getEtape());
            arcIG.getP1().setSortie(false);
        }
        notifierObservateurs();
        updateLiaison();
    }

    /**
     * Checks and removes arcs connected to the specified step.
     * @param etape the step to check.
     */
    public void checkArcs(EtapeIG etape) {
        ArrayList<ArcIG> arcsToRemove = new ArrayList<>() {};
        for (ArcIG arc : arcs) {
            if (arc.getP1().getEtape() == etape || arc.getP2().getEtape() == etape) {
                arcsToRemove.add(arc);
            }
        }
        delete(arcsToRemove.toArray(new ArcIG[0]));
    }

    /**
     * Refreshes the arcs in the world.
     */
    public void refreshArcs() {
        ArrayList<ArcIG> arcs = new ArrayList<>();
        while (!this.arcs.isEmpty()) {
            arcs.add(new ArcIG(this, this.arcs.get(0).getP1().getId(), this.arcs.get(0).getP2().getId()));
            this.arcs.remove(0);
        }
        this.arcs.addAll(arcs);
    }

    /**
     * Handles a click event on a step.
     * @param etapeIG the step that was clicked.
     */
    public void clickEtape(EtapeIG etapeIG) {
        if (!etapesSelectionnes.contains(etapeIG)) {
            selectionnerEtape(etapeIG);
        } else {
            deselectionnerEtape(etapeIG);
        }
        notifierObservateurs();
    }

    /**
     * Selects a step.
     * @param etapeIG the step to select.
     */
    public void selectionnerEtape(EtapeIG etapeIG) {
        etapesSelectionnes.add(etapeIG);
    }

    /**
     * Deselects a step.
     * @param etapeIG the step to deselect.
     */
    public void deselectionnerEtape(EtapeIG etapeIG) {
        etapesSelectionnes.remove(etapeIG);
    }

    /**
     * Handles a click event on an arc.
     * @param arcIG the arc that was clicked.
     */
    public void clickArc(ArcIG arcIG) {
        if (!contains(arcIG)) {
            selectionnerArc(arcIG);
        } else {
            deselectionnerArc(arcIG);
        }
        notifierObservateurs();
    }

    /**
     * Checks if an arc is already selected.
     * @param arc the arc to check.
     * @return true if the arc is selected, false otherwise.
     */
    public boolean contains(ArcIG arc) {
        for (ArcIG arcIG : arcsSelectionnes) {
            if (arc.getP1ID().equals(arcIG.getP1ID()) && arc.getP2ID().equals(arcIG.getP2ID()))
                return true;
        }
        return false;
    }

    /**
     * Selects an arc.
     * @param arcIG the arc to select.
     */
    public void selectionnerArc(ArcIG arcIG) {
        arcsSelectionnes.add(arcIG);
    }

    /**
     * Deselects an arc.
     * @param arcIG the arc to deselect.
     */
    public void deselectionnerArc(ArcIG arcIG) {
        arcsSelectionnes.removeIf(arc -> arc.getP1ID().equals(arcIG.getP1ID()) && arc.getP2ID().equals(arcIG.getP2ID()));
    }

    /**
     * Deselects all steps and arcs.
     */
    public void deselectionner() {
        arcsSelectionnes.clear();
        etapesSelectionnes.clear();
        notifierObservateurs();
    }

    /**
     * Gets the selected steps.
     * @return a list of selected steps.
     */
    public ArrayList<EtapeIG> getEtapesSelectionnes() {
        return etapesSelectionnes;
    }

    /**
     * Checks if a guichet is selected.
     * @return true if a guichet is selected, false otherwise.
     */
    public boolean isGuichetSelectionne() {
        for (EtapeIG etape : etapesSelectionnes) {
            if (etape.estUnGuichet()) return true;
        }
        return false;
    }

    /**
     * Checks if an activity is selected.
     * @return true if an activity is selected, false otherwise.
     */
    public boolean isActiviteSelectionne() {
        for (EtapeIG etape : etapesSelectionnes) {
            if (etape.estUneActivite()) return true;
        }
        return false;
    }

    /**
     * Gets the selected arcs.
     * @return a list of selected arcs.
     */
    public ArrayList<ArcIG> getArcsSelectionnes() {
        return arcsSelectionnes;
    }

    /**
     * Gets the memorized control point.
     * @return the memorized control point.
     */
    public PointDeControleIG getPointMemorise() {
        return pointMemorise;
    }

    /**
     * Sets the memorized control point.
     * @param pointMemorise the control point to memorize.
     */
    public void setPointMemorise(PointDeControleIG pointMemorise) {
        this.pointMemorise = pointMemorise;
    }

    /**
     * Checks if the system is waiting for some user action.
     * @return true if the system is waiting, false otherwise.
     */
    public boolean isEnAttente() {
        return isEnAttente;
    }

    /**
     * Sets the waiting state of the system.
     * @param isEnAttente the new waiting state.
     */
    public void setEnAttente(boolean isEnAttente) {
        this.isEnAttente = isEnAttente;
    }

    /**
     * Gets a control point by its ID.
     * @param id the ID of the control point.
     * @return the control point.
     */
    public PointDeControleIG getPoint(String id) {
        int etapeID = Integer.parseInt(id.replaceAll("[^0-9]", ""));
        return getEtape(etapeID).getPoint(id);
    }

    /**
     * Gets a step by its ID.
     * @param id the ID of the step.
     * @return the step.
     */
    public EtapeIG getEtape(int id) {
        return etapes.get(id);
    }

    /**
     * Returns an iterator over the entries in the world.
     * @return an iterator over the entries.
     */
    public Iterator<Map.Entry<Integer, EtapeIG>> iterator() {
        return etapes.entrySet().iterator();
    }

    /**
     * Returns an iterator over the arcs in the world.
     * @return an iterator over the arcs.
     */
    public Iterator<ArcIG> arcs() {
        return arcs.iterator();
    }

    /**
     * Starts a simulation.
     * @throws MondeException if there is an error in the world setup.
     * @throws ClassNotFoundException if a required class is not found.
     * @throws InvocationTargetException if there is an error invoking a method.
     * @throws NoSuchMethodException if a required method is not found.
     * @throws InstantiationException if a required class cannot be instantiated.
     * @throws IllegalAccessException if there is an illegal access to a method.
     */
    public void simuler() throws MondeException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        SimulationIG sim = new SimulationIG(this);
        sim.simuler();
    }

    /**
     * Returns a set view of the entries in the world.
     * @return a set view of the entries.
     */
    public Set<Map.Entry<Integer, EtapeIG>> entrySet() {
        return this.etapes.entrySet();
    }

    /**
     * Updates the list of connections (liaisons) between steps.
     */
    public void updateLiaison() {
        liaisons.clear();
        for (ArcIG arc : this.arcs) {
            EtapeIG[] liaison = {arc.getP1().getEtape(), arc.getP2().getEtape()};
            this.liaisons.add(liaison);
        }
    }

    /**
     * Gets the client manager.
     * @return the client manager.
     */
    public GestionnaireClients getGestionnaireClients() {
        return gestionnaireClients;
    }

    /**
     * Sets the client manager.
     * @param gestionnaireClients the new client manager.
     */
    public void setGestionnaireClients(GestionnaireClients gestionnaireClients) {
        this.gestionnaireClients = gestionnaireClients;
    }

    /**
     * Checks if the simulation is stopped.
     * @return true if the simulation is stopped, false otherwise.
     */
    public boolean isSimulationStopped() {
        return !simulationInProgress.getValue();
    }

    /**
     * Sets the state of the simulation.
     * @param simulationInProgress the new simulation state.
     */
    public void setSimulationInProgress(MutableBoolean simulationInProgress) {
        this.simulationInProgress = simulationInProgress;
    }

    /**
     * Returns an iterator over the connections (liaisons) between steps.
     * @return an iterator over the connections.
     */
    public Iterator<EtapeIG[]> iteratorliaison() {
        return this.liaisons.iterator();
    }

    @Override
    public String toString() {
        return "MondeIG{" +
                "etapes=" + etapes +
                '}';
    }

    /**
     * Sets the correspondence of steps.
     * @param correspondance the correspondence to set.
     */
    public void setCorrespondance(CorrespondancesEtapes correspondance) {
    }

    /**
     * Checks if a step is accessible from another step.
     * @param candidat the candidate step.
     * @param racine the root step.
     * @return true if the candidate step is accessible from the root step, false otherwise.
     */
    public boolean estAccessibleDepuis(EtapeIG candidat, EtapeIG racine) {
        Set<EtapeIG> visited = new HashSet<>();
        return dfs(racine, candidat, visited);
    }

    /**
     * Performs a depth-first search to check accessibility.
     * @param current the current step.
     * @param candidat the candidate step.
     * @param visited the set of visited steps.
     * @return true if the candidate step is accessible, false otherwise.
     */
    public boolean dfs(EtapeIG current, EtapeIG candidat, Set<EtapeIG> visited) {
        if (current == candidat) {
            return true;
        }
        visited.add(current);
        for (EtapeIG successor : current.getSuccesseurs()) {
            if (!visited.contains(successor)) {
                if (dfs(successor, candidat, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sets the number of clients for the simulation.
     * @param nbClients the number of clients.
     * @throws MondeException if the number of clients is too high.
     */
    public void setNbClients(int nbClients) throws MondeException {
        if (nbClients > TailleComposants.getInstance().getClientsW() / 10) {
            throw new MondeException("Trop de clients");
        } else {
            this.nbClients = nbClients;
        }
    }

    /**
     * Gets the number of clients for the simulation.
     * @return the number of clients.
     */
    public int getNbClients() {
        return nbClients;
    }
}
