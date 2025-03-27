package nl.hva.oop.practicumopdracht.utils;

import javafx.scene.control.TextField;
import java.text.DecimalFormatSymbols;
import java.util.regex.Pattern;

/**
 * NumericTextField extends TextField
 * This class is a custom TextField which only allows numeric input.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class NumericTextField extends TextField {
    private static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols();
    private static final char DECIMAL_SEPARATOR = ',';
    private static final char GROUPING_SEPARATOR = '.';
    // For positive decimals: ^\\d*([,.]\\d{0,2})?$
    // For positive and negative decimals: ^-?\\d*([,.]\\d{0,2})?$
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("^\\d*([,.]\\d{0,2})?$");
    // For positive integers: ^$|^[1-9]\\d*$
    // For positive and negative integers ^$|^[-1-9]\\d*$
    private static final Pattern INTEGER_PATTERN = Pattern.compile("^$|^[1-9]\\d*$");

    /**
     * Constructor for NumericTextField for both integer and decimal input.
     * @param IS_DECIMAL True if decimal input is allowed, false if only integer input is allowed.
     * @param MAX_LENGTH The maximum length of the number.
     */
    public NumericTextField(final boolean IS_DECIMAL, final int MAX_LENGTH) {
        super();
        DECIMAL_FORMAT_SYMBOLS.setDecimalSeparator(DECIMAL_SEPARATOR);
        DECIMAL_FORMAT_SYMBOLS.setGroupingSeparator(GROUPING_SEPARATOR);

        if (IS_DECIMAL) {
            this.textProperty().addListener((_, oldValue, newValue) -> {
                if (!DECIMAL_PATTERN.matcher(newValue).matches()) {
                    this.setText(oldValue);
                }

                if (newValue.length() > MAX_LENGTH) {
                    String string = newValue.substring(0, MAX_LENGTH);
                    this.setText(string);
                }
            });
        } else {
            this.textProperty().addListener((_, oldValue, newValue) -> {
                if (!INTEGER_PATTERN.matcher(newValue).matches()) {
                    this.setText(oldValue);
                }

                if (newValue.length() > MAX_LENGTH) {
                    String string = newValue.substring(0, MAX_LENGTH);
                    this.setText(string);
                }
            });
        }
    }

    /**
     * @param IS_DECIMAL Default value is 10.
     */
    public NumericTextField(final boolean IS_DECIMAL) {
        this(IS_DECIMAL, 10);
    }

    @Override
    public void insertText(int index, String text) {
        super.insertText(index, replace(text));
    }

    @Override
    public void replaceText(int start, int end, String text) {
        super.replaceText(start, end, replace(text));
    }

    private String replace(String text) {
        return text.replace(GROUPING_SEPARATOR, DECIMAL_SEPARATOR);
    }

    // The following methods don't work if you're upcasting to TextField - use NumericTextField class directly.
    public void setNumber(int value) {
        this.setText(String.valueOf(value));
    }

    public void setNumber(double value) {
        this.setText(String.valueOf(value));
    }

    public void setText(int value) {
        setNumber(value);
    }

    public void setText(double value) {
        setNumber(value);
    }
}