/**
 * Module definition for the Java app.
 * This module requires JavaFX controls and graphics, Apache Commons IO,
 * and Java Desktop for GUI components.
 * It exports packages for controllers, views, models, data, and utilities.
 */
module nl.hva.oop.practicumopdracht {
    requires transitive javafx.controls;
    requires org.apache.commons.io;
    requires java.desktop;
    requires transitive javafx.graphics;
    requires info.remzi.javafx.hyperlink;
    exports nl.hva.oop.practicumopdracht;
    exports nl.hva.oop.practicumopdracht.controllers;
    exports nl.hva.oop.practicumopdracht.views;
    exports nl.hva.oop.practicumopdracht.models;
    exports nl.hva.oop.practicumopdracht.data;
    exports nl.hva.oop.practicumopdracht.utils to javafx.graphics;
}