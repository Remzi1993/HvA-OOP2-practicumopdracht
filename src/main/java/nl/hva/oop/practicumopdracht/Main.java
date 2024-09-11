package nl.hva.oop.practicumopdracht;

import javafx.application.Application;
import nl.hva.oop.practicumopdracht.utils.Preloader;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.time.LocalDate;

/**
 * Main class for starting up the JavaFX application with a call to launch MainApplication.
 *
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class Main {
    private static final String STUDENT_NAME = "Remzi Cavdar";
    private static final int STUDENT_NUMBER = 500714645;
    private static final boolean YES_I_ACCEPT = true;
    public static boolean launchedFromMain;
    private static final boolean PRELOADER = false;

    public static void main(String[] args) {
        if (!YES_I_ACCEPT) {
            showDeclarationOfIntegrity();
            return;
        }
        launchedFromMain = true;

        /*
         * Prevents the user of starting multiple instances of the application.
         * This is done by creating a temporary file in the app directory.
         * The temp file is excluded from git and is called App.lock.
         */
        final File FILE = FileUtils.getFile("App.lock");

        if (FILE.exists()) {
            System.err.println("Error: Another instance of the application is already running.");
            return;
        }

        try (
                FileOutputStream fileOutputStream = FileUtils.openOutputStream(FILE);
                FileChannel channel = fileOutputStream.getChannel();
                FileLock lock = channel.lock()
        ) {
        } catch (SecurityException e) {
            System.err.printf("""
                    SecurityException: Insufficient permissions to create or access the lock file.
                    Please check the file system permissions or run the application with elevated privileges.
                    Error details: %s%n
                    """, e.getMessage());
            e.printStackTrace();
        } catch (OverlappingFileLockException e) {
            System.err.printf("""
                    OverlappingFileLockException: The lock file is already locked by another process or thread in this JVM.
                    Ensure that only one instance of the application is running.
                    Error details: %s%n
                    """, e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            // Handles both FileNotFoundException and any other IO related errors
            System.err.printf("""
                    IOException: An I/O error occurred while trying to create or lock the file.
                    Possible causes: disk full, file system issues, or file access permissions.
                    Error details: %s%n
                    """, e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        /*
         * Register a shutdown hook to delete the lock file when the application is closed. Even when forcefully closed
         * with the task manager. (Tested on Windows 11 with JavaFX 19)
         */
        FILE.deleteOnExit();
        /*
         * If the boolean PRELOADER is set to true a preloader will be shown. If there is not a lot of data to be loaded
         * then the preloader would be too fast to see.
         */
        if (PRELOADER) {
            System.setProperty("javafx.preloader", Preloader.class.getName());
        }
        Application.launch(MainApplication.class, args);
    }

    private static void showDeclarationOfIntegrity() {
        System.out.println("Integriteitsverklaring\n---");
        System.out.printf("Datum: %s%n", LocalDate.now());
        System.out.printf("Naam: %s%n", STUDENT_NAME);
        System.out.printf("Studentnummer: %s%n", STUDENT_NUMBER);
        System.out.println("---");

        String integriteitsVerklaring =
                "Ik verklaar naar eer en geweten dat ik deze practicumopdracht zelf zal maken en geen plagiaat zal plegen " +
                        "door code van anderen over te nemen.\n\n" +

                        "Ik ben me ervan bewust dat:\n" +
                        "\t- Er (geautomatiseerd) op fraude wordt gescanned\n" +
                        "\t- Verdachte situaties worden gemeld aan de examencommissie\n" +
                        "\t- Fraude kan leiden tot het ongeldig verklaren van deze practicumopdracht voor alle studenten\n\n" +

                        "Door 'YES_I_ACCEPT' in de Main-class op 'true' te zetten, onderteken ik deze verklaring.";

        System.out.println(integriteitsVerklaring);
    }

    // Getters
    public static String getStudentName() {
        return STUDENT_NAME;
    }

    public static int getStudentNumber() {
        return STUDENT_NUMBER;
    }
}