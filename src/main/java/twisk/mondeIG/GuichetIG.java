package twisk.mondeIG;

import javafx.scene.control.Alert;
import twisk.outils.TailleComposants;

public class GuichetIG extends EtapeIG{
    private int nbJetons;

    public GuichetIG(int nbJetons) {
        super();
        this.nbJetons = nbJetons;
        setNom("Guichet"+getId());
        setLargeur(TailleComposants.getInstance().getGuichetW());
        setHauteur(TailleComposants.getInstance().getGuichetH());
    }

    public int getNbJetons() {return nbJetons;}
    public void setNbJetons(int nb)
    {
        if (nb < 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de jetons");
            alert.setContentText("Invalid nombre de jetons");
            alert.showAndWait();
        }
        nbJetons = nb;
    }

    public boolean estUnGuichet() {
        return true;
    }
    public boolean estUneActivite() {
        return false;
    }

    @Override
    public boolean estUneSortie() {
        return false;
    }

    @Override
    public String toString()
    {
        return getNom()+" : "+getNbJetons()+" jetons";
    }
}
