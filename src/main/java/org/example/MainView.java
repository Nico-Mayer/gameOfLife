package org.example;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class MainView extends VBox {
    private int canvasWidth = 400;
    private int canvasHeight = 400;
    private int cols = 30;
    private int rows = 30;

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
        canvas = new Canvas(canvasWidth,canvasHeight);

        this.getChildren().addAll(this.stepButton, this.canvas);

        this.affine = new Affine();
        this.affine.appendScale(canvasWidth/(float)cols, canvasHeight/(float)rows);

        this.simulation = new Simulation(cols, rows);

        simulation.setAlive(2,2);
        simulation.setAlive(3,2);
        simulation.setAlive(4,2);

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
