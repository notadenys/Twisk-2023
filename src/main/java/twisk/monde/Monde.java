package twisk.monde;
import java.util.ArrayList;
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

    public String toC() {
        StringBuilder str = new StringBuilder("");
        String includes = "#include <stdio.h> \n" +
                "#include <stdlib.h>\n" +
                "#include \"def.h\"\n"
                ;
        String simulation = "\nvoid simulation(int ids)\n" +
                "{\n";
        str.append(includes);
        str.append(simulation);
        if (ge.getEtapes() != null && ge.getEtapes().size() > 1) {
            ArrayList<Etape> etapes = ge.getEtapes();
            for (int i = 0; i < etapes.size() - 2; i++) {
                Etape e = etapes.get(i);
                str.append(e.toC());
            }
        }
        str.append("} ");
        return str.toString();
    }
}
