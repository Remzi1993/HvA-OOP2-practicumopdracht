package nl.hva.oop.practicumopdracht.models;

import java.time.LocalDate;

import static nl.hva.oop.practicumopdracht.MainApplication.getDateTimeFormatter;

/**
 * Person model - MasterModel
 * This class represents a person.
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class Person {
    private String name, sex, birthplace, nationality, documentNumber;
    private LocalDate birthdate;
    private int SSN; // Social Security Number (SSN)

    /**
     * This is a constructor of the person model with the following attributes:
     * Name, Sex, Birthdate, Birthplace, Nationality, SSN, Document number
     * @param name is a String and is the full name of a person
     * @param sex is a String denoting the biological sex of a person
     * @param birthdate is a LocalDate denoting the birthdate of a person
     * @param birthplace is a String denoting the birthplace of a person
     * @param nationality is a String denoting the nationality of a person
     * @param SSN is an int denoting the Social Security Number (SSN) of a person
     * @param documentNumber is a String denoting the document number (passport or ID card) of a person
     */
    public Person(String name, String sex, LocalDate birthdate, String birthplace, String nationality, int SSN,
                  String documentNumber) {
        this.name = name;
        this.sex = sex;
        this.birthdate = birthdate;
        this.birthplace = birthplace;
        this.nationality = nationality;
        this.SSN = SSN;
        this.documentNumber = documentNumber;
    }

    @Override
    public String toString() {
        return String.format("Naam: %s - Geslacht: %s - Geboortedatum: %s - Geboorteplaats: %s - " +
                        "Nationaliteit: %s - BSN: %d - Documentnummer: %s]",
                name, sex, getDateTimeFormatter().format(birthdate), birthplace, nationality, SSN, documentNumber);
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public int getSSN() {
        return SSN;
    }

    public void setSSN(int SSN) {
        this.SSN = SSN;
    }
}