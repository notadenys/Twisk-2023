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
    private ArrayList<VueActiviteIG> activites;
    private ArrayList<VueGuichetIG> guichets;
    private ArrayList<VuePointDeControleIG> points;
    private ArrayList<VueArcIG> arcs;
    private ArrayList<Circle> clients;

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

    private ArrayList<Circle> createClients() {
        ArrayList<Circle> circles = new ArrayList<>();
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
            circle.setVisible(true);
            circles.add(circle);
        }
        return circles;
    }

    public void reagir() {
        Runnable command = () -> {
            getChildren().clear();
            activites = new ArrayList<>();
            guichets = new ArrayList<>();
            points = new ArrayList<>();
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

            if (monde.getGestionnaireClients() != null) {
                int id = 0;
                for (Client client : monde.getGestionnaireClients()) {
                    EtapeIG etp = monde.getCorrespondance().getEtapeIG(client.getEtapeActuelle());
                    if (etp != null) {
                        if (etp.estUneActivite() || etp.estUneSortie()) {
                            ActiviteIG a = (ActiviteIG) etp;
                            Random random = new Random();
                            int randomX = random.nextInt(210);
                            int randomY = random.nextInt(55);
                            System.out.println(client.getEtapeActuelle());
                            int x = a.getX() + 20 + randomX;
                            int y = a.getY() + 35 + randomY;
                            clients.get(id).setCenterX(x);
                            clients.get(id).setCenterY(y);
                            clients.get(id).setVisible(true);
                            id++;
                        } else if (etp.estUnGuichet()) {
                            GuichetIG a = (GuichetIG) etp;
                            int mult = client.getRang();
                            if (mult > 8) {
                                mult = 8;
                            }
                            int x = a.getX() + 200 - (22 * mult);
                            int y = a.getY() + 40;
                            clients.get(id).setCenterX(x);
                            clients.get(id).setCenterY(y);
                            clients.get(id).setVisible(true);
                            id++;
                        }
                    }
                }
            }

            arcs = new ArrayList<>();
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
            getChildren().addAll(clients);
        };

        if (Platform.isFxApplicationThread()) {
            command.run();
        } else {
            Platform.runLater(command);
        }
    }
}
