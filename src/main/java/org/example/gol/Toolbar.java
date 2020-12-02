package org.example.gol;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import org.example.gol.model.CellState;
import org.example.gol.viewmodel.ApplicationState;
import org.example.gol.viewmodel.ApplicationViewModel;

public class Toolbar extends ToolBar {
    private MainView mainView;
    private Simulator simulator;
    private ApplicationViewModel appViewModel;

    public Toolbar(MainView mainView, ApplicationViewModel appViewModel){
        this.appViewModel = appViewModel;
        this.mainView = mainView;
        Button draw = new Button("Draw");
        draw.setOnAction(this::handleDraw);
        Button erase = new Button("Erase");
        erase.setOnAction(this::handleErase);
        Button step = new Button("Step");
        step.setOnAction(this::handleStep);
        Button reset = new Button("Reset");
        reset.setOnAction(this::handleReset);
        Button start = new Button("Play");
        start.setOnAction(this::handleStart);
        Button stop = new Button("Stop");
        stop.setOnAction(this::handleStop);

        this.getItems().addAll(start, stop, reset, draw, erase, step);
    }

    private void handleStop(ActionEvent actionEvent) {
        this.simulator.stop();
    }

    private void handleStart(ActionEvent actionEvent) {
        switchToSimulatingState();
        this.simulator.start();
    }

    private void handleReset(ActionEvent actionEvent) {
        this.appViewModel.setCurrentState(ApplicationState.EDITING);
        this.simulator = null;
        this.mainView.draw();
    }

    private void handleStep(ActionEvent actionEvent) {
        switchToSimulatingState();
        this.mainView.getSimulation().nextGeneration();
        this.mainView.draw();
    }
    private void switchToSimulatingState(){
        this.appViewModel.setCurrentState(ApplicationState.SIMULATING);
        this.simulator = new Simulator(this.mainView, this.mainView.getSimulation());
    }

    private void handleErase(ActionEvent actionEvent) {
        this.mainView.setDrawMode(CellState.DEAD);
    }

    private void handleDraw(ActionEvent actionEvent) {
        this.mainView.setDrawMode(CellState.ALIVE);
    }
}
