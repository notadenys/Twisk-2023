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
import twisk.outils.ThreadsManager;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.io.FileInputStream;

public class VueOutils extends TilePane implements Observateur{
    MondeIG monde;
    ArrayList<Button> buttons;
    boolean simulation ;

    public VueOutils(MondeIG monde)
    {
        setPadding(new Insets(5));
        setHgap(10);
        this.monde = monde;
        simulation = false;

        buttons = new ArrayList<>();

        try {
            FileInputStream input = new FileInputStream("src/main/ressources/images/plus.png");
            FileInputStream input2 = new FileInputStream("src/main/ressources/images/play.png");
            FileInputStream input3 = new FileInputStream("src/main/ressources/images/stop.png");

            Image image = new Image(input);
            Image image2 = new Image(input2);
            Image image3 = new Image(input3);
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

            ImageView imageViewStop = new ImageView(image3);
            imageViewStop.setFitHeight(50);
            imageViewStop.setFitWidth(50);

            ImageView imageViewPlay = new ImageView(image2);
            imageViewPlay.setFitHeight(50);
            imageViewPlay.setFitWidth(50);
            Button play = new Button("Lancer", imageViewPlay);
            play.setOnAction(e -> {
                if(!simulation) {
                    try {
                        monde.simuler();
                    } catch (MondeException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (InvocationTargetException ex) {
                        throw new RuntimeException(ex);
                    } catch (NoSuchMethodException ex) {
                        throw new RuntimeException(ex);
                    } catch (InstantiationException ex) {
                        throw new RuntimeException(ex);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                    play.setGraphic(imageViewStop);
                    play.setText("Stop");
                    simulation = true;
                    this.monde = monde;
                } else {
                    ThreadsManager.getInstance().detruireTout();
                    simulation = false;
                    play.setGraphic(imageViewPlay);
                    play.setText("Lancer");

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
        for (Button b : buttons) {
            this.getChildren().add(b);
        }
    }
}
