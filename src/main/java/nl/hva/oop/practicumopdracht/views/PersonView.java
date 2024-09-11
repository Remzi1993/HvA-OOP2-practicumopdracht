package nl.hva.oop.practicumopdracht.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import nl.hva.oop.practicumopdracht.MainApplication;
import nl.hva.oop.practicumopdracht.models.Person;
import nl.hva.oop.practicumopdracht.utils.NumericTextField;

/**
 * PersonView - MasterView
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class PersonView extends View {
    private TextField txtFieldName, txtFieldBirthplace, txtFieldNationality, txtFieldSSN, txtFieldDocumentNumber;
    private DatePicker datePickerBirthdate;
    private ComboBox<String> comboBoxSex;
    private Button saveButton, newButton, deleteButton, switchButton;
    private ListView<Person> listView;
    private MenuItem menuItemSave, menuItemLoad, menuItemClose, menuItemSortAZ, menuItemSortZA, menuItemAbout;
    private static final int MAX_LENGTH_SSN = 9;

    @Override
    protected Parent initializeView() {
        // Root
        BorderPane rootBorderPane = new BorderPane();
        rootBorderPane.getStyleClass().add("root-container");
        // Create the menu bar.
        MenuBar menuBar = new MenuBar();
        menuBar.getStyleClass().add("menu-bar");
        // Create the file menu.
        Menu fileMenu = new Menu("Bestand");
        menuItemSave = new MenuItem("Opslaan");
        menuItemLoad = new MenuItem("Laden");
        menuItemClose = new MenuItem("Afsluiten");
        fileMenu.getItems().addAll(menuItemSave, menuItemLoad, new SeparatorMenuItem(), menuItemClose);
        // Create the sort menu.
        Menu sortMenu = new Menu("Sorteren");
        menuItemSortAZ = new MenuItem("Naam (A-Z)");
        menuItemSortZA = new MenuItem("Naam (Z-A)");
        sortMenu.getItems().addAll(menuItemSortAZ, menuItemSortZA);
        // Create the info and about menu.
        Menu aboutMenu = new Menu("Support & info");
        menuItemAbout = new MenuItem("Info & contactgegevens");
        aboutMenu.getItems().add(menuItemAbout);
        // Add the menus to the menu bar.
        menuBar.getMenus().addAll(fileMenu, sortMenu, aboutMenu);

        // VBox parent container
        VBox vboxContainer = new VBox();
        vboxContainer.getStyleClass().add("vbox-container");
        // Padding
        vboxContainer.setPadding(new Insets(10));
        vboxContainer.setSpacing(10);

        // GridPane container
        GridPane gridpane = new GridPane();
        gridpane.getStyleClass().add("bg-1");
        // GridPane Column settings
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(75);
        gridpane.getColumnConstraints().addAll(col1, col2);

        // GridPane horizontal and vertical Spacing - margins between columns
        gridpane.setVgap(10);
        gridpane.setHgap(10);
        gridpane.setAlignment(Pos.CENTER);

        // Labels and inputs and controls
        // Name
        Label labelName = new Label("Voor- en achternaam:");
        txtFieldName = new TextField();
        gridpane.add(labelName, 0, 0);
        gridpane.add(txtFieldName, 1, 0);

        // Sex
        Label labelSex = new Label("Geslacht:");
        comboBoxSex = new ComboBox<>();
        comboBoxSex.getItems().add("Man");
        comboBoxSex.getItems().add("Vrouw");
        gridpane.add(labelSex, 0, 1);
        gridpane.add(comboBoxSex, 1, 1);

        // Birthdate
        Label labelBirthdate = new Label("Geboortedatum: (minimaal 18 jaar)");
        datePickerBirthdate = new DatePicker();
        gridpane.add(labelBirthdate, 0, 2);
        gridpane.add(datePickerBirthdate, 1, 2);

        // Birthplace
        Label labelBirthplace = new Label("Geboorteplaats:");
        txtFieldBirthplace = new TextField();
        gridpane.add(labelBirthplace, 0, 3);
        gridpane.add(txtFieldBirthplace, 1, 3);

        // Nationality
        Label labelNationality = new Label("Nationaliteit:");
        txtFieldNationality = new TextField();
        gridpane.add(labelNationality, 0, 4);
        gridpane.add(txtFieldNationality, 1, 4);

        // SSN
        Label labelSSN = new Label("BSN: (alleen cijfers)");
        txtFieldSSN = new NumericTextField(false, MAX_LENGTH_SSN);
        gridpane.add(labelSSN, 0, 5);
        gridpane.add(txtFieldSSN, 1, 5);

        // Passport or ID document number
        Label labelDocumentNumber = new Label("Documentnummer:");
        txtFieldDocumentNumber = new TextField();
        gridpane.add(labelDocumentNumber, 0, 6);
        gridpane.add(txtFieldDocumentNumber, 1, 6);

        // Save button
        HBox hboxSaveButton = new HBox();
        saveButton = new Button("Opslaan");
        hboxSaveButton.getChildren().add(saveButton);
        hboxSaveButton.setAlignment(Pos.CENTER);
        hboxSaveButton.getStyleClass().add("bg-2");
        saveButton.setMaxWidth(MainApplication.getMaxWidthScreen());
        HBox.setHgrow(saveButton, Priority.ALWAYS);

        // ListView
        HBox hboxListview = new HBox();
        listView = new ListView<>();
        hboxListview.getChildren().add(listView);
        hboxListview.setAlignment(Pos.CENTER);
        hboxListview.getStyleClass().add("bg-3");
        HBox.setHgrow(listView, Priority.ALWAYS);

        // Bottom area for buttons
        HBox hboxBottomButtons = new HBox();
        newButton = new Button("Nieuw");
        deleteButton = new Button("Verwijderen");
        switchButton = new Button("Bekijk details");
        hboxBottomButtons.getChildren().addAll(newButton, deleteButton, switchButton);
        hboxBottomButtons.getStyleClass().add("bg-4");
        hboxBottomButtons.setSpacing(10);
        newButton.setMaxWidth(MainApplication.getMaxWidthScreen());
        deleteButton.setMaxWidth(MainApplication.getMaxWidthScreen());
        switchButton.setMaxWidth(MainApplication.getMaxWidthScreen());
        HBox.setHgrow(newButton, Priority.ALWAYS);
        HBox.setHgrow(deleteButton, Priority.ALWAYS);
        HBox.setHgrow(switchButton, Priority.ALWAYS);

        // VBox container
        vboxContainer.getChildren().addAll(gridpane, hboxSaveButton, hboxListview, hboxBottomButtons);
        // Root
        rootBorderPane.setTop(menuBar);
        rootBorderPane.setCenter(vboxContainer);
        return rootBorderPane;
    }

    // All getters
    public MenuItem getMenuItemSave() {
        return menuItemSave;
    }

    public MenuItem getMenuItemLoad() {
        return menuItemLoad;
    }

    public MenuItem getMenuItemClose() {
        return menuItemClose;
    }

    public MenuItem getMenuItemSortAZ() {
        return menuItemSortAZ;
    }

    public MenuItem getMenuItemSortZA() {
        return menuItemSortZA;
    }

    public MenuItem getMenuItemAbout() {
        return menuItemAbout;
    }

    public TextField getTxtFieldName() {
        return txtFieldName;
    }

    public TextField getTxtFieldBirthplace() {
        return txtFieldBirthplace;
    }

    public TextField getTxtFieldNationality() {
        return txtFieldNationality;
    }

    public TextField getTxtFieldSSN() {
        return txtFieldSSN;
    }

    public TextField getTxtFieldDocumentNumber() {
        return txtFieldDocumentNumber;
    }

    public DatePicker getDatePickerBirthdate() {
        return datePickerBirthdate;
    }

    public ComboBox<String> getComboBoxSex() {
        return comboBoxSex;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getNewButton() {
        return newButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getSwitchButton() {
        return switchButton;
    }

    public ListView<Person> getListView() {
        return listView;
    }
}