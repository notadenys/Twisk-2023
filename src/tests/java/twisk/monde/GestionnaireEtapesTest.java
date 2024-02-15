package twisk.monde;

import org.junit.jupiter.api.Test;


class GestionnaireEtapesTest {

    @Test
    void ajouter() {
        GestionnaireEtapes ge = new GestionnaireEtapes();
        Etape Et1 = new Activite("1");
        Etape Et2 = new Activite("2");
        ge.ajouter(Et1);
        ge.ajouter(Et2);
        assert(ge.nbEtapes() == 2 ) : "Error in ajouter()";
    }

    @Test
    void nbEtapes() {
        GestionnaireEtapes ge = new GestionnaireEtapes();
        Etape Et1 = new Activite("A1");
        Etape Et2 = new Activite("A2");
        Etape Et3 = new Guichet("G1");
        Etape Et4 = new Guichet("G2");
        ge.ajouter(Et1,Et2, Et3,Et4);
        assert(ge.nbEtapes() == 4 ) : "Error in nbEtapes()";
    }

    @Test
    void nbGuichets() {
        GestionnaireEtapes ge = new GestionnaireEtapes();
        Etape Et1 = new Activite("A1");
        Etape Et2 = new Activite("A2");
        Etape Et3 = new Guichet("G1");
        Etape Et4 = new Guichet("G2");
        ge.ajouter(Et1,Et2,Et3,Et4);
        assert(ge.nbGuichets() == 2 ) : "Error in nbGuichets()";
    }
}