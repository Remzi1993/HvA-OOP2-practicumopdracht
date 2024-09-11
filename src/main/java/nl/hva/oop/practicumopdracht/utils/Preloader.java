package nl.hva.oop.practicumopdracht.utils;

import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import nl.hva.oop.practicumopdracht.MainApplication;

/**
 * Minimal reproducible example (MRE) - Example of a simple JavaFX preloader class.
 * @author Remzi Cavdar - ict@remzi.info - <a href="https://github.com/Remzi1993">@Remzi1993</a>
 */
public class Preloader extends javafx.application.Preloader {
    private static final ProgressBar PROGRESS_BAR = new ProgressBar();
    private static Stage stage;
    private static final boolean DEBUG = false;

    private Scene getScene() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(PROGRESS_BAR);
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
    public void handleProgressNotification(ProgressNotification pn) {
        PROGRESS_BAR.setProgress(pn.getProgress());
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            if (DEBUG) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            stage.hide();
        }
    }
}