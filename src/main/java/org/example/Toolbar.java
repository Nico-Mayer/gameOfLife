package org.example;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

public class Toolbar extends ToolBar {
    private MainView mainView;

    public Toolbar(MainView mainView){
        this.mainView = mainView;
        Button draw = new Button("Draw");
        draw.setOnAction(this::handleDraw);
        Button erase = new Button("Erase");
        erase.setOnAction(this::handleErase);
        Button step = new Button("Step");
        step.setOnAction(this::handleStep);
        Button reset = new Button("Reset");
        reset.setOnAction(this::handleReset);

        this.getItems().addAll(reset, draw, erase, step);
    }

    private void handleReset(ActionEvent actionEvent) {
        this.mainView.setAppState(MainView.EDITING);
        this.mainView.draw();
    }

    private void handleStep(ActionEvent actionEvent) {
        this.mainView.setAppState(MainView.SIMULATING);
        this.mainView.getSimulation().nextGeneration();
        this.mainView.draw();
    }

    private void handleErase(ActionEvent actionEvent) {
        this.mainView.setDrawMode(Simulation.DEAD);
    }

    private void handleDraw(ActionEvent actionEvent) {
        this.mainView.setDrawMode(Simulation.ALIVE);
    }
}
