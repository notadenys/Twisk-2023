package twisk.mondeIG;

/**
 * Represents an arc between two points of control in the graphical world (MondeIG).
 */
public class ArcIG {
    private final MondeIG monde;
    private final PointDeControleIG p1;
    private final PointDeControleIG p2;

    /**
     * Constructs an ArcIG between two points of control.
     *
     * @param monde The graphical world where the arc belongs.
     * @param p1    The first point of control.
     * @param p2    The second point of control.
     */
    public ArcIG(MondeIG monde, String p1, String p2) {
        this.monde = monde;
        this.p1 = monde.getPoint(p1);
        this.p2 = monde.getPoint(p2);
        this.p1.getEtape().ajouterSuccesseur(this.p2.getEtape());
        this.p2.getEtape().ajouterPredecesseur(this.p1.getEtape());
    }

    /**
     * Gets the first point of control.
     *
     * @return The first point of control.
     */
    public PointDeControleIG getP1() {
        return p1;
    }

    /**
     * Gets the second point of control.
     *
     * @return The second point of control.
     */
    public PointDeControleIG getP2() {
        return p2;
    }

    /**
     * Gets the ID of the first point of control.
     *
     * @return The ID of the first point of control.
     */
    public String getP1ID() {
        return p1.getId();
    }

    /**
     * Gets the ID of the second point of control.
     *
     * @return The ID of the second point of control.
     */
    public String getP2ID() {
        return p2.getId();
    }

    /**
     * Returns a string representation of the arc.
     *
     * @return A string representation of the arc.
     */
    @Override
    public String toString() {
        return p1.toString() + " " + p2.toString();
    }

    /**
     * Checks if this arc is equal to another object.
     *
     * @param obj The object to compare to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ArcIG)) return false;
        ArcIG other = (ArcIG) obj;
        return monde.equals(other.monde) && p1.equals(other.p1) && p2.equals(other.p2);
    }
}
