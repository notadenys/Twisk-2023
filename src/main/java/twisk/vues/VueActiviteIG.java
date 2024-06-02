package twisk.vues;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.MondeIG;
import twisk.outils.TailleComposants;

import java.util.Iterator;
import java.util.Random;

/**
 * VueActiviteIG is a class representing the view of an activity in the interface graphique.
 * It extends VueEtapeIG and manages the graphical representation of an activity.
 */
public class VueActiviteIG extends VueEtapeIG {
    private final HBox clientWindow;

    /**
     * Constructor of VueActiviteIG.
     *
     * @param monde the world containing the activity.
     * @param etape the activity step to be displayed.
     */
    public VueActiviteIG(MondeIG monde, EtapeIG etape) {
        super(monde, etape);
        monde.ajouterObservateur(this);
        clientWindow = new HBox();
        clientWindow.setStyle("-fx-border-color: #0059FF; " +
                "-fx-background-color: #E9E9E9; " +
                "-fx-background-insets: 0 0 -1 0, 0, 1, 2; " +
                "-fx-background-radius: 3px, 3px, 2px, 1px;");
        clientWindow.setMinSize(TailleComposants.getInstance().getClientsW(), TailleComposants.getInstance().getClientsH());
        clientWindow.setSpacing(3);

        reagir();
    }

    /**
     * React to changes in the model and update the view accordingly.
     */
    public void reagir() {
        Runnable command = () -> {
            getChildren().clear();

            BorderPane es = new BorderPane();

            getEtape().resetHauteur();
            if (getEtape().estUneEntree()) {
                Image imageEntree = new Image("images/entree.png");
                ImageView imageViewEntree = new ImageView(imageEntree);
                imageViewEntree.setPreserveRatio(true);
                imageViewEntree.setFitHeight(25);

                BorderPane.setMargin(imageViewEntree, new Insets(10, 0, 0, 0));

                es.setLeft(imageViewEntree);
                getEtape().changeHauteur(30);
            }

            if (getEtape().estUneSortie()) {
                Image imageSortie = new Image("images/sortie.png");
                ImageView imageViewSortie = new ImageView(imageSortie);
                imageViewSortie.setPreserveRatio(true);
                imageViewSortie.setFitHeight(25);

                BorderPane.setMargin(imageViewSortie, new Insets(10, 0, 0, 0));

                es.setRight(imageViewSortie);
                getEtape().changeHauteur(30);
            }
            setPrefHeight(getEtape().getHauteur());
            getChildren().add(getLabel());

            clientWindow.getChildren().clear();
            Iterator<VueClient> clientIterator = this.iterator();
            while (clientIterator.hasNext()) {
                VueClient client = clientIterator.next();
                Random random = new Random();
                client.setCenterX(random.nextInt(TailleComposants.getInstance().getClientsW()));
                client.setCenterY(random.nextInt(TailleComposants.getInstance().getClientsH()));
                clientWindow.getChildren().add(client);
            }

            getChildren().add(clientWindow);
            getChildren().add(es);

            if (getMonde().getEtapesSelectionnes().contains(getEtape())) {
                setStyle("-fx-padding: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-border-color: #0059FF; " +
                        "-fx-background-color: #348feb;");
            } else {
                setStyle("-fx-padding: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-border-color: #0059FF; " +
                        "-fx-background-color: #FFFFFF;");
            }
        };
        if (Platform.isFxApplicationThread()) {
            command.run();
        } else {
            Platform.runLater(command);
        }
    }
}
