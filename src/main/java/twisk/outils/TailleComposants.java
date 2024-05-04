package twisk.outils;

public class TailleComposants {

    private final int clientsW;
    private final int clientsH;
    private final int fenetreW;
    private final int fenetreH;
    private final int etapeW;
    private final int etapeH;

    private static TailleComposants instance;

    private TailleComposants() {
        fenetreW = 1400;
        fenetreH = 1000;
        clientsW = 230;
        clientsH = 70;
        etapeW = clientsW + 20;
        etapeH = clientsH + 44;
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

    public int getEtapeW() {
        return etapeW;
    }

    public int getEtapeH() {
        return etapeH;
    }
}
