package twisk.vues;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.util.Duration;
import twisk.exceptions.TwiskException;
import twisk.mondeIG.*;


public class VueMenu extends MenuBar implements Observateur {
    MondeIG monde;

    private final MenuItem renommer;
    private final MenuItem deselectionner;
    private final MenuItem supprimer;
    private final MenuItem entree;
    private final MenuItem sortie;
    private final MenuItem delai;
    private final MenuItem ecart;
    private final MenuItem jetons;

    public VueMenu(MondeIG monde)
    {
        super();
        this.monde = monde;
        monde.ajouterObservateur(this);

        Menu fichier = new Menu("Fichier");
        Menu edition = new Menu("Edition");
        Menu mondeMenu = new Menu("Monde");
        Menu parametres = new Menu("Parametres");

        MenuItem quitter = new MenuItem("Quitter");
        quitter.setOnAction(e -> Platform.exit());

        deselectionner = new MenuItem("Effacer la selection");
        deselectionner.setOnAction(e -> monde.deselectionner());

        supprimer = new MenuItem("Supprimer la selection");
        supprimer.setOnAction(e ->
        {
            monde.delete(monde.getArcsSelectionnes().toArray(new ArcIG[0]));
            monde.delete(monde.getEtapesSelectionnes().toArray(new EtapeIG[0]));
        });

        renommer = new MenuItem("Renommer la selection");
        renommer.setOnAction(e ->
        {
            TextInputDialog input = new TextInputDialog();
            input.setHeaderText("Entrez le nouvelle nom de : " + monde.getEtapesSelectionnes().get(0).getNom());
            input.showAndWait();
            monde.getEtapesSelectionnes().get(0).setNom(input.getEditor().getText());
            monde.notifierObservateurs();
        });

        entree = new MenuItem("Marquer comme entree");
        entree.setOnAction(e -> monde.marquerCommeEntree());

        sortie = new MenuItem("Marquer comme sortie");
        sortie.setOnAction(e -> monde.marquerCommeSortie());

        delai = new MenuItem("Changer le delai");
        delai.setOnAction(e ->
        {
            try {
                TextInputDialog input = new TextInputDialog();
                input.setHeaderText("Entrez le delai desire de : " + monde.getEtapesSelectionnes().get(0).getNom());
                input.showAndWait();
                ((ActiviteIG)monde.getEtapesSelectionnes().get(0)).setTemps(Integer.parseInt(input.getEditor().getText()));
                monde.notifierObservateurs();
            } catch (TwiskException exc){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(exc.getMessage());
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.play();
                pause.setOnFinished(ev -> alert.close());
                alert.showAndWait();
            }
        });

        ecart = new MenuItem("Changer l'ecart");
        ecart.setOnAction(e ->
        {
            try {
                TextInputDialog input = new TextInputDialog();
                input.setHeaderText("Entrez l'ecart de temps desire de : " + monde.getEtapesSelectionnes().get(0).getNom());
                input.showAndWait();
                ((ActiviteIG)monde.getEtapesSelectionnes().get(0)).setEcartTemps(Integer.parseInt(input.getEditor().getText()));
                monde.notifierObservateurs();
            } catch (TwiskException exc){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(exc.getMessage());
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.play();
                pause.setOnFinished(ev -> alert.close());
                alert.showAndWait();
            }
        });

        jetons = new MenuItem("Changer le nombre de jetons");
        jetons.setOnAction(e ->
        {
            try {
                TextInputDialog input = new TextInputDialog();
                input.setHeaderText("Entrez le nombre de jetons desire de : " + monde.getEtapesSelectionnes().get(0).getNom());
                input.showAndWait();
                ((GuichetIG)monde.getEtapesSelectionnes().get(0)).setNbJetons(Integer.parseInt(input.getEditor().getText()));
                monde.notifierObservateurs();
            } catch (TwiskException exc){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(exc.getMessage());
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.play();
                pause.setOnFinished(ev -> alert.close());
                alert.showAndWait();
            }
        });


        fichier.getItems().addAll(quitter);
        edition.getItems().addAll(deselectionner, supprimer, renommer);
        mondeMenu.getItems().addAll(entree, sortie);
        parametres.getItems().addAll(delai, ecart, jetons);
        getMenus().addAll(fichier, edition, mondeMenu, parametres);
        monde.notifierObservateurs();
    }

    @Override
    public void reagir() {
        renommer.setDisable(monde.getEtapesSelectionnes().size() != 1);
        deselectionner.setDisable(monde.getEtapesSelectionnes().size() + monde.getArcsSelectionnes().size() < 1);
        supprimer.setDisable(monde.getEtapesSelectionnes().size() + monde.getArcsSelectionnes().size() < 1);
        entree.setDisable(monde.getEtapesSelectionnes().isEmpty());
        sortie.setDisable(monde.getEtapesSelectionnes().isEmpty() || monde.isGuichetSelectionne());
        delai.setDisable(monde.getEtapesSelectionnes().size() != 1 || monde.isGuichetSelectionne());
        ecart.setDisable(monde.getEtapesSelectionnes().size() != 1 || monde.isGuichetSelectionne());
        jetons.setDisable(monde.getEtapesSelectionnes().size() != 1 || monde.isActiviteSelectionne());
    }
}
