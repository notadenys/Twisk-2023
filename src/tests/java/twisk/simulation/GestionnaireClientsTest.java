package twisk.simulation;

import twisk.monde.Etape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GestionnaireClientsTest {

    private GestionnaireClients gestionnaire;

    @BeforeEach
    void setUp() {
        gestionnaire = new GestionnaireClients();
    }

    @Test
    void testSetClients() {
        int[] tabClients = {1, 2, 3};
        gestionnaire.setClients(tabClients);
        assertEquals(3, gestionnaire.getNbClients());
    }

    @Test
    void testAllerA() {
        gestionnaire.setClients(new int[]{1, 2, 3});
        Etape etape = new Etape("Test", 1) {
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
        gestionnaire.allerA(2, etape, 1);
        for (Client client : gestionnaire) {
            if (client.getNumeroClient() == 2) {
                assertEquals(etape, client.getEtapeActuelle());
                assertEquals(1, client.getRang());
                return;
            }
        }
        fail("Client with the specified number not found.");
    }

    @Test
    void testNettoyer() {
        gestionnaire.setClients(new int[]{1, 2, 3});
        gestionnaire.nettoyer();
        assertEquals(0, gestionnaire.getNbClients());
    }
}