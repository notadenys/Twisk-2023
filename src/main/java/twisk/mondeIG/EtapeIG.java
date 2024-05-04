package twisk.mondeIG;

import twisk.outils.FabriqueIdentifiant;
import twisk.outils.TailleComposants;

import java.util.*;

public class EtapeIG implements Iterable<PointDeControleIG>{
    private String nom;
    private final int id;
    private int x;
    private int y;
    private final int largeur;
    private int hauteur;
    private boolean isEntree;
    private boolean isSortie;
    private final ArrayList<PointDeControleIG> points;
    private final ArrayList<EtapeIG> successeurs;

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

        points = new ArrayList<>();
        points.add(new PointDeControleIG(this, 'L'));
        points.add(new PointDeControleIG(this, 'T'));
        points.add(new PointDeControleIG(this, 'R'));
        points.add(new PointDeControleIG(this, 'B'));
    }

    public void move(int x, int y)
    {
        setX(x);
        setY(y);
        resetPoints();
    }

    public EtapeIG(String nom)
    {
        this(nom, 50, 50);
    }
    public EtapeIG() {this("Etape", TailleComposants.getInstance().getEtapeW(), TailleComposants.getInstance().getEtapeH());}

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

    public void changeHauteur(int hauteurDiff) {
        if (hauteur == TailleComposants.getInstance().getEtapeH()) hauteur += hauteurDiff;
        resetPoints();
    }
    public void resetHauteur()
    {
        hauteur = TailleComposants.getInstance().getEtapeH();
        resetPoints();
    }

    public void resetPoints()
    {
        points.clear();
        points.add(new PointDeControleIG(this, 'L'));
        points.add(new PointDeControleIG(this, 'T'));
        points.add(new PointDeControleIG(this, 'R'));
        points.add(new PointDeControleIG(this, 'B'));
    }
    public boolean isEntree() {
        return isEntree;
    }
    public boolean isSortie() {
        return isSortie;
    }
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

    public ArrayList<EtapeIG> getSuccesseurs() {
        return successeurs;
    }
    public void ajouterSuccesseurs(EtapeIG... etapes)
    {
        Collections.addAll(successeurs, etapes);
    }
    public void supprimerSuccesseurs(EtapeIG... etapes)
    {
        successeurs.removeAll(Arrays.asList(etapes));
    }

    public Iterator<PointDeControleIG> iterator()
    {
        return points.iterator();
    }

    @Override
    public String toString() {
        return getNom();
    }
}
