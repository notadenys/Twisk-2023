package twisk.vues;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import twisk.mondeIG.ActiviteIG;
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
        this.monde = monde;

        buttons = new ArrayList<>();

        try {
            FileInputStream input = new FileInputStream("src/main/ressources/images/plus.png");
            Image image = new Image(input);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            Button button = new Button("", imageView);
            button.setOnAction(e -> monde.ajouter(new ActiviteIG(4, 2)));
            button.setTooltip(new Tooltip("bouton qui permet d’ajouter une activité"));
            buttons.add(button);
        } catch (FileNotFoundException e)
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
