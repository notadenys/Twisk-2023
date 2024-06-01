package twisk.mondeIG;

import twisk.outils.FabriqueIdentifiant;
import twisk.outils.TailleComposants;
import twisk.simulation.Client;

import java.util.*;

public abstract class EtapeIG implements Iterable<PointDeControleIG>{
    private String nom;
    private final int id;
    private int x;
    private int y;
    private int largeur;
    private int hauteur;
    private boolean isEntree;
    private boolean isSortie;
    private final ArrayList<PointDeControleIG> points;
    private final ArrayList<EtapeIG> successeurs;
    private final ArrayList<EtapeIG> predecesseurs;

    public EtapeIG(String nom, int l, int h)
    {
        this.nom = nom;
        this.id = FabriqueIdentifiant.getInstance().getNumeroEtape();
        Random random = new Random();
        this.x = random.nextInt(TailleComposants.getInstance().getFenetreW()-TailleComposants.getInstance().getClientsW());
        this.y = random.nextInt(TailleComposants.getInstance().getFenetreH()-3*TailleComposants.getInstance().getClientsH());
        this.largeur = l;
        this.hauteur = h;
        isEntree = false;
        isSortie = false;

        successeurs = new ArrayList<>();
        predecesseurs = new ArrayList<>();

        // Activity has 4 points and Guichet has only 2
        points = new ArrayList<>();
        if(estUneActivite()) {
            points.add(new PointDeControleIG(this, 'T'));
            points.add(new PointDeControleIG(this, 'B'));
        }
        points.add(new PointDeControleIG(this, 'L'));
        points.add(new PointDeControleIG(this, 'R'));

    }

    public void move(int x, int y)
    {
        setX(x);
        setY(y);
        resetPoints();
    }

    public EtapeIG() {this("Etape", 0, 0);}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public void setEntree(boolean bool) {
        isEntree = bool;
    }
    public void setSortie(boolean bool) {isSortie = bool;}

    public void setLargeur(int l){largeur = l;}
    public void setHauteur(int h){hauteur = h;}

    public void changeHauteur(int hauteurDiff) {
        if (estUneActivite())
        {
            if (hauteur == TailleComposants.getInstance().getActiviteH()) hauteur += hauteurDiff;
        }
        else if (estUnGuichet())
        {
            if (hauteur == TailleComposants.getInstance().getGuichetH()) hauteur += hauteurDiff;
        }
        resetPoints();
    }
    public void resetHauteur()
    {
        if(estUneActivite())
        {
            hauteur = TailleComposants.getInstance().getActiviteH();
        }
        else
        {
            hauteur = TailleComposants.getInstance().getGuichetH();
        }
        resetPoints();
    }

    public void resetPoints()
    {
        for (PointDeControleIG p : points) {
            p.follow();
        }
    }
    public boolean estUneEntree() {
        return isEntree;
    }
    public boolean estUneSortie() {
        return isSortie;
    }
    public abstract boolean estUneActivite();
    public abstract boolean estUnGuichet();
    public int getId() {return id;}
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getLargeur() {
        return largeur;
    }
    public int getHauteur() {
        return hauteur;
    }
    public PointDeControleIG getPoint(String id)
    {
        for (PointDeControleIG point : points)
        {
            if (Objects.equals(point.getId(), id))
            {
                return point;
            }
        }
        return null;
    }

    public PointDeControleIG getPointByPos(char pos) {
        for (PointDeControleIG point : points) {
            if (Objects.equals(point.getPos(), pos)) return point;
        }
        return null;
    }

    public ArrayList<EtapeIG> getSuccesseurs() {
        return successeurs;
    }
    public void ajouterSuccesseur(EtapeIG etape) {
        if (!successeurs.contains(etape)) successeurs.add(etape);
    }
    public void supprimerSuccesseur(EtapeIG etape)
    {
        successeurs.remove(etape);
    }

    public ArrayList<EtapeIG> getPredecesseurs() {
        return predecesseurs;
    }
    public void ajouterPredecesseur(EtapeIG etape) {
        if (!predecesseurs.contains(etape)) predecesseurs.add(etape);
    }
    public void supprimerPredecesseur(EtapeIG etape) {
        predecesseurs.remove(etape);
    }

    public Iterator<PointDeControleIG> iterator()
    {
        return points.iterator();
    }

    @Override
    public String toString() {
        return getNom();
    }

    public boolean estUneActiviteRestreinte() {
        return false;
    }
}
