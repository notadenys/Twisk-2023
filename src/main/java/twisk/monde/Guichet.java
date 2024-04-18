package twisk.monde;

import twisk.outils.FabriqueNumero;

public class Guichet extends Etape {
    private final int nbJetons;
    private int nSemaphore;

    public Guichet(String nom) {
        super(nom, 1);
        nbJetons = 0;
    }

    public Guichet(String nom, int nb){
        super(nom, 1);
        this.nbJetons = nb;
        this.nSemaphore = FabriqueNumero.getInstance().getNumeroSemaphore();
    }

    public int getNbJetons() {return nbJetons;}

    public boolean estUnGuichet() {
        return true;
    }

    public boolean estUneActivite() {
        return false;
    }

    public boolean estUneSortie() {
        return false;
    }

    public String toC() {
        if(this.estUnGuichet()) {
            StringBuilder str = new StringBuilder();
            String s1 = "    P(ids," + nSemaphore + ");\n";
            if (this.getSuccesseur() != null) {
                String s2 = "    transfert(" + this.getConstNom() + "," + this.getSuccesseur().getConstNom() + ");\n";
                String s4 = "    delai(3,1);\n";
                String s3 = "    V(ids," + nSemaphore + ");\n";
                str.append(s1).append(s2).append(s4).append(s3);
                str.append(this.getSuccesseur().toC());
            }
            return str.toString();
        }
        return"";
    }

    @Override
    public String getConstNom()
    {
        return "GUICHET_" + getModifiedNom().toUpperCase();
    }

    public String getConstSem()
    {
        return "SEM_TICKET_" + getModifiedNom().toUpperCase();
    }

    @Override
    public String toDefine()
    {
        return "#define " + getConstNom() + " " + getNum() + "\n#define " + getConstSem() + " " + nSemaphore;
    }
}
