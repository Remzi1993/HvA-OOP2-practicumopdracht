package nl.hva.oop.practicumopdracht.comparators;

import nl.hva.oop.practicumopdracht.models.Ticket;
import java.util.Comparator;

public class PriceComparator implements Comparator<Ticket> {
    private final boolean ASCENDING;

    /**
     * @param ASCENDING sorting ascending (from low to high) if TRUE or descending (from high to low) if FALSE
     */
    public PriceComparator(boolean ASCENDING) {
        this.ASCENDING = ASCENDING;
    }

    /**
     * Compares 2 tickets with each other by price
     *
     * @param ticket1 a ticket to compare
     * @param ticket2 another ticket to compare
     * @return returns a sorted list using a ternary operator (shorthand if else) to determine the order
     */
    @Override
    public int compare(Ticket ticket1, Ticket ticket2) {
        return ASCENDING ? Double.compare(ticket1.getCost(), ticket2.getCost()) :
                Double.compare(ticket2.getCost(), ticket1.getCost());
    }
}