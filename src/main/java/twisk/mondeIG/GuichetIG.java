package twisk.mondeIG;

import twisk.exceptions.JetonsException;
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
    public void setNbJetons(int nb) throws JetonsException
    {
        if (nb < 1) throw new JetonsException("Invalid nombre de jetons");
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
