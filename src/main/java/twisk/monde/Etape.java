package twisk.monde;

import twisk.outils.FabriqueNumero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public abstract class Etape implements Iterable<Etape> {
    private final String nom;
    private final ArrayList<Etape> successeurs;
    private final int numEtape;

    /**
     * @param nom name of stage
     */
    public Etape(String nom) {
        this.nom = nom;
        this.successeurs = new ArrayList<>();
        numEtape = FabriqueNumero.getInstance().getNumeroEtape();
    }

    /**
     * @param successeurs adding successors
     */
    public void ajouterSuccesseur(Etape... successeurs) {
        Collections.addAll(this.successeurs, successeurs);
    }


    /**
     * @return true if activity
     */
    public abstract boolean estUneActivite();

    /**
     * @return true if ticket office(guichet)
     */
    public abstract boolean estUnGuichet();

    public abstract boolean estUneSortie();

    /**
     * @return code C of step
     */
    public abstract String toC();
    public abstract String toDefine();

    public Iterator<Etape> iterator() {
        return successeurs.iterator();
    }

    public String getNom(){return nom;}
    public abstract String getConstNom();

    public int getNum(){return numEtape;}

    public ArrayList<Etape> getSuccesseurs() {
        return successeurs;
    }

    public Etape getSuccesseur(){
        if (successeurs.isEmpty()){
            return null;
        }
        return successeurs.get(0);
    }

    public int nbSuccesseurs()
    {
        return successeurs.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getNom()).append(" (id : ").append(getNum()).append(") : ").append(nbSuccesseurs()).append(" successeur(s)");
        if(nbSuccesseurs() > 0)
        {
            sb.append(" - ");
            for (Etape etape : getSuccesseurs())
            {
                sb.append(etape.getNom()).append(" ");
            }
        }
        return sb.toString();
    }

}
