package indy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * It's time for Indy! This is the main class to get things started.
 *
 * Class comments here...
 *
 */

public class App extends Application {

    @Override
    public void start(Stage stage) {
        PaneOrganizer organizer = new PaneOrganizer();
        Scene scene = new Scene(organizer.getRoot(), Constants.APP_WIDTH, Constants.APP_HEIGHT);
        stage.setScene(scene); 
        stage.setTitle("Jetpack Joyride");
        stage.show();

        organizer.getGamePane().requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
