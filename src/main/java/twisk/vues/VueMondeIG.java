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

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class VueMondeIG extends Pane implements Observateur {
    private final MondeIG monde;
    private CopyOnWriteArrayList<VueActiviteIG> activites;
    private CopyOnWriteArrayList<VueGuichetIG> guichets;
    private CopyOnWriteArrayList<VuePointDeControleIG> points;
    private CopyOnWriteArrayList<VueArcIG> arcs;
    private CopyOnWriteArrayList<Circle> clients;
    private int id = 0;

    public VueMondeIG(MondeIG monde) {
        this.monde = monde;
        this.clients = createClients();
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

    private CopyOnWriteArrayList<Circle> createClients() {
        CopyOnWriteArrayList<Circle> circles = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            int color = random.nextInt(5);
            Circle circle = new Circle(5);
            switch (color) {
                case 0:
                    circle.setFill(Color.RED);
                    break;
                case 1:
                    circle.setFill(Color.GREENYELLOW);
                    break;
                case 2:
                    circle.setFill(Color.BLUE);
                    break;
                case 3:
                    circle.setFill(Color.MAGENTA);
                    break;
                case 4:
                    circle.setFill(Color.ORANGE);
                    break;
            }
            circles.add(circle);
        }
        return circles;
    }

    public void reagir() {
        Runnable command = () -> {
            getChildren().clear();
            activites = new CopyOnWriteArrayList<>();
            guichets = new CopyOnWriteArrayList<>();
            points = new CopyOnWriteArrayList<>();
            for (Map.Entry<Integer, EtapeIG> etapeMap : monde) {
                EtapeIG etape = etapeMap.getValue();
                if (etape.estUneActivite()) {
                    activites.add(new VueActiviteIG(monde, etape));
                    for (PointDeControleIG point : etape) {
                        points.add(new VuePointDeControleIG(monde, point));
                    }
                } else if (etape.estUnGuichet()) {
                    guichets.add(new VueGuichetIG(monde, etape));
                    for (PointDeControleIG point : etape) {
                        points.add(new VuePointDeControleIG(monde, point));
                    }
                }
            }
            if(monde.getGestionnaireClients() != null) {
                int id = 0;
                Iterator<Client> iter = monde.getGestionnaireClients().iterator();
                while (iter.hasNext()) {
                    Client client = iter.next();
                    EtapeIG etp = monde.getCorrespondance().getIG(client.getEtapeActuelle());
                    if (etp != null) {
                        if (etp.estUneActivite()) {
                            ActiviteIG a = (ActiviteIG) etp;
                            Random random = new Random();
                            int randomX = random.nextInt(210);
                            int randomY = random.nextInt(55);
                            int x = a.getX() + 20 + randomX;
                            int y = a.getY() + 35 + randomY;
                            clients.get(id).setCenterX(x);
                            clients.get(id).setCenterY(y);
                            id++;
                        }
                        if (etp.estUnGuichet()) {
                            assert etp instanceof GuichetIG;
                            GuichetIG a = (GuichetIG) etp;
                            int mult = client.getRang();
                            if (mult > 8) {
                                mult = 8;
                            }
                            int x = a.getX() + 200 - (22 * mult);
                            int y = a.getY() + 40;
                            clients.get(id).setCenterX(x);
                            clients.get(id).setCenterY(y);
                            id++;
                        }
                    }
                }
            }

            arcs = new CopyOnWriteArrayList<>();
            monde.refreshArcs();
            for (Iterator<ArcIG> it = monde.arcs(); it.hasNext(); ) {
                ArcIG arc = it.next();
                arcs.add(new VueArcIG(monde, arc));
            }

            for (VueActiviteIG activite : activites) {
                activite.relocate(activite.getX(), activite.getY());
            }
            for (VueGuichetIG vueGuichetIG : guichets) {
                vueGuichetIG.relocate(vueGuichetIG.getX(), vueGuichetIG.getY());
            }

            getChildren().addAll(arcs);
            getChildren().addAll(activites);
            getChildren().addAll(guichets);
            getChildren().addAll(points);
            getChildren().addAll(clients);
        };

        if (Platform.isFxApplicationThread()) {
            command.run();
        } else {
            Platform.runLater(command);
        }
    }
}
