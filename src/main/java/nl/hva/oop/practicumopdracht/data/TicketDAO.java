package nl.hva.oop.practicumopdracht.data;

import nl.hva.oop.practicumopdracht.models.Person;
import nl.hva.oop.practicumopdracht.models.Ticket;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TicketDAO - DetailDAO
 * Abstract class for TicketDAO - subclasses will implement save and load methods.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public abstract class TicketDAO implements DAO<Ticket> {
    protected List<Ticket> tickets;

    public TicketDAO() {
        tickets = new ArrayList<>();
    }

    public List<Ticket> getAllFor(Person person) {
        List<Ticket> ticketsForPerson = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (Objects.equals(ticket.getBelongsTo(), person)) {
                ticketsForPerson.add(ticket);
            }
        }
        return ticketsForPerson;
    }

    public void removeAllFor(Person person) {
        tickets.removeIf(ticket -> Objects.equals(ticket.getBelongsTo(), person));
    }

    @Override
    public List<Ticket> getAll() {
        return tickets;
    }

    @Override
    public void addOrUpdate(Ticket ticket) {
        if(!tickets.contains(ticket)) {
            tickets.add(ticket);
        }
    }

    @Override
    public void remove(Ticket ticket) {
        tickets.remove(ticket);
    }

    @Override
    public abstract boolean load() throws FileNotFoundException;

    @Override
    public abstract boolean save();
}