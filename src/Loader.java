package src;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Loader extends Application {
    @Override
    /**
     * Classe permettant de charger la vue depuis le fichier view.fxml
     */
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));

        Parent root = loader.load();
        System.out.println(loader.getController().getClass());
        Scene scene = new Scene(root);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        stage.setScene(scene);
        stage.show();
    }
}
