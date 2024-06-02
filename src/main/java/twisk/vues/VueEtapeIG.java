package twisk.vues;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.MondeIG;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Represents an abstract graphical view of an EtapeIG.
 */
public abstract class VueEtapeIG extends VBox implements Observateur {
    private final MondeIG monde;
    private final EtapeIG etape;
    private final Label label;
    private final int x;
    private final int y;
    private final ArrayList<VueClient> clients;

    /**
     * Constructs a new VueEtapeIG.
     *
     * @param monde The MondeIG associated with the VueEtapeIG.
     * @param etape The EtapeIG represented by the VueEtapeIG.
     */
    public VueEtapeIG(MondeIG monde, EtapeIG etape) {
        this.monde = monde;
        this.etape = etape;
        clients = new ArrayList<>();
        x = etape.getX();
        y = etape.getY();
        label = new Label(this.etape.toString());
        label.setStyle("-fx-padding: -5px 0px  5px 0px ;-fx-font-weight: bold;");
        setAlignment(Pos.CENTER);
        setStyle("-fx-padding: 10px; " +
                "-fx-background-radius: 10px; " +
                "-fx-border-radius: 10px; " +
                "-fx-border-color: #0059FF; " +
                "-fx-background-color: #FFFFFF;");
        setPrefSize(etape.getLargeur(), etape.getHauteur());
        setOnMouseClicked(e -> {
            if (monde.isSimulationStopped()) {
                monde.clickEtape(etape);
            }
        });


        setOnDragDetected((MouseEvent event) -> {
            if (monde.isSimulationStopped()) {
                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(Integer.toString(etape.getId()));

                ImageView preview = new ImageView(snapshot(null, null));
                dragboard.setContent(content);
                dragboard.setDragView(preview.getImage());
                event.consume();
            }
        });
    }

    /**
     * Gets the label associated with the VueEtapeIG.
     *
     * @return The label.
     */
    public Label getLabel() {
        return label;
    }

    /**
     * Gets the x-coordinate of the VueEtapeIG.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the VueEtapeIG.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the MondeIG associated with the VueEtapeIG.
     *
     * @return The MondeIG.
     */
    public MondeIG getMonde() {
        return monde;
    }

    /**
     * Gets the EtapeIG represented by the VueEtapeIG.
     *
     * @return The EtapeIG.
     */
    public EtapeIG getEtape() {
        return etape;
    }

    public void add(VueClient client) {
        clients.add(client);
        reagir();
    }

    public Iterator<VueClient> iterator() {
        return clients.iterator();
    }
}
