package twisk.vues;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import twisk.exceptions.MondeException;
import twisk.mondeIG.ActiviteIG;
import twisk.mondeIG.GuichetIG;
import twisk.mondeIG.MondeIG;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.FileInputStream;

public class VueOutils extends TilePane implements Observateur{
    MondeIG monde;
    ArrayList<Button> buttons;

    public VueOutils(MondeIG monde)
    {
        setPadding(new Insets(5));
        setHgap(10);
        this.monde = monde;

        buttons = new ArrayList<>();

        try {
            FileInputStream input = new FileInputStream("src/main/ressources/images/plus.png");
            Image image = new Image(input);
            ImageView imageViewActivite = new ImageView(image);
            imageViewActivite.setFitHeight(50);
            imageViewActivite.setFitWidth(50);
            Button ajouterActivite = new Button("Activite", imageViewActivite);
            ajouterActivite.setOnAction(e -> monde.ajouter(new ActiviteIG(4, 2)));
            ajouterActivite.setTooltip(new Tooltip("bouton qui permet d’ajouter une activité"));
            buttons.add(ajouterActivite);

            ImageView imageViewGuichet = new ImageView(image);
            imageViewGuichet.setFitHeight(50);
            imageViewGuichet.setFitWidth(50);
            Button ajouterGuichet = new Button("Guichet", imageViewGuichet);
            ajouterGuichet.setOnAction(e -> monde.ajouter(new GuichetIG(2)));
            ajouterGuichet.setTooltip(new Tooltip("bouton qui permet d’ajouter une activité"));
            buttons.add(ajouterGuichet);

            ImageView imageViewPlay = new ImageView(image);
            imageViewPlay.setFitHeight(50);
            imageViewPlay.setFitWidth(50);
            Button play = new Button("Lancer", imageViewPlay);
            play.setOnAction(e -> {
                try {
                    monde.simuler();
                } catch (MondeException ex) {
                    throw new RuntimeException(ex);
                }
            });
            play.setTooltip(new Tooltip("lancer la simulation"));
            buttons.add(play);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        reagir();
    }

    @Override
    public void reagir() {
        getChildren().clear();
        for (Button b : buttons)
        {
            this.getChildren().add(b);
        }
    }
}
