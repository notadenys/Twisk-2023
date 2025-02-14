package twisk.vues;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import twisk.exceptions.MondeException;
import twisk.mondeIG.ActiviteIG;
import twisk.mondeIG.GuichetIG;
import twisk.mondeIG.MondeIG;
import twisk.outils.ThreadsManager;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * The VueOutils class represents the toolbar with buttons to control the simulation in the Twisk application.
 * It extends TilePane and implements the Observateur interface to update the buttons based on the state of the MondeIG.
 */
public class VueOutils extends TilePane implements Observateur{
    MondeIG monde;
    ArrayList<Button> buttons;
    private final Button play;
    ImageView imageViewPlay;

    /**
     * Constructor for VueOutils.
     * Initializes the toolbar with various buttons and sets up their event handlers.
     *
     * @param monde The MondeIG instance representing the world model.
     */
    public VueOutils(MondeIG monde) {
        setPadding(new Insets(5));
        setHgap(10);
        this.monde = monde;
        monde.ajouterObservateur(this);

        buttons = new ArrayList<>();
            Image image = new Image("images/plus.png");
            Image image2 = new Image("images/play.png");
            Image image3 = new Image("images/stop.png");

            ImageView imageViewActivite = new ImageView(image);
            imageViewActivite.setFitHeight(50);
            imageViewActivite.setFitWidth(50);
            Button ajouterActivite = new Button("Activite", imageViewActivite);
            ajouterActivite.setOnAction(e -> {
                if (monde.isSimulationStopped()) {
                    monde.ajouter(new ActiviteIG(4, 2));
                }
            });
            ajouterActivite.setTooltip(new Tooltip("bouton qui permet d’ajouter une activité"));
            buttons.add(ajouterActivite);

            ImageView imageViewGuichet = new ImageView(image);
            imageViewGuichet.setFitHeight(50);
            imageViewGuichet.setFitWidth(50);
            Button ajouterGuichet = new Button("Guichet", imageViewGuichet);
            ajouterGuichet.setOnAction(e -> {
                if (monde.isSimulationStopped()) {
                    monde.ajouter(new GuichetIG(2));
                }
            });
            ajouterGuichet.setTooltip(new Tooltip("bouton qui permet d’ajouter une activité"));
            buttons.add(ajouterGuichet);

            ImageView imageViewStop = new ImageView(image3);
            imageViewStop.setFitHeight(50);
            imageViewStop.setFitWidth(50);

            imageViewPlay = new ImageView(image2);
            imageViewPlay.setFitHeight(50);
            imageViewPlay.setFitWidth(50);
            play = new Button("Lancer", imageViewPlay);
            play.setOnAction(e -> {
                if (monde.isSimulationStopped()) {
                    try {
                        monde.simuler();
                    } catch (MondeException | ClassNotFoundException | InvocationTargetException |
                             NoSuchMethodException | InstantiationException ex) {
                        showErrorAlert(ex.getMessage());
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                    play.setGraphic(imageViewStop);
                    play.setText("Stop");
                    this.monde.notifierObservateurs();
                } else {
                    ThreadsManager.getInstance().detruireTout();
                    this.monde.notifierObservateurs();
                }
            });

            play.setTooltip(new Tooltip("lancer/arreter la simulation"));
            buttons.add(play);
        reagir();
    }

    /**
     * Reacts to changes in the world model by updating the toolbar buttons' state.
     */
    @Override
    public void reagir() {
        Runnable command = () -> {
            getChildren().clear();
            if (monde.isSimulationStopped()) {
                play.setGraphic(imageViewPlay);
                play.setText("Lancer");
            }
            for (Button b : buttons) {
                this.getChildren().add(b);
            }
        };
        if (Platform.isFxApplicationThread()) {
            command.run();
        } else {
            Platform.runLater(command);
        }
    }

    /**
     * Displays an error message in an alert dialog.
     *
     * @param content The error message to display.
     */
    private void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(content);
        alert.showAndWait();
    }
}
