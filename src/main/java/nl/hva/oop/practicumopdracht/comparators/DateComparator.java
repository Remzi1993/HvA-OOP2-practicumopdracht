package nl.hva.oop.practicumopdracht.comparators;

import nl.hva.oop.practicumopdracht.models.Ticket;

import java.util.Comparator;

public class DateComparator implements Comparator<Ticket> {
    private final boolean ASCENDING;

    /**
     * @param ASCENDING sorting ascending (from the oldest date to the newest date) if TRUE or
     *                  descending (from the newest date to the oldest date) if FALSE.
     */
    public DateComparator(boolean ASCENDING) {
        this.ASCENDING = ASCENDING;
    }

    /**
     * Compares 2 tickets with each other by date
     *
     * @param ticket1 a ticket to compare
     * @param ticket2 another ticket to compare
     * @return returns a sorted list using a ternary operator (shorthand if else) to determine the order.
     * If the dates are equal, it will sort by name.
     */
    @Override
    public int compare(Ticket ticket1, Ticket ticket2) {
        int a = ticket1.getStartDate().compareTo(ticket2.getStartDate());

        // If the dates are the same, sort by destination
        if (a == 0) {
            return ASCENDING ? ticket1.getDestination().compareTo(ticket2.getDestination()) :
                    ticket2.getDestination().compareTo(ticket1.getDestination());
        }

        return ASCENDING ? ticket1.getStartDate().compareTo(ticket2.getStartDate()) :
                ticket2.getStartDate().compareTo(ticket1.getStartDate());
    }
}