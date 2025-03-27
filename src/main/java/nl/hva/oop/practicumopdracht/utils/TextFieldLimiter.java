package nl.hva.oop.practicumopdracht.utils;

import javafx.scene.control.TextField;

/**
 * Class for limiting the number of characters in a text field.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class TextFieldLimiter {
    public TextFieldLimiter(final TextField TEXT_FIELD, final int MAX_LENGTH) {
        TEXT_FIELD.textProperty().addListener((_, _, _) -> {
            if (TEXT_FIELD.getText().length() > MAX_LENGTH) {
                String string = TEXT_FIELD.getText().substring(0, MAX_LENGTH);
                TEXT_FIELD.setText(string);
            }
        });
    }
}