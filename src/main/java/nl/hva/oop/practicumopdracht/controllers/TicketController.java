package nl.hva.oop.practicumopdracht.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nl.hva.oop.practicumopdracht.MainApplication;
import nl.hva.oop.practicumopdracht.comparators.DateComparator;
import nl.hva.oop.practicumopdracht.comparators.NameComparator;
import nl.hva.oop.practicumopdracht.comparators.PriceComparator;
import nl.hva.oop.practicumopdracht.data.PersonDAO;
import nl.hva.oop.practicumopdracht.data.TicketDAO;
import nl.hva.oop.practicumopdracht.models.Person;
import nl.hva.oop.practicumopdracht.models.Ticket;
import nl.hva.oop.practicumopdracht.utils.AlertDialog;
import nl.hva.oop.practicumopdracht.utils.DatePickerConverter;
import nl.hva.oop.practicumopdracht.utils.InputHandler;
import nl.hva.oop.practicumopdracht.utils.IsNumeric;
import nl.hva.oop.practicumopdracht.views.AboutView;
import nl.hva.oop.practicumopdracht.views.TicketView;
import nl.hva.oop.practicumopdracht.views.View;
import java.io.FileNotFoundException;
import java.io.InputStream;
import static nl.hva.oop.practicumopdracht.MainApplication.*;
import static nl.hva.oop.practicumopdracht.Main.DEBUG;

/**
 * TicketController - DetailController
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public class TicketController extends Controller {
    private TicketView view;
    private Ticket ticket;
    private TicketDAO ticketDAO;
    private PersonDAO personDAO;
    private AlertDialog alert;
    private Image alertDialogIcon = new MainApplication().getAppIcon();
    private final String APP_CSS = MainApplication.getAppCSS();
    // All the inputs from the view
    private ComboBox<Person> belongsTo;
    private TextField destination, cost;
    private DatePicker startDate, endDate;
    private CheckBox checkedIn;
    private TextArea description;
    private ListView<Ticket> listView;
    private Object[] data;
    private ObservableList<Person> observableListPersons;
    private ObservableList<Ticket> observableListTickets;
    private static InputHandler inputHandler;
    private Person selectedPerson;

    public TicketController(final Person SELECTED_PERSON, final boolean PERSON_NAME_ASCENDING) {
        selectedPerson = SELECTED_PERSON;
        ticketDAO = getTicketDAO();
        personDAO = getPersonDAO();
        view = new TicketView();
        inputHandler = new InputHandler();

        // By default, these buttons are disabled until a person is selected in the listViw
        view.getNewButton().setDisable(true);
        view.getDeleteButton().setDisable(true);

        // Menubar items
        view.getMenuItemSave().setOnAction(this::handleMenuSaveButton);
        view.getMenuItemLoad().setOnAction(this::handleMenuLoadButton);
        view.getMenuItemClose().setOnAction(this::handleMenuCloseButton);
        view.getMenuItemAbout().setOnAction(_ -> new AboutView());

        // Buttons
        view.getSaveButton().setOnAction(this::handleSaveButton);
        view.getNewButton().setOnAction(this::handleNewButton);
        view.getDeleteButton().setOnAction(this::handleDeleteButton);
        view.getSwitchButton().setOnAction(this::handleSwitchButton);
        view.getRadioButtonDate1().setOnAction(this::handleRadioButtonDate1);
        view.getRadioButtonDate2().setOnAction(this::handleRadioButtonDate2);
        view.getRadioButtonCost1().setOnAction(this::handleRadioButtonCost1);
        view.getRadioButtonCost2().setOnAction(this::handleRadioButtonCost2);

        // All the persons in the DAO
        observableListPersons = FXCollections.observableArrayList(personDAO.getAll());
        // Sets the persons name in ascending order or descending order depending on what's passed in the constructor
        observableListPersons.sort(new NameComparator(PERSON_NAME_ASCENDING));
        // Set the items in the belongs to combobox
        view.getComboBoxBelongsTo().setItems(observableListPersons);
        // Selected person from the previous screen
        view.getComboBoxBelongsTo().getSelectionModel().select(selectedPerson);
        // Set the items in the listview
        observableListTickets = FXCollections.observableArrayList(ticketDAO.getAllFor(selectedPerson));
        // Default sorting from date ascending order
        observableListTickets.sort(new DateComparator(true));
        view.getRadioButtonDate1().setSelected(true);
        // Set the items in the listview
        view.getListView().setItems(observableListTickets);

        view.getComboBoxBelongsTo().getSelectionModel().selectedItemProperty().addListener(
                (_, _, newBelongsTo) -> {
                    if (newBelongsTo != null) {
                        /* This is needed to remember the selected person in the combobox, so we can give this to the
                         * PersonController when the switch button is pressed and when PersonView is displayed.
                         */
                        selectedPerson = newBelongsTo;
                        // Set the items in the listview
                        observableListTickets.setAll(ticketDAO.getAllFor(newBelongsTo));
                        // Enable these buttons if a ticket is selected in the listView
                        view.getNewButton().setDisable(true);
                        view.getDeleteButton().setDisable(true);
                        // Reset and clear warnings when a ticket is selected from the listView
                        inputHandler.setTotalErrorValues(0);
                        inputHandler.clearWarnings(data);
                        inputHandler.clearValues(data, false, false);
                    } else {
                        selectedPerson = null;
                    }
                }
        );

        view.getListView().getSelectionModel().selectedItemProperty().addListener((
                _, _, newTicket) -> {
            if (newTicket != null) {
                getInputDataFromView();
                destination.setText(newTicket.getDestination());
                startDate.setValue(newTicket.getStartDate());
                endDate.setValue(newTicket.getEndDate());
                cost.setText(String.valueOf(newTicket.getCost()).replace(".", ","));
                checkedIn.setSelected(newTicket.isCheckedIn());
                description.setText(newTicket.getDescription());

                // Enable these buttons if a ticket is selected in the listView
                view.getNewButton().setDisable(false);
                view.getDeleteButton().setDisable(false);

                // Reset and clear warnings when a ticket is selected from the listView
                inputHandler.setTotalErrorValues(0);
                inputHandler.clearWarnings(data);
            }
        });

        // Datepickers
        startDate = view.getDatePickerStartDate();
        endDate = view.getDatePickerEndDate();
        // Create the DateConverter
        DatePickerConverter datePickerConverter = new DatePickerConverter(getDateFormat());
        // Add the Converter to the DatePicker
        startDate.setConverter(datePickerConverter);
        endDate.setConverter(datePickerConverter);
        // Set the Date in the Prompt
        startDate.setPromptText(getDateFormat().toUpperCase());
        endDate.setPromptText(getDateFormat().toUpperCase());

        startDate.focusedProperty().addListener((_, _, isFocused) -> {
            if (!isFocused) {
                try {
                    // Set typed text to DatePicker value
                    startDate.setValue(startDate.getConverter().fromString(startDate.getEditor().getText()));
                } catch (Exception e) {
                    // For wrong input return old value
                    startDate.getEditor().setText(startDate.getConverter().toString(startDate.getValue()));
                }
            }
        });

        endDate.focusedProperty().addListener((_, _, isFocused) -> {
            if (!isFocused) {
                try {
                    // Set typed text to DatePicker value
                    endDate.setValue(endDate.getConverter().fromString(endDate.getEditor().getText()));
                } catch (Exception e) {
                    // For wrong input return old value
                    endDate.getEditor().setText(endDate.getConverter().toString(endDate.getValue()));
                }
            }
        });
    }

    @Override
    public View getView() {
        return view;
    }

    private void getInputDataFromView() {
        belongsTo = view.getComboBoxBelongsTo();
        destination = view.getTxtDestination();
        startDate = view.getDatePickerStartDate();
        endDate = view.getDatePickerEndDate();
        cost = view.getTxtFieldCost();
        checkedIn = view.getCheckBoxCheckedIn();
        description = view.getTxtAreaDescription();
        listView = view.getListView();
        data = new Object[]{belongsTo, destination, startDate, endDate, cost, checkedIn, description, listView};
    }

    private void handleMenuSaveButton(ActionEvent event) {
        alert = new AlertDialog("CONFIRMATION", "Data opslaan",
                "Wilt u de data opslaan?", alertDialogIcon, APP_CSS);
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            if (personDAO.save() && ticketDAO.save()) {
                menuAlert(true, "Data succesvol opgeslagen",
                        "De data is succesvol opgeslagen.");
            } else {
                menuAlert(false, "Error bij opslaan data!",
                        "Er is een fout opgetreden tijdens het opslaan van de data.");
            }
        }
    }

    private void handleMenuLoadButton(ActionEvent event) {
        alert = new AlertDialog("CONFIRMATION", "Data laden", "Wilt u de data laden?",
                alertDialogIcon, APP_CSS);
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            try {
                view.getComboBoxBelongsTo().getSelectionModel().clearSelection();
                view.getListView().getSelectionModel().clearSelection();

                // Load data from data sources and confirm if successful
                if (personDAO.load() && ticketDAO.load()) {
                    // Update the observable list with the new data
                    observableListPersons.setAll(personDAO.getAll());
                    observableListTickets.setAll(ticketDAO.getAllFor(
                            view.getComboBoxBelongsTo().getSelectionModel().getSelectedItem()
                    ));

                    menuAlert(true, "Data succesvol opgehaald",
                            """
                                    De data is succesvol opgehaald.
                                    Selecteer een persoon om de vliegtickets te zien.""");
                } else {
                    menuAlert(false, "Error bij laden data!",
                            "Er is een fout opgetreden tijdens het laden van de data.");
                }
            } catch (FileNotFoundException e) {
                alert = new AlertDialog("ERROR", "Fout bij laden data!",
                        "Het bestand kon niet gevonden worden", alertDialogIcon, APP_CSS);
                alert.show();
                System.err.println("Couldn't load data!");
                Platform.exit();
                System.exit(0);
            } catch (Exception e) {
                System.err.println("Something went wrong while loading data!");
                e.printStackTrace();
                Platform.exit();
                System.exit(0);
            }
        }
    }

    private void menuAlert(boolean result, String title, String contextText) {
        if (result) {
            alert = new AlertDialog("INFORMATION", title, contextText, alertDialogIcon, APP_CSS);
            alert.show();
        } else {
            alert = new AlertDialog("ERROR", title, contextText, alertDialogIcon, APP_CSS);
            alert.show();
        }
    }

    private void handleMenuCloseButton(ActionEvent event) {
        alert = new AlertDialog("CONFIRMATION", "Afsluiten",
                "Weet u zeker dat u de app wilt afsluiten?", alertDialogIcon, APP_CSS);

        String path1 = "images/emoji/cry.gif";
        try (InputStream inputStream = MainApplication.class.getResourceAsStream(path1)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found as resource: " + path1);
            }
            alert.setGraphic(new ImageView(new Image(inputStream, 100,
                    100, true, true)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load the application icon.", e);
        }

        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }

    private void handleSaveButton(ActionEvent event) {
        if (DEBUG) {
            System.out.println("\nStart action: save");
        }
        getInputDataFromView();

        // String builder
        StringBuilder sb = new StringBuilder("Invoerveld(en) niet of onjuist ingevuld:");

        // Reset and clear warnings
        inputHandler.setTotalErrorValues(0);
        inputHandler.clearWarnings(data);

        IsNumeric isNumeric = new IsNumeric();

        // Only checkedIn and description are allowed to be empty, because they are optional
        if (belongsTo.getSelectionModel().isEmpty() || destination.getText().isBlank() ||
                startDate.getValue() == null || endDate.getValue() == null || cost.getText().isBlank() ||
                !isNumeric.isDouble(cost.getText().replace(",", "."))) {

            /* Show warnings if inputs are empty and/or have incorrect values - empty and/or incorrect inputs get a
            red border */
            inputHandler.checkValues(data);
            inputHandler.checkIsDouble(cost);

            if (belongsTo.getSelectionModel().isEmpty()) {
                sb.append("\nGeen persoon geselecteerd.");
            }
            if (startDate.getValue() == null) {
                sb.append("\nGeen datum vanaf ingevuld.");
            }
            if (endDate.getValue() == null) {
                sb.append("\nGeen datum tot ingevuld.");
            }
            if (cost.getText().isBlank()) {
                sb.append("\nGeen kosten ingevuld.");
            }
            if (!isNumeric.isDouble(cost.getText())) {
                sb.append("\nOnjuist nummer ingevoerd bij kosten.");
            }

            alert = new AlertDialog("WARNING", alertDialogIcon, APP_CSS);
            if (inputHandler.getTotalErrorValues() > 1) {
                alert.setTitle("Invoervelden zijn niet/onjuist ingevuld");
            } else {
                alert.setTitle("Een invoerveld niet/onjuist ingevuld");
            }
            sb.append("\n\nProbeer het opnieuw.");
            alert.setContentText(String.valueOf(sb));
            alert.show();
            return;
        }

        // Get selected ticket from listView and compare it
        ticket = view.getListView().getSelectionModel().getSelectedItem();

        alert = new AlertDialog("INFORMATION", alertDialogIcon, APP_CSS);
        if (ticket == null) {
            // Create new ticket
            ticket = new Ticket(
                    belongsTo.getSelectionModel().getSelectedItem(),
                    destination.getText(),
                    startDate.getValue(), endDate.getValue(),
                    Double.parseDouble(cost.getText().replace(",", ".")),
                    checkedIn.isSelected(), description.getText()
            );

            // Update ListView
            observableListTickets.add(ticket);
            // Alert title and text
            alert.setTitle("Ticket aangemaakt");
            alert.setContentText("Ticket is aangemaakt.");
        } else {
            // Update ticket
            ticket.setBelongsTo(belongsTo.getSelectionModel().getSelectedItem());
            ticket.setDestination(destination.getText());
            ticket.setStartDate(startDate.getValue());
            ticket.setEndDate(endDate.getValue());
            ticket.setCost(Double.parseDouble(cost.getText().replace(",", ".")));
            ticket.setCheckedIn(checkedIn.isSelected());
            ticket.setDescription(description.getText());

            // Alert title and text
            alert.setTitle("Ticket bijgewerkt");
            alert.setContentText("Ticket is bijgewerkt.");
        }

        // Update DAO
        ticketDAO.addOrUpdate(ticket);
        // Show confirmation
        alert.show();
        // Clear everything after successful save
        inputHandler.clearValues(data, false, false);
        // Disable the buttons again until a ticket is selected in the listViw
        view.getNewButton().setDisable(true);
        view.getDeleteButton().setDisable(true);
        if (DEBUG) {
            System.out.println("End action: save");
        }
    }

    private void handleNewButton(ActionEvent event) {
        if (DEBUG) {
            System.out.println("\nStart action: new/clear");
        }

        alert = new AlertDialog("CONFIRMATION", "Ticket aanmaken",
                "Wilt u een nieuw ticket aanmaken?", alertDialogIcon, APP_CSS);
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            getInputDataFromView();
            if (data == null) {
                System.err.println("Data is null");
                return;
            }
            // Clear everything
            inputHandler.clearValues(data, false, false);
            // Clear warnings
            inputHandler.clearWarnings(data);
            // Disable the buttons again until a ticket is selected in the listViw
            view.getNewButton().setDisable(true);
            view.getDeleteButton().setDisable(true);
        }

        if (DEBUG) {
            System.out.println("End action: new/clear");
        }
    }

    private void handleDeleteButton(ActionEvent event) {
        if (DEBUG) {
            System.out.println("\nStart action: delete");
        }
        ticket = view.getListView().getSelectionModel().getSelectedItem();

        // If nothing is selected in the listview
        if (ticket == null) {
            alert = new AlertDialog("Selecteer ticket",
                    "Selecteer een ticket dat u wilt verwijderen.", alertDialogIcon, APP_CSS);
            alert.show();
            return;
        }

        alert = new AlertDialog("CONFIRMATION", "Ticket verwijderen",
                "Wilt u dit vliegticket verwijderen?", alertDialogIcon, APP_CSS);
        alert.show();

        if (alert.getResult() == ButtonType.OK) {
            // Update DAO
            ticketDAO.remove(ticket);
            // Update ListView
            observableListTickets.remove(ticket);
            // Clear listView selection and inputs
            view.getListView().getSelectionModel().clearSelection();
            inputHandler.clearValues(data, false, false);
            // Disable the buttons again until a ticket is selected in the listViw
            view.getNewButton().setDisable(true);
            view.getDeleteButton().setDisable(true);
        }

        if (DEBUG) {
            System.out.println("End action: delete");
        }
    }

    private void handleRadioButtonDate1(ActionEvent event) {
        observableListTickets.sort(new DateComparator(true));
    }

    private void handleRadioButtonDate2(ActionEvent event) {
        observableListTickets.sort(new DateComparator(false));
    }

    private void handleRadioButtonCost1(ActionEvent event) {
        observableListTickets.sort(new PriceComparator(true));
    }

    private void handleRadioButtonCost2(ActionEvent event) {
        observableListTickets.sort(new PriceComparator(false));
    }

    private void handleSwitchButton(ActionEvent event) {
        switchController(new PersonController(selectedPerson));
    }
}