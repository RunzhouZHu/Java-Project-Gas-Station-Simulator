package controller;

import framework.Trace;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.MyEngine;
import view.SimulatorView;

public class SimulatorController {
    static MyEngine m = new MyEngine();


    @FXML
    private Button startSimulationButton;

    @FXML
    private void startSimulationButtonClicked() {
        System.out.println("startSimulationButtonClicked() called");

        Trace.setTraceLevel(Trace.Level.INFO);


        m.setSimulationTime(1000);
        m.run();

    }

    public static void main(String[] args) {
        SimulatorView.launch(SimulatorView.class);
    }
}
