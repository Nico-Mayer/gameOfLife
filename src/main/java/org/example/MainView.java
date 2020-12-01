package org.example;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MainView extends VBox {
    public static final int EDITING = 0;
    public static final int SIMULATING = 1;

    private int canvasWidth = 400;
    private int canvasHeight = 400;
    private int cols = 30;
    private int rows = 30;
    private int appState = EDITING;

    private int drawMode = Simulation.ALIVE;

    private Infobox infobox;
    private Canvas canvas;
    private Affine affine;

    private Simulation simulation;
    private Simulation initalSimulation;

    public MainView() {
        //ToolBar
        Toolbar toolbar = new Toolbar(this);
        // InfoBox
        this.infobox = new Infobox();
        this.infobox.setDrawMode(this.drawMode);
        this.infobox.setCursorPosition(0, 0);

        // This Pane Fills the Gap between infobox and canvas
        Pane spacer = new Pane();
        spacer.setMinSize(0, 0);
        spacer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(spacer, Priority.ALWAYS);

        this.canvas = new Canvas(canvasWidth,canvasHeight);
        // Action Listeners
        this.canvas.setOnMousePressed(this::handleDraw);
        this.canvas.setOnMouseDragged(this::handleDraw);
        this.canvas.setOnMouseMoved(this::handleMoved);
        this.setOnKeyPressed(this::onKeyPressed);

        this.getChildren().addAll(toolbar, this.canvas, spacer, infobox);

        this.affine = new Affine();
        this.affine.appendScale(canvasWidth/(float)cols, canvasHeight/(float)rows);

        this.initalSimulation = new Simulation(cols, rows);
        this.simulation = Simulation.copy(initalSimulation);
    }

    private void handleMoved(MouseEvent mouseEvent) {
        Point2D simCoord = this.getSimulationCoordinates(mouseEvent);
        int simX = (int)simCoord.getX();
        int simY = (int)simCoord.getY();
        this.infobox.setCursorPosition(simX, simY);
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.D){
            this.drawMode = Simulation.ALIVE;
        }else if(keyEvent.getCode() == KeyCode.E){
            this.drawMode = Simulation.DEAD;
        }
    }

    private void handleDraw(MouseEvent mouseEvent) {
            if(this.appState == SIMULATING){
                return;
            }
            Point2D simCoord = this.getSimulationCoordinates(mouseEvent);
            int simX = (int)simCoord.getX();
            int simY = (int)simCoord.getY();

            this.initalSimulation.setState(simX, simY, drawMode);
            draw();
    }

    private Point2D getSimulationCoordinates(MouseEvent event){
        double mouseX = event.getX();
        double mouseY = event.getY();
        try {
            Point2D simCoord = this.affine.inverseTransform(mouseX, mouseY);
            return simCoord;
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException("Non invertable transform");
        }
    }

    public void draw(){
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setTransform(this.affine);

        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0,0,canvasWidth,canvasHeight);

        if(this.appState == EDITING){
            drawSimulation(this.initalSimulation);
        }else{
            drawSimulation(this.simulation);
        }

        g.setStroke(Color.GRAY);
        g.setLineWidth(0.05f);
        for (int x = 0; x <= this.simulation.width; x++) {
            g.strokeLine(x, 0, x, rows);
        }

        for (int y = 0; y <= this.simulation.height; y++) {
            g.strokeLine(0, y, cols, y);
        }
    }
    private void drawSimulation(Simulation simulationToDraw){
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        for (int x = 0; x < simulationToDraw.width; x++) {
            for (int y = 0; y < simulationToDraw.height; y++) {
                if (simulationToDraw.isAlive(x, y) == Simulation.ALIVE) {
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    public Simulation getSimulation() {
        return this.simulation;
    }

    public void setDrawMode(int mode) {
        this.drawMode = mode;
        this.infobox.setDrawMode(mode);
    }

    public void setAppState(int appState) {
        if(appState == this.appState){
            return;
        }
        if(appState == SIMULATING) {
            this.simulation = Simulation.copy(initalSimulation);
        }
        this.appState = appState;
    }
}
