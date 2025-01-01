package nl.hva.oop.practicumopdracht.utils;

import nl.hva.oop.practicumopdracht.Main;

/**
 * Utility class for checking if a string is numeric.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class IsNumeric {
    // Debug setting for getting error messages in the console
    private static final boolean DEBUG = Main.DEBUG;

    /**
     * @param string will be checked if it's an int.
     * @return true or false. Return true if a String is an int
     */
    public boolean isInt(String string) {
        if (string.isBlank()) {
            if (DEBUG) {
                System.err.println("String is empty.");
            }
            return false;
        }

        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            if (DEBUG) {
                System.err.println("String is not an integer!");
            }
            return false;
        }

        return true;
    }

    /**
     * @param string will be checked if it's a double.
     * @return true or false. Return true if a String is a double
     */
    public boolean isDouble(String string) {
        if (string.isBlank()) {
            if (DEBUG) {
                System.err.println("String is empty.");
            }
            return false;
        }

        try {
            Double.parseDouble(string);
        } catch (NumberFormatException nfe) {
            if (DEBUG) {
                System.err.println("String is not a double!");
            }
            return false;
        }

        return true;
    }
}