package nl.hva.oop.practicumopdracht.utils;

import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.util.Objects;
import static nl.hva.oop.practicumopdracht.MainApplication.getDateFormat;

/**
 * Class InputHandler
 * Handles/checks all kinds of inputs (JavaFX UI Controls) and clears all inputs. There are also methods to check inputs
 * for double and int
 *
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class InputHandler {
    private static final Border BORDER = Border.stroke(Color.RED);
    private static final IsNumeric IS_NUMERIC = new IsNumeric();
    private int totalErrorValues;

    /**
     * Method to check for any JavaFX input control if it is empty or not
     *
     * @param inputs Checks if the inputs are empty and set a red border around the empty inputs
     */
    public void checkValues(Object[] inputs) {
        if (inputs == null || inputs.length == 0) {
            return; // If inputs is null or empty return
        }
        for (Object input : inputs) {
            if (input instanceof TextField) {
                if (((TextField) input).getText().isBlank()) {
                    ((TextField) input).setBorder(BORDER);
                    ++totalErrorValues;
                }
            }
            if (input instanceof ComboBox) {
                ((ComboBox<?>) input).setBorder(null);
                if (((ComboBox<?>) input).getSelectionModel().isEmpty()) {
                    ((ComboBox<?>) input).setBorder(BORDER);
                    ++totalErrorValues;
                }
            }
            if (input instanceof DatePicker) {
                ((DatePicker) input).setBorder(null);
                if (Objects.equals(((DatePicker) input).getEditor().getText(), getDateFormat().toUpperCase()) ||
                        ((DatePicker) input).getValue() == null && ((DatePicker) input).isEditable()) {
                    ((DatePicker) input).setBorder(BORDER);
                    ++totalErrorValues;
                }
            }
        }
    }

    /**
     * @param inputs           Checks if the inputs are empty and set a red border around the empty inputs
     * @param DatePickerMinAge Checks if a datepicker input is older than the minimum age
     */
    public void checkValues(Object[] inputs, int DatePickerMinAge) {
        if (inputs == null || inputs.length == 0) {
            return; // If inputs is null or empty return
        }
        for (Object input : inputs) {
            if (input instanceof TextField) {
                if (((TextField) input).getText().isBlank()) {
                    ((TextField) input).setBorder(BORDER);
                    ++totalErrorValues;
                }
            }
            if (input instanceof ComboBox) {
                ((ComboBox<?>) input).setBorder(null);
                if (((ComboBox<?>) input).getSelectionModel().isEmpty()) {
                    ((ComboBox<?>) input).setBorder(BORDER);
                    ++totalErrorValues;
                }
            }
            if (input instanceof DatePicker) {
                ((DatePicker) input).setBorder(null);
                if (Objects.equals(((DatePicker) input).getEditor().getText(), getDateFormat().toUpperCase()) ||
                        ((DatePicker) input).getValue().isAfter(LocalDate.now().minusYears(DatePickerMinAge)) &&
                                ((DatePicker) input).isEditable()) {
                    ((DatePicker) input).setBorder(BORDER);
                    ++totalErrorValues;
                }
            }
        }
    }

    /**
     * @param inputs Checks if the inputs are an int and if not gives the inputs a red border
     */
    public void checkValuesIsInt(Object[] inputs) {
        // Check if input is an int and if not give the inputs red border
        for (Object input : inputs) {
            if (input instanceof TextField) {
                if (!IS_NUMERIC.isInt(((TextField) input).getText())) {
                    ((TextField) input).setBorder(BORDER);
                    ++totalErrorValues;
                }
            }
        }
    }

    /**
     * @param input Checks if a single input is an int and if not gives the input a red border
     */
    public void checkIsInt(Object input) {
        if (input instanceof TextField) {
            if (!IS_NUMERIC.isInt(((TextField) input).getText())) {
                ((TextField) input).setBorder(BORDER);
                ++totalErrorValues;
            }
        }
    }

    /**
     * @param inputs Checks if the inputs are a double and if not gives the inputs a red border
     */
    public void checkValuesIsDouble(Object[] inputs) {
        for (Object input : inputs) {
            if (input instanceof TextField) {
                if (!IS_NUMERIC.isDouble(((TextField) input).getText())) {
                    ((TextField) input).setBorder(BORDER);
                    ++totalErrorValues;
                }
            }
        }
    }

    /**
     * @param input Checks if a single input is a double and if not gives the input a red border
     */
    public void checkIsDouble(Object input) {
        if (input instanceof TextField) {
            if (!IS_NUMERIC.isDouble(((TextField) input).getText())) {
                ((TextField) input).setBorder(BORDER);
                ++totalErrorValues;
            }
        }
    }

    /**
     * @param inputs        Clears all inputs
     * @param clearComboBox Clears the ComboBox if set to true
     */
    public void clearValues(Object[] inputs, boolean clearComboBox, boolean DatePickerDefaultValue) {
        if (inputs == null || inputs.length == 0) {
            return; // If inputs is null or empty return
        }
        for (Object input : inputs) {
            if (input instanceof TextField) {
                ((TextField) input).clear();
            }
            if (input instanceof TextArea) {
                ((TextArea) input).clear();
            }
            if (input instanceof CheckBox) {
                ((CheckBox) input).setSelected(false);
            }
            if (input instanceof ListView<?>) {
                ((ListView<?>) input).getSelectionModel().clearSelection();
            }
            if (input instanceof ComboBox && clearComboBox) {
                ((ComboBox<?>) input).getSelectionModel().clearSelection();
            }
            if (input instanceof DatePicker && DatePickerDefaultValue) {
                ((DatePicker) input).setValue(LocalDate.now().minusYears(18));
                ((DatePicker) input).getEditor().setText(getDateFormat().toUpperCase());
            } else if (input instanceof DatePicker) {
                ((DatePicker) input).setValue(null);
                ((DatePicker) input).getEditor().clear();
            }
        }
    }

    /**
     * @param inputs Clears the red border from all inputs
     */
    public void clearWarnings(Object[] inputs) {
        if (inputs == null || inputs.length == 0) {
            return; // If inputs is null or empty return
        }
        for (Object input : inputs) {
            if (input instanceof TextField) {
                ((TextField) input).setBorder(null);
            }
            if (input instanceof ComboBox) {
                ((ComboBox<?>) input).setBorder(null);
            }
            if (input instanceof DatePicker) {
                ((DatePicker) input).setBorder(null);
            }
        }
    }

    // All getters and setters
    public int getTotalErrorValues() {
        return totalErrorValues;
    }

    public void setTotalErrorValues(int total) {
        this.totalErrorValues = total;
    }
}