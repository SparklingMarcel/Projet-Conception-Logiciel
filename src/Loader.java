package src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Loader extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));

        Parent root = loader.load();
        System.out.println(loader.getController().getClass());
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}
