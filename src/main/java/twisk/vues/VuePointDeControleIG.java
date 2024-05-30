package twisk.vues;

import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
                monde.ajouter(monde.getPointMemorise().getId(), point.getId());
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
                monde.ajouter(db.getString(), point.getId());
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
