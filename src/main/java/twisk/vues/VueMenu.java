package twisk.vues;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.util.Duration;
import twisk.exceptions.MondeException;
import twisk.exceptions.TwiskException;
import twisk.mondeIG.*;
import twisk.outils.ThreadsManager;

/**
 * The VueMenu class represents the menu bar in the Twisk application.
 * It extends MenuBar and implements the Observateur interface to update the menu items
 * based on the state of the MondeIG.
 */
public class VueMenu extends MenuBar implements Observateur {
    private final MondeIG monde;

    private final MenuItem renommer;
    private final MenuItem deselectionner;
    private final MenuItem supprimer;
    private final MenuItem entree;
    private final MenuItem sortie;
    private final MenuItem delai;
    private final MenuItem ecart;
    private final MenuItem jetons;
    private final MenuItem clients;

    /**
     * Constructor for VueMenu.
     * Initializes the menu bar with various menu items and sets up their event handlers.
     *
     * @param monde The MondeIG instance representing the world model.
     */
    public VueMenu(MondeIG monde) {
        super();
        this.monde = monde;
        monde.ajouterObservateur(this);

        Menu fichier = new Menu("Fichier");
        Menu edition = new Menu("Edition");
        Menu mondeMenu = new Menu("Monde");
        Menu parametres = new Menu("Parametres");

        MenuItem quitter = new MenuItem("Quitter");
        quitter.setOnAction(e ->
                {
                    ThreadsManager.getInstance().detruireTout();
                    Platform.exit();
                });

        deselectionner = new MenuItem("Effacer la selection");
        deselectionner.setOnAction(e -> monde.deselectionner());

        supprimer = new MenuItem("Supprimer la selection");
        supprimer.setOnAction(e -> {
            monde.delete(monde.getArcsSelectionnes().toArray(new ArcIG[0]));
            monde.delete(monde.getEtapesSelectionnes().toArray(new EtapeIG[0]));
        });

        renommer = new MenuItem("Renommer la selection");
        renommer.setOnAction(e -> {
            TextInputDialog input = new TextInputDialog();
            input.setHeaderText("Entrez le nouvelle nom de : " + monde.getEtapesSelectionnes().get(0).getNom());
            input.showAndWait();
            if (!input.getEditor().getText().isEmpty()) {
                monde.getEtapesSelectionnes().get(0).setNom(input.getEditor().getText());
            }
            monde.deselectionner();
        });

        entree = new MenuItem("Marquer comme entree");
        entree.setOnAction(e -> {
            monde.marquerCommeEntree();
            monde.deselectionner();
        });

        sortie = new MenuItem("Marquer comme sortie");
        sortie.setOnAction(e -> {
            monde.marquerCommeSortie();
            monde.deselectionner();
        });

        delai = new MenuItem("Changer le delai");
        delai.setOnAction(e -> {
            try {
                TextInputDialog input = new TextInputDialog();
                input.setHeaderText("Entrez le delai desire de : " + monde.getEtapesSelectionnes().get(0).getNom());
                input.showAndWait();
                if (!input.getEditor().getText().isEmpty()) {
                    ((ActiviteIG) monde.getEtapesSelectionnes().get(0)).setTemps(Integer.parseInt(input.getEditor().getText()));
                }
                monde.deselectionner();
            } catch (TwiskException exc) {
                showErrorMessage(exc.getMessage());
            }
        });

        ecart = new MenuItem("Changer l'ecart");
        ecart.setOnAction(e -> {
            try {
                TextInputDialog input = new TextInputDialog();
                input.setHeaderText("Entrez l'ecart de temps desire de : " + monde.getEtapesSelectionnes().get(0).getNom());
                input.showAndWait();
                if (!input.getEditor().getText().isEmpty()) {
                    ((ActiviteIG) monde.getEtapesSelectionnes().get(0)).setEcartTemps(Integer.parseInt(input.getEditor().getText()));
                }
                monde.deselectionner();
            } catch (TwiskException exc) {
                showErrorMessage(exc.getMessage());
            }
        });

        jetons = new MenuItem("Changer le nombre de jetons");
        jetons.setOnAction(e -> {
            try {
                TextInputDialog input = new TextInputDialog();
                input.setHeaderText("Entrez le nombre de jetons desire de : " + monde.getEtapesSelectionnes().get(0).getNom());
                input.showAndWait();
                if (!input.getEditor().getText().isEmpty()) {
                    ((GuichetIG) monde.getEtapesSelectionnes().get(0)).setNbJetons(Integer.parseInt(input.getEditor().getText()));
                }
                monde.deselectionner();
            } catch (TwiskException exc) {
                showErrorMessage(exc.getMessage());
            }
        });

        clients = new MenuItem("Changer le nombre de clients");
        clients.setOnAction(e -> {
            TextInputDialog input = new TextInputDialog();
            input.setHeaderText("Entrez le nombre de clients");
            input.showAndWait();
            try {
                if (!input.getEditor().getText().isEmpty()) {
                    monde.setNbClients(Integer.parseInt(input.getEditor().getText()));
                }
            } catch (MondeException ex) {
                showErrorMessage(ex.getMessage());
            }
        });

        fichier.getItems().addAll(quitter);
        edition.getItems().addAll(deselectionner, supprimer, renommer);
        mondeMenu.getItems().addAll(entree, sortie);
        parametres.getItems().addAll(clients, delai, ecart, jetons);
        getMenus().addAll(fichier, edition, mondeMenu, parametres);
        monde.notifierObservateurs();
    }

    /**
     * Reacts to changes in the world model by updating the menu items' enabled/disabled state.
     */
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
        clients.setDisable(!monde.isSimulationStopped());
    }

    /**
     * Displays an error message in an alert dialog.
     *
     * @param message The error message to display.
     */
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.play();
        pause.setOnFinished(ev -> alert.close());
        alert.showAndWait();
    }
}
