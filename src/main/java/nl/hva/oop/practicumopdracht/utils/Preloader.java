package nl.hva.oop.practicumopdracht.utils;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import nl.hva.oop.practicumopdracht.Main;
import nl.hva.oop.practicumopdracht.MainApplication;

/**
 * Minimal reproducible example (MRE) - Example of a simple JavaFX preloader class.
 * @author Remzi Cavdar - ict@remzi.info - <a href="https://github.com/Remzi1993">@Remzi1993</a>
 */
public class Preloader extends javafx.application.Preloader {
    private static Stage stage;
    private static final boolean DEBUG = Main.DEBUG;

    private Scene getScene() {
        BorderPane borderPane = new BorderPane();

        // Create a ProgressIndicator (which has a built-in spinning animation when indeterminate)
        ProgressIndicator progressCircle = new ProgressIndicator();
        // Explicitly set it to indeterminate mode
        progressCircle.setProgress(-1F);

        // Set the ProgressIndicator in the centre of the BorderPane
        borderPane.setCenter(progressCircle);
        // Set background to white
        borderPane.setStyle("-fx-background-color: white;");

        return new Scene(borderPane, 300, 150);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Preloader.stage = stage;
        stage.getIcons().add(new MainApplication().getAppIcon());
        stage.setTitle("Laden van applicatie");
        stage.setResizable(false);
        stage.setScene(getScene());
        stage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            if (DEBUG) {
                // Use PauseTransition to delay the preloader for 5 seconds in DEBUG mode
                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                pause.setOnFinished(_ -> {
                    stage.hide();  // Hide the preloader after the delay
                });
                pause.play();
            } else {
                stage.hide();  // Immediately hide the preloader in non-DEBUG mode
            }
        }
    }
}