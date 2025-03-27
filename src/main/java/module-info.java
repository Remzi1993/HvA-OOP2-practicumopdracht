module nl.hva.oop.practicumopdracht {
    requires transitive javafx.controls;
    requires org.apache.commons.io;
    requires java.desktop;
    requires transitive javafx.graphics;
    exports nl.hva.oop.practicumopdracht;
    exports nl.hva.oop.practicumopdracht.controllers;
    exports nl.hva.oop.practicumopdracht.views;
    exports nl.hva.oop.practicumopdracht.models;
    exports nl.hva.oop.practicumopdracht.data;
    exports nl.hva.oop.practicumopdracht.utils to javafx.graphics;
}