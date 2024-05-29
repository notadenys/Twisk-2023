package twisk.mondeIG;

import twisk.exceptions.ArcException;
import twisk.exceptions.MondeException;

import twisk.outils.CorrespondancesEtapes;
import twisk.outils.MutableBoolean;
import twisk.simulation.GestionnaireClients;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class MondeIG extends SujetObserve implements Iterable<Map.Entry<Integer,EtapeIG>>{
    private final Map<Integer, EtapeIG> etapes;
    private final ArrayList<ArcIG> arcs;

    private final ArrayList<EtapeIG> etapesSelectionnes;
    private final ArrayList<ArcIG> arcsSelectionnes;
    private PointDeControleIG pointMemorise;
    private boolean isEnAttente;
    private final ArrayList<EtapeIG[]> liaisons;
    private GestionnaireClients gestionnaireClients;
    private CorrespondancesEtapes correspondance;
    private SimulationIG sim;
    private MutableBoolean simulationInProgress;

    public MondeIG()
    {
        simulationInProgress = new MutableBoolean();
        etapes = new HashMap<>();
        arcs = new ArrayList<>();
        isEnAttente = false;
        etapesSelectionnes = new ArrayList<>();
        arcsSelectionnes = new ArrayList<>();
        liaisons = new ArrayList<>();
        gestionnaireClients = new GestionnaireClients();
    }

    public void ajouter(ActiviteIG activite)
    {
        etapes.put(activite.getId(), activite);
        notifierObservateurs();
    }

    public void ajouter(GuichetIG guichet)
    {
        etapes.put(guichet.getId(), guichet);
        notifierObservateurs();
    }

    public void delete(EtapeIG... etapes)
    {
        for (EtapeIG etapeIG : etapes)
        {
            this.etapes.remove(etapeIG.getId());
            etapesSelectionnes.remove(etapeIG);
            checkArcs(etapeIG);
        }
        notifierObservateurs();
    }

    public void marquerCommeEntree()
    {
        for (EtapeIG etape : etapesSelectionnes)
        {
            etape.setEntree(!etape.estUneEntree());
        }
        notifierObservateurs();
    }

    public void marquerCommeSortie()
    {
        for (EtapeIG etape : etapesSelectionnes)
        {
            etape.setSortie(!etape.estUneSortie());
        }
        notifierObservateurs();
    }

    public ArrayList<EtapeIG> getEtapes() {
        return new ArrayList<>(etapes.values());
    }

    public ArrayList<EtapeIG> getEntrees()
    {
        ArrayList<EtapeIG> entrees = new ArrayList<>();
        for (EtapeIG etape : etapes.values())
        {
            if (etape.estUneEntree())
            {
                entrees.add(etape);
            }
        }
        return entrees;
    }

    public ArrayList<EtapeIG> getSorties()
    {
        ArrayList<EtapeIG> sorties = new ArrayList<>();
        for (EtapeIG etape : etapes.values())
        {
            if (etape.estUneSortie())
            {
                sorties.add(etape);
            }
        }
        return sorties;
    }

    /**
     * @return all the guichets in the world
     */
    public ArrayList<GuichetIG> getGuichets()
    {
        ArrayList<GuichetIG> guichets = new ArrayList<>();
        for (EtapeIG etape : etapes.values())
        {
            if (etape.estUnGuichet())
            {
                guichets.add((GuichetIG) etape);
            }
        }
        return guichets;
    }

    /**
     * @return all the activities
     */
    public ArrayList<ActiviteIG> getActivites()
    {
        ArrayList<ActiviteIG> activites = new ArrayList<>();
        for (EtapeIG etape : etapes.values())
        {
            if (etape.estUneActivite())
            {
                activites.add((ActiviteIG) etape);
            }
        }
        return activites;
    }

    /**
     * @return all the limited activities
     */
    public ArrayList<ActiviteIG> getActivitesRestraintes()
    {
        ArrayList<ActiviteIG> activites = new ArrayList<>();
        for (ActiviteIG activite : getActivites())
        {
            if (activite.isRestrainte())
            {
                activites.add(activite);
            }
        }
        return activites;
    }

    /* Contraintes pour les arcs
    - Meme etape
    - Meme chemin
     */
    public void ajouter(String point1, String point2) throws ArcException {
        PointDeControleIG p1 = getPoint(point1);
        PointDeControleIG p2 = getPoint(point2);

        if (p1.getEtape() == p2.getEtape() && p1 != p2)
        {
            throw new ArcException("Impossible d'ajouter l'arc sur le meme etape");
        }
        else if (p1.getEtape().getSuccesseurs().contains(p2.getEtape()) || p2.getEtape().getSuccesseurs().contains(p1.getEtape()))
        {
            throw new ArcException("Impossible d'ajouter l'arc sur etape : deja connecte");
        }
        else if (p1 == p2)  // deuxieme click sur le point va desactiver le selection
        {
            setEnAttente(false);
            notifierObservateurs();
        }
        else
        {
            arcs.add(new ArcIG(this, point1, point2));
            setEnAttente(false);
            notifierObservateurs();
        }
        updateLiaison();
    }

    public void delete(ArcIG... arcs)
    {
        for (ArcIG arcIG : arcs)
        {
            this.arcs.remove(arcIG);
            arcsSelectionnes.remove(arcIG);
            arcIG.getP1().getEtape().supprimerSuccesseur(arcIG.getP2().getEtape());
            arcIG.getP2().getEtape().supprimerPredecesseur(arcIG.getP1().getEtape());
        }
        notifierObservateurs();
        updateLiaison();
    }

    public void checkArcs(EtapeIG etape)
    {
        ArrayList<ArcIG> arcsToRemove = new ArrayList<>() {};
        for (ArcIG arc : arcs)
        {
            if (arc.getP1().getEtape() == etape || arc.getP2().getEtape() == etape)
            {
                arcsToRemove.add(arc);
            }
        }
        delete(arcsToRemove.toArray(new ArcIG[0]));
    }

    public void refreshArcs()
    {
        ArrayList<ArcIG> arcs = new ArrayList<>();
        while (!this.arcs.isEmpty())
        {
            arcs.add(new ArcIG(this, this.arcs.get(0).getP1().getId(), this.arcs.get(0).getP2().getId()));
            this.arcs.remove(0);
        }
        this.arcs.addAll(arcs);
    }

    public void clickEtape(EtapeIG etapeIG)
    {
        if (!etapesSelectionnes.contains(etapeIG))
        {
            selectionnerEtape(etapeIG);
        }
        else
        {
            deselectionnerEtape(etapeIG);
        }
        notifierObservateurs();
    }

    public void selectionnerEtape(EtapeIG etapeIG)
    {
        etapesSelectionnes.add(etapeIG);
    }

    public void deselectionnerEtape(EtapeIG etapeIG)
    {
        etapesSelectionnes.remove(etapeIG);
    }

    public void clickArc(ArcIG arcIG)
    {
        if (!contains(arcIG))
        {
            selectionnerArc(arcIG);
        }
        else
        {
            deselectionnerArc(arcIG);
        }
        notifierObservateurs();
    }

    public boolean contains(ArcIG arc)
    {
        for (ArcIG arcIG : arcsSelectionnes)
        {
            if (arc.getP1ID().equals(arcIG.getP1ID()) && arc.getP2ID().equals(arcIG.getP2ID()))
                return true;
        }
        return false;
    }

    public void selectionnerArc(ArcIG arcIG)
    {
        arcsSelectionnes.add(arcIG);
    }

    public void deselectionnerArc(ArcIG arcIG)
    {
        arcsSelectionnes.removeIf(arc -> arc.getP1ID().equals(arcIG.getP1ID()) && arc.getP2ID().equals(arcIG.getP2ID()));
    }

    public void deselectionner()
    {
        arcsSelectionnes.clear();
        etapesSelectionnes.clear();
        notifierObservateurs();
    }

    public ArrayList<EtapeIG> getEtapesSelectionnes()
    {
        return etapesSelectionnes;
    }

    public boolean isGuichetSelectionne()
    {
        for (EtapeIG etape : etapesSelectionnes)
        {
            if (etape.estUnGuichet()) return true;
        }
        return false;
    }

    public boolean isActiviteSelectionne()
    {
        for (EtapeIG etape : etapesSelectionnes)
        {
            if (etape.estUneActivite()) return true;
        }
        return false;
    }

    public ArrayList<ArcIG> getArcsSelectionnes()
    {
        return arcsSelectionnes;
    }

    public PointDeControleIG getPointMemorise() {
        return pointMemorise;
    }
    public void setPointMemorise(PointDeControleIG pointMemorise) {
        this.pointMemorise = pointMemorise;
    }

    public boolean isEnAttente() {
        return isEnAttente;
    }
    public void setEnAttente(boolean isEnAttente) {
        this.isEnAttente = isEnAttente;
    }

    public PointDeControleIG getPoint(String id)
    {
        int etapeID = Integer.parseInt(id.replaceAll("[^0-9]", ""));
        return getEtape(etapeID).getPoint(id);
    }
    public EtapeIG getEtape(int id)
    {
        return etapes.get(id);
    }

    public Iterator<Map.Entry<Integer,EtapeIG>> iterator()
    {
        return etapes.entrySet().iterator();
    }
    public Iterator<ArcIG> arcs()
    {
        return arcs.iterator();
    }

    public void simuler() throws MondeException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        sim = new SimulationIG(this);
        sim.simuler();
    }

    public Set<Map.Entry<Integer, EtapeIG>> entrySet() {
        return this.etapes.entrySet();
    }

    public void updateLiaison() {
        liaisons.clear();
        for (ArcIG arc : this.arcs) {
            EtapeIG[] liaison = {arc.getP1().getEtape(), arc.getP2().getEtape()};
            this.liaisons.add(liaison);
        }
    }

    public GestionnaireClients getGestionnaireClients() {
        return gestionnaireClients;
    }

    public void setGestionnaireClients(GestionnaireClients gestionnaireClients) {
        this.gestionnaireClients = gestionnaireClients;
    }

    public boolean isSimulationInProgress() {
        return simulationInProgress.getValue();
    }

    public void setSimulationInProgress(MutableBoolean simulationInProgress) {
        this.simulationInProgress = simulationInProgress;
    }

    public Iterator<EtapeIG[]> iteratorliaison(){
        return this.liaisons.iterator() ;
    }

    @Override
    public String toString() {
        return "MondeIG{" +
                "etapes=" + etapes +
                '}';
    }
    public CorrespondancesEtapes getCorrespondance() {
        return correspondance;
    }

    public void setCorrespondance(CorrespondancesEtapes correspondance) {
        this.correspondance = correspondance;
    }
}