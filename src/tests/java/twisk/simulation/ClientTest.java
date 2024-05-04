package twisk.simulation;

import org.junit.jupiter.api.Test;
import twisk.monde.Etape;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    @Test
    public void testAllerA() {
        Client client = new Client(1);
        Etape etape = new Etape("Etape") {
            @Override
            public boolean estUneActivite() {
                return false;
            }
            @Override
            public boolean estUnGuichet() {
                return false;
            }
            @Override
            public boolean estUneSortie() {
                return false;
            }
            @Override
            public String toC() {
                return "";
            }
            @Override
            public String toDefine() {
                return "";
            }
            @Override
            public String getConstNom() {
                return "";
            }
        };
        client.allerA(etape, 2);
        assertEquals(etape, client.getEtapeActuelle());
        assertEquals(2, client.getRang());
    }

    @Test
    public void testGetNumeroClient() {
        int numero = 1;
        Client client = new Client(numero);
        assertEquals(numero, client.getNumeroClient());
    }
}