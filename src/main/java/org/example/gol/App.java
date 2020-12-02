package org.example.gol;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.gol.viewmodel.ApplicationState;
import org.example.gol.viewmodel.ApplicationViewModel;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        ApplicationViewModel applicationViewModel = new ApplicationViewModel(ApplicationState.EDITING);
        MainView mainview = new MainView(applicationViewModel);
        Scene scene = new Scene(mainview, 640, 480);
        stage.setScene(scene);
        stage.show();

        mainview.draw();
    }

    public static void main(String[] args) {
        launch();
    }

}