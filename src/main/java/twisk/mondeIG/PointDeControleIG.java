package twisk.mondeIG;

public class PointDeControleIG {
    private int x, y;
    private final String id;
    private final EtapeIG etape;
    private final char pos;

    public PointDeControleIG(EtapeIG etape, char pos)
    {
        this.etape = etape;
        id = String.valueOf(etape.getId()) + pos;
        this.pos = pos;

        follow();
    }

    public void follow()
    {
        switch (pos)
        {
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

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String getId() {
        return id;
    }
    public EtapeIG getEtape() {
        return etape;
    }
    public char getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PointDeControleIG && id.equals(((PointDeControleIG) obj).id);
    }
}