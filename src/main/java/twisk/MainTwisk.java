package twisk;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import twisk.mondeIG.MondeIG;
import twisk.outils.TailleComposants;
import twisk.outils.ThreadsManager;
import twisk.vues.VueMenu;
import twisk.vues.VueMondeIG;
import twisk.vues.VueOutils;

public class MainTwisk extends Application {
    public void start(Stage stage) {
        stage.setTitle("Twisk");
        MondeIG monde = new MondeIG();
        BorderPane root = new BorderPane();
        root.setTop(new VueMenu(monde));
        root.setCenter(new VueMondeIG(monde));
        root.setBottom(new VueOutils(monde));
        stage.setScene(new Scene(root, TailleComposants.getInstance().getFenetreW(), TailleComposants.getInstance().getFenetreH()));
        stage.setOnCloseRequest(e -> {
            ThreadsManager.getInstance().detruireTout();
            Platform.exit();
        });
        stage.show();
    }
}
