package twisk.vues;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.MondeIG;
import twisk.outils.TailleComposants;

import java.util.Iterator;

/**
 * VueGuichetIG is a class representing the view of a guichet (counter) in the simulation.
 * It extends VueEtapeIG and manages the graphical representation of a guichet.
 */
public class VueGuichetIG extends VueEtapeIG implements Observateur {
    private final GridPane grid;

    /**
     * Constructor of VueGuichetIG.
     *
     * @param monde the world (model) to which the guichet belongs.
     * @param etape the step (guichet) to be represented.
     */
    public VueGuichetIG(MondeIG monde, EtapeIG etape) {
        super(monde, etape);
        monde.ajouterObservateur(this);

        grid = new GridPane();
        grid.setGridLinesVisible(false);
        grid.setStyle("-fx-background-color: linear-gradient(to top, #AFAFAF, #E4E1E1); " +
                "-fx-border-color: #00FF36;");
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(10);
        grid.setHgap(1);
        for (int i = 0; i < 10; i++) {
            StackPane pane = new StackPane();
            pane.minHeight(TailleComposants.getInstance().getGridH() ) ;
            pane.minWidth(TailleComposants.getInstance().getGridW() ) ;
            pane.prefWidth(TailleComposants.getInstance().getGridW() ) ;
            pane.setPrefHeight(TailleComposants.getInstance().getGridH() );
            pane.setStyle("-fx-background-color: linear-gradient(to top, #AFAFAF, #E4E1E1) ;-fx-border-color: #00FF36; ");
            grid.add(pane,i,0);
            grid.getColumnConstraints().add(columnConstraints) ;
        }
        reagir();
    }

    /**
     * Updates the graphical representation of the guichet when the state changes.
     */
    @Override
    public void reagir() {
        Runnable command = () -> {
            getChildren().clear();
            for (Node node : grid.getChildren()) {
                ((StackPane) node).getChildren().clear();
            }

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
            setPrefHeight(getEtape().getHauteur());
            getChildren().add(getLabel());

            boolean isDirectionInversed = this.getEtape().getPointByPos('R').isSortie();

            Iterator<VueClient> clientIterator = this.iterator();
            while (clientIterator.hasNext()) {
                VueClient client = clientIterator.next();
                if (client.getRang() <= 10) {  // draws only 10 first clients, so they fit in the grid
                    int pos = isDirectionInversed ? 10 - client.getRang() : client.getRang() - 1;
                    ((StackPane) grid.getChildren().get(pos)).getChildren().add(client);
                }
            }

            getChildren().add(grid);
            getChildren().add(es);

            if (getMonde().getEtapesSelectionnes().contains(getEtape())) {
                setStyle("-fx-padding: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-border-color: #00FF36; " +
                        "-fx-background-color: #348feb;");
            } else {
                setStyle("-fx-padding: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-border-color: #00FF36 ; " +
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
