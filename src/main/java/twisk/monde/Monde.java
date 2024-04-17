package twisk.monde;
import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.Iterator;


public class Monde implements Iterable<Etape>{
    private GestionnaireEtapes ge;
    private SasEntree se;
    private SasSortie ss;

    /**
     * constructor for Monde
     */
    public Monde() {
        try {
            se = new SasEntree();
            ss = new SasSortie();
            ge = new GestionnaireEtapes();
            ge.ajouter(se, ss);
        }
        catch (InvalidNameException e)
        {
            e.printStackTrace();
        }
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

    public Iterator<Etape> iterator() {
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
        StringBuilder str = new StringBuilder();
        String includes = "#include <stdio.h> \n" +
                "#include <stdlib.h>\n" +
                "#include \"def.h\"\n\n";
        String simulation = "\n\nvoid simulation(int ids)\n" +
                "{\n";
        str.append(includes);
        for (Etape etape : ge.getEtapes()) {
            str.append(etape.toDefine());
            str.append("\n");
        }
        str.append(simulation);

//        for (Etape etape : ge.getEtapes())
//        {
//            System.out.println(etape);
//            str.append(etape.toC());
//        }

        if (ge.getEtapes() != null && !ge.getEtapes().isEmpty()) {
            ArrayList<Etape> etapes = ge.getEtapes();
            str.append(etapes.get(0).toC());
            str.append(etapes.get(0).getSuccesseur().toC());
        }
        str.append("} ");
        return str.toString();
    }
    public String getName(int id) {
        return this.ge.getNomByID(id);
    }
}
