package twisk.vues;

import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import twisk.exceptions.TwiskException;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.PointDeControleIG;

public class VuePointDeControleIG extends Circle {
    PointDeControleIG point;
    MondeIG monde;
    public VuePointDeControleIG(MondeIG monde, PointDeControleIG point)
    {
        this.monde = monde;
        this.point = point;
        setCenterX(point.getX());
        setCenterY(point.getY());
        setRadius(6);
        if (monde.isEnAttente() && monde.getPointMemorise() == point)
        {
            setFill(Color.BLUE);
        }
        else
        {
            setFill(Color.DEEPPINK);
        }

        setOnMouseClicked(e -> {
            if (monde.isEnAttente())
            {
                try {
                    monde.ajouter(monde.getPointMemorise().getId(), point.getId());
                }
                catch (TwiskException exc){
                    monde.setEnAttente(false);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Liaison impossible");
                    alert.setHeaderText(null);
                    alert.setContentText(exc.getMessage());
                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.play();
                    pause.setOnFinished(ev -> alert.close());
                    alert.showAndWait();
                }
            }
            else {
                monde.setPointMemorise(point);
                monde.setEnAttente(true);
            }
            monde.notifierObservateurs();
        });


        setOnDragDetected((MouseEvent event) -> {
            Dragboard dragboard = startDragAndDrop(TransferMode.LINK);
            ClipboardContent content = new ClipboardContent();
            content.putString(point.getId());

            dragboard.setContent(content);
            event.consume();
        });

        setOnDragOver((DragEvent event) -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.LINK);
            }
            event.consume();
        });

        setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                try {
                    monde.ajouter(db.getString(), point.getId());
                }
                catch (TwiskException exception)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Liaison impossible");
                    alert.setHeaderText(null);
                    alert.setContentText(exception.getMessage());
                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.play();
                    pause.setOnFinished(ev -> alert.close());
                    alert.showAndWait();
                }

                monde.setEnAttente(false);
                monde.notifierObservateurs();
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }
}
