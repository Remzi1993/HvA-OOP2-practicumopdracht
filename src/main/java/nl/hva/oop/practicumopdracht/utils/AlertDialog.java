package nl.hva.oop.practicumopdracht.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import nl.hva.oop.practicumopdracht.MainApplication;
import java.io.InputStream;
import java.util.Objects;

/**
 * JavaFX Custom alert dialog
 * I made this custom alert dialog class because I didn't want to repeat my alert code all over the place with the
 * added chance to make mistakes. Also, to keep my code short and clean.
 *
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class AlertDialog {
    private String type, title, headerText, contentText;
    private Alert alert = new Alert(Alert.AlertType.NONE);
    private DialogPane dialog;
    private Image alertDialogIcon;
    private String css;

    /**
     * Default AlertDialog constructor when nothing is provided
     */
    public AlertDialog(Image alertDialogIcon, String css) {
        this.headerText = null;
        this.alertDialogIcon = alertDialogIcon;
        this.css = css;
    }

    /**
     * AlertDialog constructor only providing type
     *
     * @param type setAlertType for new Alert
     */
    public AlertDialog(String type, Image alertDialogIcon, String css) {
        this.type = type;
        this.headerText = null;
        this.alertDialogIcon = alertDialogIcon;
        this.css = css;
        initialize();
    }

    /**
     * AlertDialog constructor without providing type and headerText
     *
     * @param title       setTitle for new Alert
     * @param contentText setContentText for new Alert
     */
    public AlertDialog(String title, String contentText, Image alertDialogIcon, String css) {
        // Providing default type - If you want to create an empty alert, use the class without params/args
        this.type = "INFORMATION";
        this.title = title;
        this.contentText = contentText;
        this.alertDialogIcon = alertDialogIcon;
        this.css = css;
        this.headerText = null;
        initialize();
    }

    /**
     * AlertDialog constructor without providing headerText
     *
     * @param type        setAlertType for new Alert
     * @param title       setTitle for new Alert
     * @param contentText setContentText for new Alert
     */
    public AlertDialog(String type, String title, String contentText, Image alertDialogIcon, String css) {
        this.type = type;
        this.title = title;
        this.contentText = contentText;
        this.alertDialogIcon = alertDialogIcon;
        this.css = css;
        this.headerText = null;
        initialize();
    }

    /**
     * AlertDialog constructor
     *
     * @param type        setAlertType for new Alert
     * @param title       setTitle for new Alert
     * @param headerText  setHeaderText for new Alert
     * @param contentText setContentText for new Alert
     */
    public AlertDialog(String type, String title, String headerText, String contentText, Image alertDialogIcon,
                       String css) {
        this.type = type;
        this.title = title;
        this.headerText = headerText;
        this.contentText = contentText;
        this.alertDialogIcon = alertDialogIcon;
        this.css = css;
        initialize();
    }

    private void initialize() {
        switch (type) {
            case "CONFIRMATION":
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                String path1 = "images/emoji/thinking-face.gif";
                try (InputStream inputStream = MainApplication.class.getResourceAsStream(path1)) {
                    if (inputStream == null) {
                        throw new IllegalArgumentException("File not found as resource: " + path1);
                    }
                    alert.setGraphic(new ImageView(new Image(inputStream, 100,
                            100, true, true)));
                } catch (Exception e) {
                    throw new RuntimeException("Failed to load the application icon.", e);
                }
                break;
            case "WARNING":
                alert.setAlertType(Alert.AlertType.WARNING);
                String path2 = "images/emoji/weary.gif";
                try (InputStream inputStream = MainApplication.class.getResourceAsStream(path2)) {
                    if (inputStream == null) {
                        throw new IllegalArgumentException("File not found as resource: " + path2);
                    }
                    alert.setGraphic(new ImageView(new Image(inputStream, 100,
                            100, true, true)));
                } catch (Exception e) {
                    throw new RuntimeException("Failed to load the application icon.", e);
                }
                break;
            case "ERROR":
                alert.setAlertType(Alert.AlertType.ERROR);
                String path3 = "images/emoji/dizzy-face.gif";
                try (InputStream inputStream = MainApplication.class.getResourceAsStream(path3)) {
                    if (inputStream == null) {
                        throw new IllegalArgumentException("File not found as resource: " + path3);
                    }
                    alert.setGraphic(new ImageView(new Image(inputStream, 100,
                            100, true, true)));
                } catch (Exception e) {
                    throw new RuntimeException("Failed to load the application icon.", e);
                }
                break;
            case "NONE":
                break;
            default:
                alert.setAlertType(Alert.AlertType.INFORMATION);
                String path4 = "images/emoji/slightly-happy.gif";
                try (InputStream inputStream = MainApplication.class.getResourceAsStream(path4)) {
                    if (inputStream == null) {
                        throw new IllegalArgumentException("File not found as resource: " + path4);
                    }
                    alert.setGraphic(new ImageView(new Image(inputStream, 100,
                            100, true, true)));
                } catch (Exception e) {
                    throw new RuntimeException("Failed to load the application icon.", e);
                }
        }
    }

    public void setGraphic(final ImageView IMAGE_VIEW) {
        alert.setGraphic(IMAGE_VIEW);
    }

    private void initializeText() {
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
    }

    public void show() {
        try {
            initializeText();
        } catch (Exception e) {
            System.err.println("Alert dialog text couldn't be initialized. Please check if you have at least provided " +
                    "a title and contentText");
        }

        try {
            /* Add button to AlertType NONE otherwise alert window can't be closed
             * See link for explanation: https://stackoverflow.com/questions/32402131/alert-type-none-closing-javafx
             */
            if (Objects.equals(type, "NONE")) {
                alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            }

            // Set the CSS file and add a CSS class to the dialog
            dialog = alert.getDialogPane();
            dialog.getStylesheets().add(css);

            // To set the icon of the dialog window
            Stage stage = (Stage) dialog.getScene().getWindow();
            stage.getIcons().add(alertDialogIcon);

            // Show the dialog
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Alert dialog couldn't be created. Something went wrong");
        }
    }

    // Help my fellow programmers so that they can use both :)
    public void showAndWait() {
        show();
    }

    public ButtonType getResult() {
        return alert.getResult();
    }

    @Override
    public String toString() {
        if (type == null || title == null || contentText == null) {
            return "You need to at least provide type, title and contentText";
        }
        if (headerText == null) {
            return String.format("""
                    Alert type: %s
                    Title: %s
                    Content text: %s
                    """, type, title, contentText);
        }
        return String.format("""
                Alert type: %s
                Title: %s
                Header text: %s
                Content text: %s
                """, type, title, headerText, contentText);
    }

    // Getter and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }
}