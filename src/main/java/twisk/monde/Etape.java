package twisk.monde;

import twisk.outils.FabriqueNumero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

public abstract class Etape implements Iterable<Etape> {
    private final String nom;
    private final String modifiedNom;
    private final ArrayList<Etape> successeurs;
    private final int numEtape;

    /**
     * @param nom name of stage
     */
    public Etape(String nom){
        this.nom = nom;
        // very complicated code which replaces all spaces by _ and all non-word characters by a random letter :')
        this.modifiedNom = nom.replace(' ', '_').replaceAll("\\W", Character.toString((char)(97 + new Random().nextInt(25)))).replaceAll("^[0-9]", Character.toString((char)(97 + new Random().nextInt(25))));
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

    public String getModifiedNom(){
        return modifiedNom;
    }

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

    public int nbSuccesseurs() {
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
