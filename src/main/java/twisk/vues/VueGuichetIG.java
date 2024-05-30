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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Random;

public class VueGuichetIG extends VueEtapeIG implements Observateur{
    GridPane grid;

    public VueGuichetIG(MondeIG monde, EtapeIG etape)
    {
        super(monde, etape);
        monde.ajouterObservateur(this);

        grid = new GridPane() ;
        grid.setGridLinesVisible(false);
        grid.setStyle("-fx-background-color: linear-gradient(to top, #AFAFAF, #E4E1E1) ");
        grid.setStyle("-fx-border-color: #00FF36;");
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
                Image imageEntree = null;
                try {
                    imageEntree = new Image(new FileInputStream("src/main/ressources/images/entree.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView imageViewEntree = new ImageView(imageEntree);
                imageViewEntree.setPreserveRatio(true);
                imageViewEntree.setFitHeight(25);

                BorderPane.setMargin(imageViewEntree, new Insets(10, 0, 0, 0));

                es.setLeft(imageViewEntree);
                getEtape().changeHauteur(30);
            }
            setPrefHeight(getEtape().getHauteur());
            getChildren().add(getLabel());

            Iterator<VueClient> clientIterator = this.iterator();
            while (clientIterator.hasNext()) {
                VueClient client = clientIterator.next();
                ((StackPane)grid.getChildren().get(client.getRang()-1)).getChildren().add(client);
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