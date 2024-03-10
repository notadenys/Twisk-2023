package twisk.outils;

public class FabriqueNumero {
    private int cptEtape;
    private int cptSemaphore;

    private static FabriqueNumero instance;

    private FabriqueNumero() {
        this.cptEtape = 0;
        this.cptSemaphore = 1;
    }

    /**
     * @return instance of FabriqueNumero
     */
    public static FabriqueNumero getInstance() {
        if (instance == null) {
            instance = new FabriqueNumero();
        }
        return instance;
    }

    /**
     * @return number of stage
     */
    public int getNumeroEtape() {
        return instance.cptEtape++;
    }
    /**
     * @return number of
     */
    public int getNumeroSemaphore() {
        return instance.cptSemaphore++;
    }

    /**
     * reset counter to 0 if we have more than one world
     */
    public void reset() {
        instance.cptEtape = 0;
        instance.cptSemaphore = 1;
    }
}
