package twisk.vues;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.MondeIG;
import twisk.outils.TailleComposants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class VueActiviteIG extends VueEtapeIG {
    private final HBox clientWindow;

    public VueActiviteIG(MondeIG monde, EtapeIG etape)
    {
        super(monde, etape);
        monde.ajouterObservateur(this);
        clientWindow = new HBox();
        clientWindow.setStyle("-fx-border-color: #0059FF; " +
                "-fx-background-color: #E9E9E9; " +
                "-fx-background-insets: 0 0 -1 0, 0, 1, 2; " +
                "-fx-background-radius: 3px, 3px, 2px, 1px;");
        clientWindow.setMinSize(TailleComposants.getInstance().getClientsW(), TailleComposants.getInstance().getClientsH());
        reagir();
    }

    public void reagir()
    {
        getChildren().clear();

        BorderPane es = new BorderPane();

        getEtape().resetHauteur();
        if (getEtape().isEntree())
        {
            Image imageEntree = null;
            try {
                imageEntree = new Image(new FileInputStream("src/twisk/ressources/images/entree.png"));
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

        if (getEtape().isSortie())
        {
            Image imageSortie = null;
            try {
                imageSortie = new Image(new FileInputStream("src/twisk/ressources/images/sortie.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ImageView imageViewSortie = new ImageView(imageSortie);
            imageViewSortie.setPreserveRatio(true);
            imageViewSortie.setFitHeight(25);

            BorderPane.setMargin(imageViewSortie, new Insets(10, 0, 0, 0));

            es.setRight(imageViewSortie);
            getEtape().changeHauteur(30);
        }
        setPrefHeight(getEtape().getHauteur());
        getChildren().add(getLabel());
        getChildren().add(clientWindow);
        getChildren().add(es);

        if (getMonde().getEtapesSelectionnes().contains(getEtape()))
        {
            setStyle("-fx-padding: 10px; " +
                    "-fx-background-radius: 10px; " +
                    "-fx-border-radius: 10px; " +
                    "-fx-border-color: #0059FF; " +
                    "-fx-background-color: #348feb;");
        }
        else
        {
            setStyle("-fx-padding: 10px; " +
                    "-fx-background-radius: 10px; " +
                    "-fx-border-radius: 10px; " +
                    "-fx-border-color: #0059FF; " +
                    "-fx-background-color: #FFFFFF;");
        }

    }
}
