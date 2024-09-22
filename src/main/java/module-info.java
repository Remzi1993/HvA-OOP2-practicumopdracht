module nl.hva.oop.practicumopdracht {
    requires javafx.controls;
    requires org.apache.commons.io;
    requires java.desktop;
    // Export the main package
    exports nl.hva.oop.practicumopdracht;
    // Export the package containing the Preloader to javafx.graphics
    exports nl.hva.oop.practicumopdracht.utils to javafx.graphics;
}