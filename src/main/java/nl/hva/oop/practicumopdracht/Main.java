package nl.hva.oop.practicumopdracht;

import javafx.application.Application;
import nl.hva.oop.practicumopdracht.utils.Preloader;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Properties;
import static nl.hva.oop.practicumopdracht.MainApplication.DEBUG;

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
    private static final File CONFIG_FILE = new File("data/config.properties");
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        if (!YES_I_ACCEPT) {
            showDeclarationOfIntegrity();
            return;
        }
        launchedFromMain = true;

        int port = loadPortFromConfig();

        if (!attemptSingleInstanceLock(port)) {
            return; // Another instance is already running
        }

        /*
         * If the boolean PRELOADER is set to true, a preloader will be shown.
         * If there is not a lot of data to be loaded, then the preloader would be too fast to see.
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

        String Integriteitsverklaring = """
                Ik verklaar naar eer en geweten dat ik deze practicumopdracht zelf zal maken en geen plagiaat zal
                plegen door code van anderen over te nemen. Ik ben me ervan bewust dat:
                 - Er (geautomatiseerd) op fraude wordt gescand
                 - Verdachte situaties worden gemeld aan de examencommissie
                 - Fraude kan leiden tot het ongeldig verklaren van deze practicumopdracht voor alle studenten
                 Door 'YES_I_ACCEPT' in de Main-class op 'true' te zetten, onderteken ik deze verklaring.""";

        System.out.println(Integriteitsverklaring);
    }

    private static boolean attemptSingleInstanceLock(int port) {
        try {
            // Keep the server socket open for the entire duration of the application
            serverSocket = new ServerSocket(port);
            if (DEBUG) {
                System.out.printf("Application started on port %d.%n", port);
            }

            // Add a shutdown hook to release the port when the application exits
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (serverSocket != null && !serverSocket.isClosed()) {
                        serverSocket.close();
                        if (DEBUG) {
                            System.out.printf("Releasing port %d on exit.%n", port);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error releasing the server socket on application exit: " + e.getMessage());
                }
            }));
            return true;
        } catch (IOException e) {
            System.err.printf("Error: Another instance of the application is already running on port %d.%n", port);
            return false;
        }
    }

    private static int loadPortFromConfig() {
        Properties properties = new Properties();
        // Default port if the config file is missing or incorrect
        int defaultPort = 51152;

        try (
                InputStream inputStream = FileUtils.openInputStream(CONFIG_FILE);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
        ) {
            properties.load(bufferedReader);
            return Integer.parseInt(properties.getProperty("port", String.valueOf(defaultPort)));
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load port from config file, using default port: " + defaultPort);
            return defaultPort;
        }
    }

    // Getters
    public static String getStudentName() {
        return STUDENT_NAME;
    }

    public static int getStudentNumber() {
        return STUDENT_NUMBER;
    }
}