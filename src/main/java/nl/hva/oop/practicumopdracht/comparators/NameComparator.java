package nl.hva.oop.practicumopdracht.comparators;

import nl.hva.oop.practicumopdracht.models.Person;
import java.util.Comparator;

public class NameComparator implements Comparator<Person> {
    private final boolean ASCENDING;

    /**
     * @param ASCENDING sorting alphabetically ascending (from A to Z) if TRUE or descending (from Z to A) if FALSE
     */
    public NameComparator(boolean ASCENDING) {
        this.ASCENDING = ASCENDING;
    }

    /**
     * Compares 2 persons with each other by name
     *
     * @param person1 a person to compare
     * @param person2 another person to compare
     * @return returns a sorted list using a ternary operator (shorthand if else) to determine the order
     */
    @Override
    public int compare(Person person1, Person person2) {
        return ASCENDING ? person1.getName().compareTo(person2.getName()) : person2.getName().compareTo(person1.getName());
    }
}