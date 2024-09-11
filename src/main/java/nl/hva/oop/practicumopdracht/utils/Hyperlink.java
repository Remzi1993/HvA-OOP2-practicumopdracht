package nl.hva.oop.practicumopdracht.utils;

import javafx.scene.Node;

import java.awt.*;
import java.net.URI;

/**
 * A custom Hyperlink class with the ability to open a hyperlink in the default browser.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class Hyperlink extends javafx.scene.control.Hyperlink {
    public Hyperlink() {
        super();
    }

    public Hyperlink(String text, String url) {
        super(text);
        setUrl(url);
    }

    public Hyperlink(String text, Node graphic, String url) {
        super(text, graphic);
        setUrl(url);
    }

    public void setUrl(String url) {
        // Make the link clickable and open the URL in the default browser.
        this.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}