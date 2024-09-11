package nl.hva.oop.practicumopdracht.models;

import java.io.Serializable;
import java.time.LocalDate;

import static nl.hva.oop.practicumopdracht.MainApplication.getDateTimeFormatter;

/**
 * Ticket model - DetailModel
 * This class represents an airline ticket.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class Ticket implements Serializable {
    private Person belongsTo;
    private String destination, description;
    private LocalDate startDate, endDate;
    private double cost;
    private boolean checkedIn;

    /**
     * This is a constructor of the ticket model with the following attributes:
     * Destination, Description, Start date, End date, Cost, Checked in
     * @param belongsTo is a Person object denoting the person who owns the ticket
     * @param destination is a String denoting the destination of the ticket
     * @param startDate is a LocalDate denoting the start date of the ticket
     * @param endDate is a LocalDate denoting the end date of the ticket
     * @param cost is a double denoting the cost of the ticket
     * @param checkedIn is a boolean denoting whether the person has checked in or not
     * @param description is a String denoting the description of the ticket
     */
    public Ticket(Person belongsTo, String destination, LocalDate startDate, LocalDate endDate, double cost,
                  boolean checkedIn, String description) {
        this.belongsTo = belongsTo;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cost = cost;
        this.checkedIn = checkedIn;
        this.description = description;
    }

    public Ticket(String destination, LocalDate startDate, LocalDate endDate, double cost,
                  boolean checkedIn, String description) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cost = cost;
        this.checkedIn = checkedIn;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Ticket: [Bestemming: %s - Datum vanaf: %s - Datum tot: %s - Kosten: %.2f - Checked in: %b]" +
                        "%nBeschrijving: %s",
                destination, getDateTimeFormatter().format(startDate),
                getDateTimeFormatter().format(endDate), cost, checkedIn, description);
    }

    // Getters and setters
    public Person getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(Person belongsTo) {
        this.belongsTo = belongsTo;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}