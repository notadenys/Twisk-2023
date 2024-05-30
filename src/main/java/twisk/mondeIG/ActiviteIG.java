package twisk.mondeIG;

import twisk.exceptions.TempsException;
import twisk.outils.TailleComposants;

/**
 * Represents an activity in the graphical world (MondeIG).
 */
public class ActiviteIG extends EtapeIG {
    private int temps, ecartTemps;
    private boolean isRestrainte;

    /**
     * Constructs an ActiviteIG with the given time and time deviation.
     *
     * @param temps      The time required for the activity.
     * @param ecartTemps The deviation in time for the activity.
     */
    public ActiviteIG(int temps, int ecartTemps) {
        super();
        assert ecartTemps < temps : "ecartTemps est superiore que temps";
        this.temps = temps;
        this.ecartTemps = ecartTemps;
        isRestrainte = false;
        setNom("Activite" + getId());
        setLargeur(TailleComposants.getInstance().getActiviteW());
        setHauteur(TailleComposants.getInstance().getActiviteH());
    }

    /**
     * Gets the required time for the activity.
     *
     * @return The required time for the activity.
     */
    public int getTemps() {
        return temps;
    }

    /**
     * Gets the deviation in time for the activity.
     *
     * @return The deviation in time for the activity.
     */
    public int getEcartTemps() {
        return ecartTemps;
    }

    /**
     * Sets the required time for the activity.
     *
     * @param temps The required time for the activity.
     * @throws TempsException If the provided time is less than the deviation in time.
     */
    public void setTemps(int temps) throws TempsException {
        if (temps < ecartTemps) {
            throw new TempsException("Temps est plus petit que l'ecart de temps");
        } else {
            this.temps = temps;
        }
    }

    /**
     * Sets the deviation in time for the activity.
     *
     * @param ecart The deviation in time for the activity.
     * @throws TempsException If the provided deviation is greater than the required time.
     */
    public void setEcartTemps(int ecart) throws TempsException {
        if (ecart > temps) {
            throw new TempsException("L'ecart de temps est plus grand que le temps");
        } else {
            this.ecartTemps = ecart;
        }
    }

    /**
     * Checks if the activity is restrained.
     *
     * @return True if the activity is restrained, false otherwise.
     */
    public boolean isRestrainte() {
        return isRestrainte;
    }

    /**
     * Sets whether the activity is restrained or not.
     *
     * @param restrainte True if the activity is restrained, false otherwise.
     */
    public void setRestrainte(boolean restrainte) {
        isRestrainte = restrainte;
    }

    /**
     * Checks if the stage is a ticket office.
     *
     * @return Always false since this is not a ticket office.
     */
    public boolean estUnGuichet() {
        return false;
    }

    /**
     * Checks if the stage is an activity.
     *
     * @return Always true since this is an activity.
     */
    public boolean estUneActivite() {
        return true;
    }

    /**
     * Returns a string representation of the activity.
     *
     * @return A string representation of the activity.
     */
    @Override
    public String toString() {
        return getNom() + " : " + getTemps() + " ± " + getEcartTemps() + " temps";
    }
}
