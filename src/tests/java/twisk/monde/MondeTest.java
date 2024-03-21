package twisk.monde;

import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;


class MondeTest {

    @Test
    void aCommeEntree() throws InvalidNameException {
        Monde mn = new Monde();
        Etape g1 = new Guichet("g1");
        mn.aCommeEntree(g1);
        assert(mn.getEntree().getSuccesseurs().get(0).getNum() == g1.getNum()) : "Error in aCommeEntree()";
    }

    @Test
    void aCommeSortie() throws InvalidNameException {
        Monde mn = new Monde();
        Etape g1 = new Guichet("g1");
        mn.aCommeSortie(g1);
        assert(mn.getSortie().getNum() == g1.getSuccesseurs().get(0).getNum()) : "Error in aCommeSortie()";
    }

    @Test
    void ajouter() throws InvalidNameException {
        Monde mn = new Monde();
        Etape g1 = new Guichet("g1");
        mn.ajouter(g1);
        System.out.println(mn.nbEtapes());
        assert(mn.nbEtapes() == 3) : "Error in ajouter()";
    }

    @Test
    void nbGuichets() throws InvalidNameException {
        Monde mn = new Monde();
        Etape g1 = new Guichet("g1");
        Etape act1 = new Activite("a1");

        mn.ajouter(g1, act1);
        boolean b = mn.nbGuichets() == 1;
        assert(b) : "Error in nbguichet()";
    }
}