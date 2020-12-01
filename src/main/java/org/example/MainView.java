package org.example;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MainView extends VBox {
    private int canvasWidth = 400;
    private int canvasHeight = 400;
    private int cols = 30;
    private int rows = 30;

    private int drawMode = 1;

    private Button stepButton;
    private Canvas canvas;
    private Affine affine;

    private Simulation simulation;

    public MainView() {
        stepButton = new Button("step");
        this.stepButton.setOnAction(actionEvent -> {
            this.simulation.nextGeneration();
            draw();
        });
        this.canvas = new Canvas(canvasWidth,canvasHeight);
        // Action Listeners
        this.canvas.setOnMousePressed(this::handleDraw);
        this.canvas.setOnMouseDragged(this::handleDraw);
        this.setOnKeyPressed(this::onKeyPressed);

        this.getChildren().addAll(this.stepButton, this.canvas);

        this.affine = new Affine();
        this.affine.appendScale(canvasWidth/(float)cols, canvasHeight/(float)rows);

        this.simulation = new Simulation(cols, rows);
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.D){
            this.drawMode = 1;
        }else if(keyEvent.getCode() == KeyCode.E){
            this.drawMode = 0;
        }
    }

    private void handleDraw(MouseEvent mouseEvent) {
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();

        try {
            Point2D simCoord = this.affine.inverseTransform(mouseX, mouseY);
            int simX = (int)simCoord.getX();
            int simY = (int)simCoord.getY();
            this.simulation.setState(simX, simY, drawMode);
            draw();

            System.out.println(simX + " " + simY);
        } catch (NonInvertibleTransformException e) {
            System.out.println("could not invert mouse coordinates");
        }
    }

    public void draw(){
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setTransform(this.affine);

        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0,0,canvasWidth,canvasHeight);
        g.setFill(Color.BLACK);
        for (int x = 0; x < simulation.width; x++) {
            for (int y = 0; y < simulation.height; y++) {
                if (this.simulation.isAlive(x, y) == 1) {
                    g.fillRect(x, y, 1, 1);
                }
            }
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
}
