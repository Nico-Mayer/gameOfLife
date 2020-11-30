package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        MainView mainview = new MainView();
        Scene scene = new Scene(mainview, 640, 480);
        stage.setScene(scene);
        stage.show();

        mainview.draw();
    }

    public static void main(String[] args) {
        launch();
    }

}