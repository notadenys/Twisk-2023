package twisk.mondeIG;

public class ArcIG {
    private final MondeIG monde;
    private final PointDeControleIG p1;
    private final PointDeControleIG p2;

    public ArcIG(MondeIG monde, String p1, String p2)
    {
        this.monde = monde;
        this.p1 = monde.getPoint(p1);
        this.p2 = monde.getPoint(p2);
        this.p1.getEtape().ajouterSuccesseurs(this.p2.getEtape());
        this.p2.getEtape().ajouterPredecesseurs(this.p1.getEtape());
    }

    public PointDeControleIG getP1() {
        return p1;
    }

    public PointDeControleIG getP2() {
        return p2;
    }

    public String getP1ID() {
        return p1.getId();
    }

    public String getP2ID() {
        return p2.getId();
    }

    @Override
    public String toString() {
        return p1.toString() + " " + p2.toString();
    }
}
