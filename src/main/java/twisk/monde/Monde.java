package twisk.monde;

import java.util.Iterator;


public class Monde implements Iterable<Etape>{
    private final GestionnaireEtapes ge;
    private final SasEntree se;
    private final SasSortie ss;

    /**
     * constructor for Monde
     */
    public Monde() {
        se = new SasEntree();
        ss = new SasSortie();
        ge = new GestionnaireEtapes();
        ge.ajouter(se, ss);
    }

    /**
     * sets entrances for the world
     * @param etapes stages are to be set as entrances
     */
    public void aCommeEntree(Etape... etapes) {
        se.ajouterSuccesseur(etapes);
    }

    /**
     * sets exits for the world
     * @param etapes stages are to be set as exits
     */
    public void aCommeSortie(Etape... etapes) {
        for (Etape etape : etapes)
        {
            etape.ajouterSuccesseur(ss);
        }
    }

    /**
     * add stages to the world
     * @param etapes stages to add
     */
    public void ajouter(Etape...etapes) {
        ge.ajouter(etapes);
    }

    /**
     * @return amount of existing stages
     */
    public int nbEtapes() {
        return ge.nbEtapes();
    }

    /**
     * @return amount of counters
     */
    public int nbGuichets() {
        return ge.nbGuichets();
    }

    public Iterator<Etape> iterator()
    {
        return ge.iterator();
    }

    public SasEntree getEntree() {return se;}
    public SasSortie getSortie() {return ss;}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Etape etape : this)
        {
            sb.append(etape).append('\n');
        }

        return sb.toString();
    }
}
