package nl.hva.oop.practicumopdracht.data;

import nl.hva.oop.practicumopdracht.models.Person;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.time.LocalDate;

import static nl.hva.oop.practicumopdracht.MainApplication.*;

/**
 * BinaryPersonDAO - BinaryMasterDAO
 * This is a DAO class which handles loading and saving data to a binary file for the Person model.
 * Refactored to use Apache Commons IO for file handling.
 *
 * @author Remzi Cavdar
 */
public class BinaryPersonDAO extends PersonDAO {
    private static final String DIRECTORY_NAME = getAppDataDirectory();
    private static final String FILE_NAME = "Persons.dat";
    private static final File DIRECTORY = FileUtils.getFile(DIRECTORY_NAME);
    private static final File FILE = FileUtils.getFile(DIRECTORY, FILE_NAME);

    @Override
    public boolean load() {
        if (DEBUG) {
            System.out.printf("%n******** Debug info%n* App data directory: %s%n* Full path to file: %s%n********%n%n",
                    DIRECTORY.getAbsolutePath(), FILE.getAbsolutePath());
        }

        // Ensure the directory and file exist, using Apache Commons IO
        try {
            FileUtils.forceMkdir(DIRECTORY); // Ensures the directory exists, creates it if necessary
            if (!FILE.exists()) {
                FileUtils.touch(FILE); // Creates the file if it doesn't exist
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
                FileInputStream fileInputStream = FileUtils.openInputStream(FILE);
                DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        ) {
            if (dataInputStream.available() == 0) {
                if (DEBUG) {
                    System.out.println("File is empty");
                }
                return true;
            }

            // Clear the list before loading new data
            persons.clear();

            int arraySize = dataInputStream.readInt();
            for (int i = 0; i < arraySize; i++) {
                persons.add(new Person(
                        dataInputStream.readUTF(),
                        dataInputStream.readUTF(),
                        LocalDate.parse(dataInputStream.readUTF(), getDateTimeFormatter()),
                        dataInputStream.readUTF(),
                        dataInputStream.readUTF(),
                        dataInputStream.readInt(),
                        dataInputStream.readUTF()
                ));
            }

            // Successful load
            if (DEBUG) {
                System.out.println("Loading complete: " + FILE_NAME);
            }
            return true;

        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean save() {
        if (DEBUG) {
            System.out.println("\nSaving data: " + FILE_NAME);
        }

        try (
                FileOutputStream fileOutputStream = FileUtils.openOutputStream(FILE);
                DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
        ) {
            dataOutputStream.writeInt(persons.size());

            for (Person person : persons) {
                dataOutputStream.writeUTF(person.getName());
                dataOutputStream.writeUTF(person.getSex());
                dataOutputStream.writeUTF(person.getBirthdate().format(getDateTimeFormatter()));
                dataOutputStream.writeUTF(person.getBirthplace());
                dataOutputStream.writeUTF(person.getNationality());
                dataOutputStream.writeInt(person.getSSN());
                dataOutputStream.writeUTF(person.getDocumentNumber());
            }

            // Successful save
            if (DEBUG) {
                System.out.println("Saving data complete: " + FILE_NAME);
            }
            return true;

        } catch (IOException e) {
            System.err.println("An error occurred while saving the file: " + e.getMessage());
            return false;
        }
    }
}