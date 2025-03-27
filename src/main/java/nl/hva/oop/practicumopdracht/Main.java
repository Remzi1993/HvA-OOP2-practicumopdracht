package nl.hva.oop.practicumopdracht;

import javafx.application.Application;
import nl.hva.oop.practicumopdracht.utils.Preloader;
import java.io.*;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main class for starting up the JavaFX application with a call to launch MainApplication.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class Main {
    private static final String STUDENT_NAME = "Remzi Cavdar";
    private static final int STUDENT_NUMBER = 500714645;
    private static final boolean YES_I_ACCEPT = true;
    public static boolean launchedFromMain;
    private static final boolean PRELOADER = false;
    private static String APP_DATA_DIRECTORY;
    private static ServerSocket serverSocket;
    private static final int DEFAULT_PORT_MIN = 1024;
    private static final int DEFAULT_PORT_MAX = 65535;
    public static final String APP_VERSION = "2.0.6";
    /**
     * This is a global setting for the entire application for getting error and/or success messages in the console.
     * Set in jpackage --java-options "-DDEBUG=true/false"
     */
    public static final boolean DEBUG = Boolean.getBoolean("DEBUG") || true;

    public static void main(String[] args) {
        if (!YES_I_ACCEPT) {
            showDeclarationOfIntegrity();
            return;
        }
        launchedFromMain = true;

        if (DEBUG) {
            System.out.println("App config:");
        }

        String os = System.getProperty("os.name").toUpperCase();
        String appFolder = "Remzi Cavdar" + File.separator + "HvA OOP2 practicumopdracht";

        if (os.contains("WIN")) {
            // Correctly get the APPDATA folder and append the app folder
            APP_DATA_DIRECTORY = System.getenv("APPDATA") + File.separator + appFolder;
        } else {
            // For non-Windows OS, get the user home and append the app folder
            APP_DATA_DIRECTORY = System.getProperty("user.home") + File.separator + appFolder;
        }

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

    private static int generateRandomPort() {
        return ThreadLocalRandom.current().nextInt(DEFAULT_PORT_MIN, DEFAULT_PORT_MAX + 1);
    }

    private static boolean isPortAvailable(int port) {
        try (ServerSocket _ = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static int getAvailablePort() {
        int port;
        do {
            port = generateRandomPort();
        } while (!isPortAvailable(port)); // Repeat until an available port is found
        return port;
    }

    private static int loadPortFromConfig() {
        Properties properties = new Properties();
        File configFile = new File(APP_DATA_DIRECTORY, "config.properties");

        // Check if the config file exists
        if (configFile.exists()) {
            try (InputStream inputStream = new FileInputStream(configFile);
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                properties.load(bufferedReader);
                String portString = properties.getProperty("port");

                if (portString != null) {
                    int port = Integer.parseInt(portString);
                    if (port >= DEFAULT_PORT_MIN && port <= DEFAULT_PORT_MAX) {
                        return port; // Use the port from config without checking availability
                    }
                }
            } catch (IOException | NumberFormatException e) {
                if (DEBUG) {
                    System.err.println("Invalid config file or port, generating a new one.");
                }
            }
        }

        // If no valid port is found in the config file, generate a new one
        int newPort = getAvailablePort();
        savePortToConfig(newPort);
        return newPort;
    }

    private static void savePortToConfig(int port) {
        Properties properties = new Properties();
        properties.setProperty("port", String.valueOf(port));

        File configFile = new File(APP_DATA_DIRECTORY, "config.properties");
        File configDir = configFile.getParentFile(); // Get the directory

        // Ensure the directory exists
        if (!configDir.exists()) {
            if (configDir.mkdirs()) {
                if (DEBUG) {
                    System.out.println("Created config directory: " + configDir.getAbsolutePath());
                }
            } else {
                if (DEBUG) {
                    System.err.println("Failed to create config directory: " + configDir.getAbsolutePath());
                }
                return;
            }
        }

        // Save the port to the config file
        try (OutputStream outputStream = new FileOutputStream(configFile)) {
            properties.store(outputStream, "App config");
            if (DEBUG) {
                System.out.println("Port saved to config file: " + port);
            }
        } catch (IOException e) {
            System.err.println("Failed to save port to config file: " + e.getMessage());
        }
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

    // Getters
    public static String getStudentName() {
        return STUDENT_NAME;
    }

    public static int getStudentNumber() {
        return STUDENT_NUMBER;
    }

    public static String getAppDataDirectory() {
        return APP_DATA_DIRECTORY;
    }
}