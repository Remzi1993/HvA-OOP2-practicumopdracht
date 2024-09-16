package nl.hva.oop.practicumopdracht.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.hva.oop.practicumopdracht.MainApplication;
import nl.hva.oop.practicumopdracht.utils.Hyperlink;
import java.io.InputStream;
import static nl.hva.oop.practicumopdracht.MainApplication.APP_VERSION;

/**
 * AboutView - The info and support view. This view shows the information about the application and the developer.
 * This is a basic view with no functionality or any business logic therefore it doesn't need a controller.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class AboutView {
    public AboutView() {
        Stage stage = new Stage();
        stage.getIcons().add(new MainApplication().getAppIcon());
        stage.setTitle("Informatie & contactgegevens - versie: " + APP_VERSION);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(getScene());
        stage.show();
    }

    private Scene getScene() {
        // VBox parent container
        VBox vboxContainer = new VBox();
        vboxContainer.setAlignment(Pos.CENTER);
        vboxContainer.getStyleClass().add("about-view-vbox-container");
        // HBox container for the images
        HBox hbox = new HBox();

        String path1 = "images/emoji/zany-face.gif";
        ImageView img1;
        try (InputStream inputStream = MainApplication.class.getResourceAsStream(path1)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found as resource: " + path1);
            }
            img1 = new ImageView(new Image(inputStream, 100, 100, true,
                    true));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load the application icon.", e);
        }

        String path2 = "images/emoji/star-struck.gif";
        ImageView img2;
        try (InputStream inputStream = MainApplication.class.getResourceAsStream(path2)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found as resource: " + path2);
            }
            img2 = new ImageView(new Image(inputStream, 100, 100, true,
                    true));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load the application icon.", e);
        }

        String path3 = "images/emoji/partying-face.gif";
        ImageView img3;
        try (InputStream inputStream = MainApplication.class.getResourceAsStream(path3)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found as resource: " + path3);
            }
            img3 = new ImageView(new Image(inputStream, 100, 100, true,
                    true));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load the application icon.", e);
        }

        String path4 = "images/emoji/rocket.gif";
        ImageView img4;
        try (InputStream inputStream = MainApplication.class.getResourceAsStream(path4)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found as resource: " + path4);
            }
            img4 = new ImageView(new Image(inputStream, 100, 100, true,
                    true));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load the application icon.", e);
        }

        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(img1, img2, img3, img4);
        hbox.getStyleClass().add("about-view-hbox");

        // GridPane container
        GridPane gridpane = new GridPane();
        gridpane.getStyleClass().add("gridpane");
        // GridPane Column settings
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(40);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        gridpane.getColumnConstraints().addAll(col1, col2);

        // GridPane horizontal and vertical Spacing - margins between columns
        gridpane.setVgap(5);
        gridpane.setHgap(5);

        Label label1 = new Label("Gemaakt door:");
        Label label2 = new Label("Remzi Cavdar");
        gridpane.add(label1, 0, 0);
        gridpane.add(label2, 1, 0);
        Label label3 = new Label("Klas:");
        Label label4 = new Label("ISR101");
        gridpane.add(label3, 0, 1);
        gridpane.add(label4, 1, 1);
        Label label5 = new Label("Studentnummer:");
        Label label6 = new Label("500714645");
        gridpane.add(label5, 0, 2);
        gridpane.add(label6, 1, 2);
        Label label7 = new Label("E-mail:");
        Hyperlink link = new Hyperlink("remzi.cavdar@hva.nl", "mailto:remzi.cavdar@hva.nl");
        gridpane.add(label7, 0, 3);
        gridpane.add(link, 1, 3);
        Label label8 = new Label("Repository:");
        Hyperlink link2 = new Hyperlink("github.com/Remzi1993/HvA-OOP2-practicumopdracht",
                "https://github.com/Remzi1993/HvA-OOP2-practicumopdracht");
        gridpane.add(label8, 0, 4);
        gridpane.add(link2, 1, 4);
        Label label9 = new Label("School:");
        Label label10 = new Label("Hogeschool van Amsterdam (HvA)");
        gridpane.add(label9, 0, 5);
        gridpane.add(label10, 1, 5);
        Label label11 = new Label("Opleiding:");
        Label label12 = new Label("HBO-ICT Software Engineering");
        gridpane.add(label11, 0, 6);
        gridpane.add(label12, 1, 6);
        Label label13 = new Label("Studieloopbaanbegeleider:");
        Label label14 = new Label("Ingrid Roks");
        gridpane.add(label13, 0, 7);
        gridpane.add(label14, 1, 7);

        HBox hbox2 = new HBox();
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().add(gridpane);
        HBox.setHgrow(gridpane, Priority.ALWAYS);
        hbox2.getStyleClass().add("about-view-hbox2");

        // Root container
        vboxContainer.getChildren().addAll(hbox, hbox2);
        // Scene
        Scene scene = new Scene(vboxContainer, 600, 400);
        // Apply stylesheet
        scene.getStylesheets().add(MainApplication.getAppCSS());
        return scene;
    }
}