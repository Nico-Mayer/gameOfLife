package org.example.gol;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import org.example.gol.model.Board;
import org.example.gol.model.BoundedBoard;
import org.example.gol.model.CellState;
import org.example.gol.model.StandardRule;
import org.example.gol.viewmodel.ApplicationState;
import org.example.gol.viewmodel.ApplicationViewModel;

public class MainView extends VBox {
    private int canvasWidth = 400;
    private int canvasHeight = 400;
    private int cols = 30;
    private int rows = 30;

    private CellState drawMode = CellState.ALIVE;

    private Infobox infobox;
    private Canvas canvas;
    private Affine affine;

    private Simulation simulation;
    private Board initalSimulation;
    private ApplicationViewModel appViewModel;
    private boolean isDrawingEnabeld = true;
    private boolean drawInitialBoard = true;

    public MainView(ApplicationViewModel appViewModel) {
        //ToolBar
        Toolbar toolbar = new Toolbar(this, appViewModel);
        // InfoBox
        this.infobox = new Infobox();
        this.infobox.setDrawMode(this.drawMode);
        this.infobox.setCursorPosition(0, 0);
        // App View Model
        this.appViewModel = appViewModel;
        this.appViewModel.listenToAppState(this::onApplicationStateChanged);

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

        this.initalSimulation = new BoundedBoard(cols, rows);
    }
    private void onApplicationStateChanged(ApplicationState state){
        if(state == ApplicationState.EDITING){
            this.isDrawingEnabeld = true;
            this.drawInitialBoard = true;
        }else if(state == ApplicationState.SIMULATING){
            this.isDrawingEnabeld = false;
            this.drawInitialBoard = false;
            this.simulation = new Simulation(this.initalSimulation, new StandardRule());
        }else{
            throw new IllegalArgumentException("unsupported application state" + state.name());
        }
    }

    private void handleMoved(MouseEvent mouseEvent) {
        Point2D simCoord = this.getSimulationCoordinates(mouseEvent);
        int simX = (int)simCoord.getX();
        int simY = (int)simCoord.getY();
        this.infobox.setCursorPosition(simX, simY);
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.D){
            this.drawMode = CellState.ALIVE;
        }else if(keyEvent.getCode() == KeyCode.E){
            this.drawMode = CellState.DEAD;
        }
    }

    private void handleDraw(MouseEvent mouseEvent) {
            if(!isDrawingEnabeld){
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

        if(isDrawingEnabeld){
            drawSimulation(this.initalSimulation);
        }else{
            drawSimulation(this.simulation.getBoard());
        }

        g.setStroke(Color.GRAY);
        g.setLineWidth(0.05f);
        for (int x = 0; x <= this.initalSimulation.getWidth(); x++) {
            g.strokeLine(x, 0, x, rows);
        }

        for (int y = 0; y <= this.initalSimulation.getHeight(); y++) {
            g.strokeLine(0, y, cols, y);
        }
    }
    private void drawSimulation(Board simulationToDraw){
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        for (int x = 0; x < simulationToDraw.getWidth(); x++) {
            for (int y = 0; y < simulationToDraw.getHeight(); y++) {
                if (simulationToDraw.getState(x, y) == CellState.ALIVE) {
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    public Simulation getSimulation() {
        return this.simulation;
    }

    public void setDrawMode(CellState mode) {
        this.drawMode = mode;
        this.infobox.setDrawMode(mode);
    }
}
