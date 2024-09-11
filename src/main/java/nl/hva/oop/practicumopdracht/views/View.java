package nl.hva.oop.practicumopdracht.views;

import javafx.scene.Parent;

/**
 * View - Abstract class for all views (with a controller)
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public abstract class View {
    private Parent root;

    public View() {
        root = initializeView();
    }

    protected abstract Parent initializeView();

    public Parent getRoot() {
        return root;
    }
}