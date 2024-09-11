package nl.hva.oop.practicumopdracht.data;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Interface for DAO abstract classes.
 * @param <T> model Person or Ticket
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */
public interface DAO<T> {
    List<T> getAll();
    void addOrUpdate(T object);
    void remove(T object);
    boolean save();
    boolean load() throws FileNotFoundException;
}