package nl.hva.oop.practicumopdracht.data;

import nl.hva.oop.practicumopdracht.models.Person;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * PersonDAO - MasterDAO
 * Abstract class for PersonDAO - subclasses will implement save and load methods.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public abstract class PersonDAO implements DAO<Person> {
    protected List<Person> persons;

    public PersonDAO() {
        persons = new ArrayList<>();
    }

    public Person getById(int id) {
        try {
            return persons.get(id);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("ID is not in the list");
            return null;
        } catch (Exception e) {
            System.err.println("PersonDAO getById(): Something went wrong!");
            return null;
        }
    }

    public int getIdFor(Person person) {
        return persons.indexOf(person);
    }

    @Override
    public List<Person> getAll() {
        return persons;
    }

    @Override
    public void addOrUpdate(Person person) {
        if(!persons.contains(person)) {
            persons.add(person);
        }
    }

    @Override
    public void remove(Person person) {
        persons.remove(person);
    }

    @Override
    public abstract boolean load() throws FileNotFoundException;

    @Override
    public abstract boolean save();
}