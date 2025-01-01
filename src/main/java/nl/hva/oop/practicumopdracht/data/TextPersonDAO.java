package nl.hva.oop.practicumopdracht.data;

import nl.hva.oop.practicumopdracht.models.Person;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import static nl.hva.oop.practicumopdracht.Main.getAppDataDirectory;
import static nl.hva.oop.practicumopdracht.Main.DEBUG;
import static nl.hva.oop.practicumopdracht.MainApplication.getDateTimeFormatter;

/**
 * TextPersonDAO - TextMasterDAO
 * This is a DAO class which handles loading and saving data to a text file for the Person model.
 * Ensures UTF-8 encoding for reading and writing text files.
 *
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class TextPersonDAO extends PersonDAO {
    private static final String DIRECTORY_NAME = getAppDataDirectory();
    private static final String FILE_NAME = "Persons.txt";
    private static final File DIRECTORY = FileUtils.getFile(DIRECTORY_NAME);
    private static final File FILE = FileUtils.getFile(DIRECTORY, FILE_NAME);
    private static final String SEPARATOR = ";";

    @Override
    public boolean load() {
        if (DEBUG) {
            System.out.printf("%n******** Debug info%n* App data directory: %s%n* Full path to file: %s%n********%n%n",
                    DIRECTORY.getAbsolutePath(), FILE.getAbsolutePath());
        }

        // Ensure the directory and file exist
        try {
            FileUtils.forceMkdir(DIRECTORY); // Create the directory if it doesn't exist
            if (!FILE.exists()) {
                FileUtils.touch(FILE); // Create the file if it doesn't exist
                if (DEBUG) {
                    System.out.println("File created: " + FILE_NAME);
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating the directory or file: " + e.getMessage());
            return false;
        }

        if (DEBUG) {
            System.out.println("Loading data: " + FILE_NAME);
        }

        try (
                InputStream inputStream = FileUtils.openInputStream(FILE);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
        ) {
            // Clear the list before loading new data
            persons.clear();

            String line = bufferedReader.readLine();
            while (line != null) {
                String[] values = line.split(SEPARATOR);

                if (DEBUG) {
                    System.out.println(Arrays.toString(values));
                }

                try {
                    persons.add(new Person(
                            values[0], // Name
                            values[1], // Sex
                            LocalDate.parse(values[2], getDateTimeFormatter()), // Birthdate
                            values[3], // Birthplace
                            values[4], // Nationality
                            Integer.parseInt(values[5]), // SSN
                            values[6]  // Document number
                    ));
                } catch (DateTimeException e) {
                    System.err.println("Error parsing date: " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number: " + e.getMessage());
                }

                // Read the next line
                line = bufferedReader.readLine();
            }

            if (DEBUG) {
                System.out.println("Loading complete: " + FILE_NAME);
            }
            return true;

        } catch (FileNotFoundException e) {
            System.err.println("File not found! - " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Something went wrong while reading the file: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean save() {
        if (DEBUG) {
            System.out.println("\nSaving data: " + FILE_NAME);
        }

        try (
                OutputStream outputStream = FileUtils.openOutputStream(FILE);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
        ) {
            for (Person person : persons) {
                // Write Person data in the format: Name;Sex;Birthdate;Birthplace;Nationality;SSN;Document number
                bufferedWriter.append(String.format("%s%s%s%s%s%s%s%s%s%s%d%s%s",
                        person.getName(),
                        SEPARATOR,
                        person.getSex(),
                        SEPARATOR,
                        getDateTimeFormatter().format(person.getBirthdate()),
                        SEPARATOR,
                        person.getBirthplace(),
                        SEPARATOR,
                        person.getNationality(),
                        SEPARATOR,
                        person.getSSN(),
                        SEPARATOR,
                        person.getDocumentNumber()));
                bufferedWriter.newLine();
            }

            if (DEBUG) {
                System.out.println("Saving data complete: " + FILE_NAME);
            }
            return true;

        } catch (IOException e) {
            System.err.println("Something went wrong while saving the file: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}