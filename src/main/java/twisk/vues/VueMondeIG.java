package twisk.vues;

import javafx.application.Platform;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import twisk.mondeIG.ArcIG;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.PointDeControleIG;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class VueMondeIG extends Pane implements Observateur{
    private final MondeIG monde;
    private final ArrayList<VueActiviteIG> activites;
    private final ArrayList<VueGuichetIG> guichets;
    private final ArrayList<VuePointDeControleIG> points;
    private final ArrayList<VueArcIG> arcs;

    public VueMondeIG(MondeIG monde)
    {
        this.monde = monde;
        monde.ajouterObservateur(this);
        activites = new ArrayList<>();
        guichets = new ArrayList<>();
        points = new ArrayList<>();
        arcs = new ArrayList<>();

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
                etape.move((int)event.getX()-(etape.getLargeur()/2), (int)event.getY()-(etape.getHauteur()/2));
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

    public void reagir()
    {
        Runnable command = () -> {
            getChildren().clear();
            activites.clear();
            guichets.clear();
            points.clear();
            for (Map.Entry<Integer,EtapeIG> etapeMap : monde)
            {
                EtapeIG etape = etapeMap.getValue();
                if (etape.estUneActivite())
                {
                    activites.add(new VueActiviteIG(monde, etape));
                    for (PointDeControleIG point : etape)
                    {
                        points.add(new VuePointDeControleIG(monde, point));
                    }
                }
                else if (etape.estUnGuichet())
                {
                    guichets.add(new VueGuichetIG(monde, etape));
                    for (PointDeControleIG point : etape)
                    {
                        points.add(new VuePointDeControleIG(monde, point));
                    }
                }

            }
            arcs.clear();
            monde.refreshArcs();
            Iterator<ArcIG> iterator = monde.arcs();
            while (iterator.hasNext())
            {
                ArcIG arc = iterator.next();
                arcs.add(new VueArcIG(monde, arc));
            }
            for (VueActiviteIG activite : activites)
            {
                activite.relocate(activite.getX(), activite.getY());
            }
            for (VueGuichetIG vueGuichetIG : guichets)
            {
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
