package twisk.monde;

public class Guichet extends Etape {
    private int nbJetons;
    private int nSemaphore;

    public Guichet(String nom) {
        super(nom);
    }

    public Guichet(String nom, int nb) {
        super(nom);
        this.nbJetons = nb;
        this.nSemaphore = 1;
    }

    public boolean estUnGuichet() {
        return true;
    }

    public boolean estUneActivite() {
        return false;
    }

    public String toC()
    {
        StringBuilder str = new StringBuilder("");
        String s1 = "P(ids,"+ nSemaphore + ");\n";
        if (this.getSuccesseur() != null) {
            String s2 = "transfert(" + this.getNum() + "," + this.getSuccesseur().getNum() + ");\n";
            String s4 = "delai(3,1);\n";
            String s3 = "V(ids," + nSemaphore + ");\n";
            this.nSemaphore++;
            str.append(s1).append(s2).append(s4).append(s3);
            str.append(this.getSuccesseur().toC());
        }
        return str.toString();
    }
}
