package twisk.mondeIG;

import twisk.exceptions.TempsException;
import twisk.outils.TailleComposants;

public class ActiviteIG extends EtapeIG
{
    private int temps, ecartTemps;
    public ActiviteIG(int temps, int ecartTemps)
    {
        super();
        assert ecartTemps < temps : "ecartTemps est superiore que temps";
        this.temps = temps;
        this.ecartTemps = ecartTemps;
        setNom("Activite"+getId());
        setLargeur(TailleComposants.getInstance().getActiviteW());
        setHauteur(TailleComposants.getInstance().getActiviteH());
    }

    public int getTemps() {
        return temps;
    }

    public int getEcartTemps() {
        return ecartTemps;
    }

    public void setTemps(int temps) throws TempsException
    {
        if (temps < ecartTemps) {
            throw new TempsException("Temps est plus petit que l'ecart de temps");
        } else {
            this.temps = temps;
        }

    }

    public void setEcartTemps(int ecart) throws TempsException
    {
        if (ecart > temps) {
            throw new TempsException("L'ecart de temps est plus grand que le temps");
        } else {
            this.ecartTemps = ecart;
        }

    }

    public boolean estUnGuichet() {
        return false;
    }
    public boolean estUneActivite() {
        return true;
    }

    @Override
    public String toString()
    {
        return getNom()+" : "+getTemps()+" ± "+getEcartTemps()+" temps";
    }
}
