package twisk.outils;

public class FabriqueNumero {
    private int cptEtape;
    private int cptSemaphore;

    private FabriqueNumero() {
        this.cptEtape = 0;
        this.cptSemaphore = 1;
    }

    /**
     * @return instance of FabriqueNumero
     */
    public static FabriqueNumero getInstance() {
        return new FabriqueNumero();
    }

    /**
     * @return number of stage
     */
    public int getNumeroEtape() {
        return this.cptEtape++;
    }
    /**
     * @return number of
     */
    public int getNumeroSemaphore() {
        return this.cptSemaphore++;
    }

    /**
     * reset counter to 0 if we have more than one world
     */
    public void reset() {
        this.cptEtape = 0;
        this.cptSemaphore = 1;
    }
}
