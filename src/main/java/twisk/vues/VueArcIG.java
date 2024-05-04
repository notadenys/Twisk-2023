package twisk.vues;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import twisk.mondeIG.ArcIG;
import twisk.mondeIG.MondeIG;

public class VueArcIG extends Pane {
    private final ArcIG arc;
    private final MondeIG monde;

    public VueArcIG(MondeIG monde, ArcIG arc)
    {
        this.monde = monde;
        this.arc = arc;

        double p1x = arc.getP1().getX();
        double p1y = arc.getP1().getY();
        double p2x = arc.getP2().getX();
        double p2y = arc.getP2().getY();
        Line line = new Line(p1x, p1y, p2x, p2y);
        line.setStrokeWidth(3);

        Polyline p = new Polyline();
        double angle = Math.atan2((p2y - p1y), (p2x - p1x)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 15 + p2x;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 15 + p2y;
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 15 + p2x;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 15 + p2y;
        p.strokeProperty().bind(p.fillProperty());
        p.getPoints().addAll(x1, y1, x2, y2, p2x, p2y, x1, y1);

        setOnMouseClicked(e -> monde.clickArc(arc));

        coloriser(line, p);
        getChildren().addAll(p, line);
    }

    private void coloriser(Line line, Polyline polyline)
    {
        if (monde.contains(arc))
        {
            polyline.setFill(Color.DARKBLUE);
            line.setStroke(Color.DARKBLUE);
        }
        else
        {
            polyline.setFill(Color.DEEPPINK);
            line.setStroke(Color.DEEPPINK);
        }
    }
}
