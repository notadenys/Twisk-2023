package twisk.mondeIG;

import javafx.scene.control.Alert;
import twisk.outils.TailleComposants;

public class ActiviteIG extends EtapeIG
{
    private int temps, ecartTemps;
    private boolean isRestrainte;

    public ActiviteIG(int temps, int ecartTemps)
    {
        super();
        assert ecartTemps < temps : "ecartTemps est superiore que temps";
        this.temps = temps;
        this.ecartTemps = ecartTemps;
        isRestrainte = false;
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

    public void setTemps(int temps)
    {
        if (temps < ecartTemps) {
            showErrorAlert("Erreur de temps", "Temps est plus petit que l'ecart de temps");
        } else {
            this.temps = temps;
        }

    }

    public void setEcartTemps(int ecart)
    {
        if (ecart > temps) {
            showErrorAlert("Erreur de l'ecart de temps", "L'ecart de temps est plus grand que le temps");
        } else {
            this.ecartTemps = ecart;
        }
    }

    public boolean isRestrainte() {
        return isRestrainte;
    }
    public void setRestrainte(boolean restrainte) {
        isRestrainte = restrainte;
    }

    public boolean estUnGuichet() {
        return false;
    }
    public boolean estUneActivite() {
        return true;
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public String toString()
    {
        return getNom()+" : "+getTemps()+" ± "+getEcartTemps()+" temps";
    }
}
