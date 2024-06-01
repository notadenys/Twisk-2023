package twisk.mondeIG;

import twisk.outils.FabriqueIdentifiant;
import twisk.outils.TailleComposants;
import twisk.simulation.Client;

import java.util.*;

/**
 * The abstract class EtapeIG represents a step in the IG world of the Twisk application.
 * It manages the basic properties and behaviors of an IG step, including position, size,
 * entry/exit status, and control points.
 */
public abstract class EtapeIG implements Iterable<PointDeControleIG> {
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

    /**
     * Constructs an EtapeIG with a specified name, width, and height.
     *
     * @param nom the name of the step.
     * @param l   the width of the step.
     * @param h   the height of the step.
     */
    public EtapeIG(String nom, int l, int h) {
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

        points = new ArrayList<>();
        if (estUneActivite()) {
            points.add(new PointDeControleIG(this, 'T'));
            points.add(new PointDeControleIG(this, 'B'));
        }
        points.add(new PointDeControleIG(this, 'L'));
        points.add(new PointDeControleIG(this, 'R'));
    }

    /**
     * Moves the step to a new position and updates control points.
     *
     * @param x the new x-coordinate.
     * @param y the new y-coordinate.
     */
    public void move(int x, int y) {
        setX(x);
        setY(y);
        resetPoints();
    }

    /**
     * Default constructor creating an EtapeIG with default values.
     */
    public EtapeIG() {
        this("Etape", 0, 0);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
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

    public void setSortie(boolean bool) {
        isSortie = bool;
    }

    public void setLargeur(int l) {
        largeur = l;
    }

    public void setHauteur(int h) {
        hauteur = h;
    }

    /**
     * Adjusts the height of the step based on the specified height difference.
     *
     * @param hauteurDiff the height difference to be applied.
     */
    public void changeHauteur(int hauteurDiff) {
        if (estUneActivite()) {
            if (hauteur == TailleComposants.getInstance().getActiviteH()) hauteur += hauteurDiff;
        } else if (estUnGuichet()) {
            if (hauteur == TailleComposants.getInstance().getGuichetH()) hauteur += hauteurDiff;
        }
        resetPoints();
    }

    /**
     * Resets the height of the step to its default value based on its type.
     */
    public void resetHauteur() {
        if (estUneActivite()) {
            hauteur = TailleComposants.getInstance().getActiviteH();
        } else {
            hauteur = TailleComposants.getInstance().getGuichetH();
        }
        resetPoints();
    }

    /**
     * Resets the positions of control points based on the current position of the step.
     */
    public void resetPoints() {
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

    public int getId() {
        return id;
    }

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

    /**
     * Gets the control point with the specified ID.
     *
     * @param id the ID of the control point.
     * @return the control point with the specified ID, or null if not found.
     */
    public PointDeControleIG getPoint(String id) {
        for (PointDeControleIG point : points) {
            if (Objects.equals(point.getId(), id)) {
                return point;
            }
        }
        return null;
    }

    /**
     * Gets the control point at the specified position.
     *
     * @param pos the position of the control point (T, B, L, R).
     * @return the control point at the specified position, or null if not found.
     */
    public PointDeControleIG getPointByPos(char pos) {
        for (PointDeControleIG point : points) {
            if (Objects.equals(point.getPos(), pos)) return point;
        }
        return null;
    }

    public ArrayList<EtapeIG> getSuccesseurs() {
        return successeurs;
    }

    /**
     * Adds a successor to this step.
     *
     * @param etape the successor step to be added.
     */
    public void ajouterSuccesseur(EtapeIG etape) {
        if (!successeurs.contains(etape)) successeurs.add(etape);
    }

    /**
     * Removes a successor from this step.
     *
     * @param etape the successor step to be removed.
     */
    public void supprimerSuccesseur(EtapeIG etape) {
        successeurs.remove(etape);
    }

    public ArrayList<EtapeIG> getPredecesseurs() {
        return predecesseurs;
    }

    /**
     * Adds a predecessor to this step.
     *
     * @param etape the predecessor step to be added.
     */
    public void ajouterPredecesseur(EtapeIG etape) {
        if (!predecesseurs.contains(etape)) predecesseurs.add(etape);
    }

    /**
     * Removes a predecessor from this step.
     *
     * @param etape the predecessor step to be removed.
     */
    public void supprimerPredecesseur(EtapeIG etape) {
        predecesseurs.remove(etape);
    }

    @Override
    public Iterator<PointDeControleIG> iterator() {
        return points.iterator();
    }

    @Override
    public String toString() {
        return getNom();
    }

    /**
     * Indicates whether this step is a restricted activity.
     *
     * @return false by default, should be overridden if applicable.
     */
    public boolean estUneActiviteRestreinte() {
        return false;
    }
}
