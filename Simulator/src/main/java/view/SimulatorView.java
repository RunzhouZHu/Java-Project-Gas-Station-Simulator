package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
/**
 * The SimulatorView class is the main entry point for the JavaFX application.
 * It loads the FXML layout and sets up the primary stage for the simulator.
 */
public class SimulatorView extends Application {
    /**
     * Starts the JavaFX application by loading the FXML layout and setting up the primary stage.
     *
     * @param stage the primary stage for this application
     * @throws Exception if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/simulator_view.fxml"));
        Parent root = fxmlLoader.load();


        stage.setTitle("Simulator");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
