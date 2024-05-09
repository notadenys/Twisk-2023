package twisk.outils;

public class TailleComposants {

    private final int clientsW;
    private final int clientsH;
    private final int fenetreW;
    private final int fenetreH;
    private final int activiteW;
    private final int activiteH;
    private final int guichetW;
    private final int guichetH;
    private final int gridW;
    private final int gridH;

    private static TailleComposants instance;

    private TailleComposants() {
        fenetreW = 1400;
        fenetreH = 1000;
        clientsW = 230;
        clientsH = 70;
        activiteW = clientsW + 20;
        activiteH = clientsH + 44;
        gridW = 250;
        gridH = 50;
        guichetW = gridW + 20;
        guichetH = gridH + 44;
    }

    public static TailleComposants getInstance() {
        if (instance == null) {
            instance = new TailleComposants();
        }
        return instance;
    }

    public int getClientsW() {
        return clientsW;
    }

    public int getClientsH() {
        return clientsH;
    }

    public int getFenetreW() {
        return fenetreW;
    }

    public int getFenetreH() {
        return fenetreH;
    }

    public int getActiviteW() {
        return activiteW;
    }

    public int getActiviteH() {
        return activiteH;
    }

    public int getGuichetW() {
        return guichetW;
    }

    public int getGuichetH() {
        return guichetH;
    }

    public int getGridW() {
        return gridW;
    }

    public int getGridH() {
        return gridH;
    }
}
