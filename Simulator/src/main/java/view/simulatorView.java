package view;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;


public class simulatorView extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
            getClass().getResource("/fxml/simulator_view.fxml")
        );
        Parent root = fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.show();
    }

}
