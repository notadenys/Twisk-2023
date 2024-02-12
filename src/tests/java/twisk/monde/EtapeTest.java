package tests.java.twisk.monde;

import main.java.twisk.monde.Activite;
import main.java.twisk.monde.Etape;
import main.java.twisk.monde.Guichet;
import org.junit.jupiter.api.Test;


class EtapeTest {

    @Test
    void ajouterSuccesseur() {
        Etape a = new Activite("1");
        Etape b = new Activite("2");

        a.ajouterSuccesseur(b);
        assert(a.nbSuccesseurs() == 1):"Error in ajouterSuccesseur()";
    }

    @Test
    void estUneActivite() {
        Etape a = new Activite("1");
        boolean b = a.estUneActivite();
        assert(b):"Error in estUneActivite()";

        Etape c = new Guichet("1");
        boolean d = c.estUneActivite();
        assert(!d):"Error in estUnActivite()";
    }

    @Test
    void estUnGuichet() {
        Etape a = new Activite("1");
        boolean b = a.estUnGuichet();
        assert(!b):"Error estUnGuichet()";

        Etape c = new Guichet("1");
        boolean d = c.estUnGuichet();
        assert(d):"Error estUnGuichet()";

    }

    @Test
    void testAjouterSuccesseur() {
        Etape a = new Activite("1");
        Etape c = new Guichet("1");
        a.ajouterSuccesseur(c);
        boolean b = a.nbSuccesseurs() == 1;
        assert(b):"Error in ajouterSuccesseurs()";
    }
}