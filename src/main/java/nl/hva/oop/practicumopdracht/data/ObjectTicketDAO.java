package nl.hva.oop.practicumopdracht.data;

import nl.hva.oop.practicumopdracht.models.Ticket;
import org.apache.commons.io.FileUtils;

import java.io.*;

import static nl.hva.oop.practicumopdracht.MainApplication.*;

/**
 * ObjectTicketDAO - ObjectDetailDAO
 * This class handles loading and saving Ticket objects to a file using serialization and deserialization.
 * Refactored to use Apache Commons IO for file handling.
 *
 * @author Remzi Cavdar
 */
public class ObjectTicketDAO extends TicketDAO {
    private static final String DIRECTORY_NAME = getAppDataDirectory();
    private static final String FILE_NAME = "Tickets.obj";
    private static final File DIRECTORY = FileUtils.getFile(DIRECTORY_NAME);
    private static final File FILE = FileUtils.getFile(DIRECTORY, FILE_NAME);

    @Override
    public boolean load() {
        if (DEBUG) {
            System.out.printf("%n******** Debug info%n* App data directory: %s%n* Full path to file: %s%n********%n%n",
                    DIRECTORY.getAbsolutePath(), FILE.getAbsolutePath());
        }

        // Ensure the directory exists and the file is created, using Apache Commons IO
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
                FileInputStream fileInputStream = FileUtils.openInputStream(FILE);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            if (objectInputStream.available() == 0) {
                if (DEBUG) {
                    System.out.println("File is empty");
                }
                return true;
            }

            // Clear the list before loading new data
            tickets.clear();

            int arraySize = objectInputStream.readInt();
            if (arraySize == 0) {
                return false;
            }

            for (int i = 0; i < arraySize; i++) {
                int belongsTo = objectInputStream.readInt();
                Ticket ticket = (Ticket) objectInputStream.readObject();
                ticket.setBelongsTo(getPersonDAO().getById(belongsTo));
                tickets.add(ticket);
            }

            if (DEBUG) {
                System.out.println("Loading complete: " + FILE_NAME);
            }
            return true;
        } catch (EOFException e) {
            if (DEBUG) {
                System.out.println("File is empty");
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found! - " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Something went wrong while reading the file: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found while deserializing the object.");
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
                FileOutputStream fileOutputStream = FileUtils.openOutputStream(FILE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeInt(tickets.size());

            for (Ticket ticket : tickets) {
                Ticket ticketObj = new Ticket(
                        ticket.getDestination(),
                        ticket.getStartDate(),
                        ticket.getEndDate(),
                        ticket.getCost(),
                        ticket.isCheckedIn(),
                        ticket.getDescription()
                );
                // Save belongsTo as person ID
                objectOutputStream.writeInt(getPersonDAO().getIdFor(ticket.getBelongsTo()));
                objectOutputStream.writeObject(ticketObj); // Serialize the Ticket object
            }

            if (DEBUG) {
                System.out.println("Saving data complete: " + FILE_NAME);
            }
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("File not found! - " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("An error occurred while saving the file: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}