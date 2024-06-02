package twisk.vues;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import twisk.mondeIG.ArcIG;
import twisk.mondeIG.MondeIG;

/**
 * Represents the graphical view of an ArcIG.
 */
public class VueArcIG extends Pane {
    private final ArcIG arc;
    private final MondeIG monde;

    /**
     * Constructs a new VueArcIG.
     *
     * @param monde The MondeIG associated with the VueArcIG.
     * @param arc   The ArcIG represented by the VueArcIG.
     */
    public VueArcIG(MondeIG monde, ArcIG arc) {
        this.monde = monde;
        this.arc = arc;

        // Create a line representing the arc
        double p1x = arc.getP1().getX();
        double p1y = arc.getP1().getY();
        double p2x = arc.getP2().getX();
        double p2y = arc.getP2().getY();
        Line line = new Line(p1x, p1y, p2x, p2y);
        line.setStrokeWidth(3);

        // Create a polyline to display an arrowhead
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

        // Set event handler for mouse click on the arc
        setOnMouseClicked(e -> {
            if (monde.isSimulationStopped()) {
                monde.clickArc(arc);
            }
        });

        // Colorize the arc based on its presence in the monde
        coloriser(line, p);

        // Add the line and polyline to the Pane
        getChildren().addAll(p, line);
    }

    /**
     * Colorizes the arc based on its presence in the monde.
     *
     * @param line     The line representing the arc.
     * @param polyline The polyline representing the arrowhead.
     */
    private void coloriser(Line line, Polyline polyline) {
        if (monde.contains(arc)) {
            // Arc is present in the monde
            polyline.setFill(Color.DARKBLUE);
            line.setStroke(Color.DARKBLUE);
        } else {
            // Arc is not present in the monde
            polyline.setFill(Color.DEEPPINK);
            line.setStroke(Color.DEEPPINK);
        }
    }
}
