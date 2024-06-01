package twisk.mondeIG;

/**
 * Represents a control point associated with a stage in the interactive world.
 */
public class PointDeControleIG {
    private int x, y;
    private boolean isSortie;
    private final String id;
    private final EtapeIG etape;
    private final char pos;

    /**
     * Constructs a control point for the specified stage at the given position.
     *
     * @param etape The stage associated with this control point.
     * @param pos   The position of the control point ('R' for right, 'T' for top, 'L' for left, 'B' for bottom).
     */
    public PointDeControleIG(EtapeIG etape, char pos) {
        this.etape = etape;
        id = String.valueOf(etape.getId()) + pos;
        this.pos = pos;
        isSortie = false;
        follow(); // Updates the coordinates of the control point based on its position.
    }

    /**
     * Updates the coordinates of the control point based on its position relative to the associated stage.
     */
    public void follow() {
        switch (pos) {
            case 'R':  // right
                x = etape.getX() + etape.getLargeur();
                y = etape.getY() + (etape.getHauteur() / 2);
                break;

            case 'T':  // top
                x = etape.getX() + (etape.getLargeur() / 2);
                y = etape.getY();
                break;

            case 'L':  // left
                x = etape.getX();
                y = etape.getY() + (etape.getHauteur() / 2);
                break;

            case 'B':  // bottom
                x = etape.getX() + (etape.getLargeur() / 2);
                y = etape.getY() + etape.getHauteur();
                break;

            default:
                break;
        }
    }

    /**
     * Gets the X-coordinate of the control point.
     *
     * @return The X-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Y-coordinate of the control point.
     *
     * @return The Y-coordinate.
     */
    public int getY() {
        return y;
    }

    public boolean isSortie() {
        return isSortie;
    }

    public void setSortie(boolean sortie) {
        isSortie = sortie;
    }

    public char getPos() { return pos; }

    /**
     * Gets the unique identifier of the control point.
     *
     * @return The control point identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the stage associated with this control point.
     *
     * @return The associated stage.
     */
    public EtapeIG getEtape() {
        return etape;
    }

    /**
     * Returns a string representation of the control point (its unique identifier).
     *
     * @return The string representation of the control point.
     */
    @Override
    public String toString() {
        return id;
    }

    /**
     * Checks if this control point is equal to another object.
     *
     * @param obj The object to compare with this control point.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof PointDeControleIG && id.equals(((PointDeControleIG) obj).id);
    }
}
