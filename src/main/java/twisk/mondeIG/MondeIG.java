package twisk.mondeIG;

import twisk.exceptions.ArcException;

import java.util.*;

public class MondeIG extends SujetObserve implements Iterable<Map.Entry<Integer,EtapeIG>>{
    private final HashMap<Integer, EtapeIG> etapes;
    private final ArrayList<ArcIG> arcs;

    private final ArrayList<EtapeIG> etapesSelectionnes;
    private final ArrayList<ArcIG> arcsSelectionnes;
    private PointDeControleIG pointMemorise;
    private boolean isEnAttente;

    public MondeIG()
    {
        etapes = new HashMap<>();
        arcs = new ArrayList<>();
        isEnAttente = false;
        etapesSelectionnes = new ArrayList<>();
        arcsSelectionnes = new ArrayList<>();
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
        etapesSelectionnes.clear();
        notifierObservateurs();
    }

    public void marquerCommeSortie()
    {
        for (EtapeIG etape : etapesSelectionnes)
        {
            etape.setSortie(!etape.estUneSortie());
        }
        etapesSelectionnes.clear();
        notifierObservateurs();
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
        else if (p1.getEtape().getSuccesseurs().contains(p2.getEtape()))
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
            System.out.println(arcs);
            setEnAttente(false);
            notifierObservateurs();
        }
    }

    public void delete(ArcIG... arcs)
    {
        for (ArcIG arcIG : arcs)
        {
            this.arcs.remove(arcIG);
            arcsSelectionnes.remove(arcIG);
            arcIG.getP1().getEtape().supprimerSuccesseurs(arcIG.getP2().getEtape());
        }
        notifierObservateurs();
    }

    public void checkArcs(EtapeIG etape)
    {
        arcs.removeIf(arc -> arc.getP1().getEtape() == etape || arc.getP2().getEtape() == etape);
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
        System.out.println(etapesSelectionnes);
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
        System.out.println(contains(arcIG));
        if (!contains(arcIG))
        {
            selectionnerArc(arcIG);
        }
        else
        {
            deselectionnerArc(arcIG);
        }
        System.out.println(arcsSelectionnes);
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

    @Override
    public String toString() {
        return "MondeIG{" +
                "etapes=" + etapes +
                '}';
    }
}
