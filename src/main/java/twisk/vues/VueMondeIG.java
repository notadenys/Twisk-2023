package twisk.vues;
import javafx.application.Platform;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import twisk.mondeIG.*;
import twisk.simulation.Client;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class VueMondeIG extends Pane implements Observateur {
    private final MondeIG monde;
    private final ArrayList<VueActiviteIG> activites;
    private final ArrayList<VueGuichetIG> guichets;
    private final ArrayList<VuePointDeControleIG> points;
    private final ArrayList<VueArcIG> arcs;

    public VueMondeIG(MondeIG monde) {
        this.monde = monde;
        activites = new ArrayList<>();
        guichets = new ArrayList<>();
        points = new ArrayList<>();
        arcs = new ArrayList<>();
        monde.ajouterObservateur(this);

        setOnDragOver((DragEvent event) -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                EtapeIG etape = monde.getEtape(Integer.parseInt(db.getString()));
                etape.move((int) event.getX() - (etape.getLargeur() / 2), (int) event.getY() - (etape.getHauteur() / 2));
                monde.setEnAttente(false);
                reagir();
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });

        reagir();
    }

    private void setUpClients(VueEtapeIG etapeIG) {
        if (!monde.isSimulationStopped()) {
            for (Client client : monde.getGestionnaireClients()) {
                    if (client.getEtapeActuelle() != null) {
                        if (client.getEtapeActuelle().getNom().equals(etapeIG.getEtape().getNom())) {
                            etapeIG.add(new VueClient(client));
                        }
                    }
            }
        }
    }

    public void reagir() {
        Runnable command = () -> {
            getChildren().clear();
            activites.clear();
            guichets.clear();
            points.clear();
            for (Map.Entry<Integer, EtapeIG> etapeMap : monde) {
                EtapeIG etape = etapeMap.getValue();
                if (etape.estUneActivite()) {
                    VueActiviteIG activite = new VueActiviteIG(monde, etape);
                    setUpClients(activite);
                    activites.add(activite);
                    for (PointDeControleIG point : etape) {
                        points.add(new VuePointDeControleIG(monde, point));
                    }
                } else if (etape.estUnGuichet()) {
                    VueGuichetIG guichet = new VueGuichetIG(monde, etape);
                    setUpClients(guichet);
                    guichets.add(guichet);
                    for (PointDeControleIG point : etape) {
                        points.add(new VuePointDeControleIG(monde, point));
                    }
                }
            }

            arcs.clear();
            monde.refreshArcs();
            for(Iterator<ArcIG> it = monde.arcs(); it.hasNext(); ) {
                ArcIG arc = it.next();
                arcs.add(new VueArcIG(monde, arc));
            }

            for(VueActiviteIG activite : activites) {
                activite.relocate(activite.getX(), activite.getY());
            }
            for(VueGuichetIG vueGuichetIG : guichets) {
                vueGuichetIG.relocate(vueGuichetIG.getX(), vueGuichetIG.getY());
            }
            getChildren().addAll(arcs);
            getChildren().addAll(activites);
            getChildren().addAll(guichets);
            getChildren().addAll(points);
        };

        if (Platform.isFxApplicationThread()) {
            command.run();
        } else {
            Platform.runLater(command);
        }
    }
}
