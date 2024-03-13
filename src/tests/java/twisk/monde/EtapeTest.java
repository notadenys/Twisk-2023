package twisk.monde;

import org.junit.jupiter.api.Test;
import twisk.monde.*;

class EtapeTest {

    @Test
    void ajouterSuccesseur() {
        Etape a = new Activite("a1");
        Etape b = new Activite("a2");

        a.ajouterSuccesseur(b);
        assert(a.nbSuccesseurs() == 1):"Error in ajouterSuccesseur()";
    }

    @Test
    void estUneActivite() {
        Etape a = new Activite("a1");
        boolean b = a.estUneActivite();
        assert(b):"Error in estUneActivite()";

        Etape c = new Guichet("a1");
        boolean d = c.estUneActivite();
        assert(!d):"Error in estUnActivite()";
    }

    @Test
    void estUnGuichet() {
        Etape a = new Activite("a1");
        boolean b = a.estUnGuichet();
        assert(!b):"Error estUnGuichet()";

        Etape c = new Guichet("a1");
        boolean d = c.estUnGuichet();
        assert(d):"Error estUnGuichet()";

    }

    @Test
    void testAjouterSuccesseur() {
        Etape a = new Activite("a1");
        Etape c = new Guichet("a1");
        a.ajouterSuccesseur(c);
        boolean b = a.nbSuccesseurs() == 1;
        assert(b):"Error in ajouterSuccesseurs()";
    }
}