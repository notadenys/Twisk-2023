package twisk.monde;

import twisk.outils.FabriqueNumero;

import javax.naming.InvalidNameException;

public class Guichet extends Etape {
    private int nbJetons;
    private int nSemaphore;

    public Guichet(String nom) throws InvalidNameException {
        super(nom);
    }

    public Guichet(String nom, int nb) throws InvalidNameException {
        super(nom);
        this.nbJetons = nb;
        this.nSemaphore = FabriqueNumero.getInstance().getNumeroSemaphore();
    }

    public boolean estUnGuichet() {
        return true;
    }

    public boolean estUneActivite() {
        return false;
    }

    public boolean estUneSortie() {
        return false;
    }

    public String toC()
    {
        StringBuilder str = new StringBuilder();
        String s1 = "    P(ids,"+ nSemaphore + ");\n";
        if (this.getSuccesseur() != null) {
            String s2 = "    transfert(" + this.getConstNom() + "," + this.getSuccesseur().getConstNom() + ");\n";
            String s4 = "    delai(3,1);\n";
            String s3 = "    V(ids," + nSemaphore + ");\n";
            str.append(s1).append(s2).append(s4).append(s3);
            str.append(this.getSuccesseur().toC());
        }
        return str.toString();
    }

    @Override
    public String getConstNom()
    {
        return "GUICHET_" + getNom().toUpperCase();
    }

    public String getConstSem()
    {
        return "SEM_TICKET_" + getNom().toUpperCase();
    }

    @Override
    public String toDefine()
    {
        return "#define " + getConstNom() + " " + getNum() + "\n#define " + getConstSem() + " " + nSemaphore;
    }
}
