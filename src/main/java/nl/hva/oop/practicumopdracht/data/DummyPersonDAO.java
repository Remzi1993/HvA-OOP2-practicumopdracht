package nl.hva.oop.practicumopdracht.data;

import nl.hva.oop.practicumopdracht.models.Person;

import java.time.LocalDate;

import static nl.hva.oop.practicumopdracht.MainApplication.getDateTimeFormatter;

/**
 * DummyPersonDAO - DummyMasterDAO
 * This is a dummy DAO class that is used to test the application.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class DummyPersonDAO extends PersonDAO {
    @Override
    public boolean load() {
        // Clear the current list to avoid duplicates on reloading
        persons.clear();

        // Name, Sex, Birthdate, Birthplace, Nationality, SSN, Document number
        persons.add(new Person("Remzi Cavdar", "Man",
                LocalDate.parse("11-06-1993", getDateTimeFormatter()), "Amsterdam",
                "Nederlandse", 987654, "HGJH3864"));

        persons.add(new Person("Jeroen van der Heijden", "Man",
                LocalDate.parse("11-06-1993", getDateTimeFormatter()), "Hoofddorp",
                "Nederlandse", 423555, "IJHVE23864"));

        persons.add(new Person("Berkant de Jong", "Man",
                LocalDate.parse("11-06-1993", getDateTimeFormatter()), "Hoofddorp",
                "Nederlandse", 423555, "IJHVE23864"));

        persons.add(new Person("Ali de Groot", "Man",
                LocalDate.parse("11-06-1993", getDateTimeFormatter()), "VerwegistanDorp",
                "Verwegistan", 6575676, "VGHJEE23864"));

        persons.add(new Person("Mohamed de Veteraan", "Man",
                LocalDate.parse("11-06-1993", getDateTimeFormatter()), "Volendam",
                "Nederlandse", 9756589, "MHFDEE23864"));

        persons.add(new Person("Abdulrahman Moon", "Man",
                LocalDate.parse("11-06-1993", getDateTimeFormatter()), "VerwegistanDorp",
                "British", 6744646, "CERGVTHBE23864"));

        persons.add(new Person("Rhodri Barr", "Man",
                LocalDate.parse("11-06-1993", getDateTimeFormatter()), "UK",
                "British", 3456787, "THJE23864"));

        persons.add(new Person("Aldoor van Hagen", "Man",
                LocalDate.parse("11-06-1993", getDateTimeFormatter()), "UK",
                "British", 3456787, "THJE23864"));

        persons.add(new Person("Magna van Hoog", "Man",
                LocalDate.parse("11-06-1993", getDateTimeFormatter()), "UK",
                "British", 3456787, "THJE23864"));

        persons.add(new Person("Siechske van der Domme", "Man",
                LocalDate.parse("11-06-1993", getDateTimeFormatter()), "UK",
                "British", 3456787, "THJE23864"));

        return true;
    }

    @Override
    public boolean save() {
        return false;
    }
}