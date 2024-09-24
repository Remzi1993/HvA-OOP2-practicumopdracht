package nl.hva.oop.practicumopdracht.utils;

import java.util.UUID;

public class UUIDGenerator {
    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        System.out.println("Generated UUID: " + uuid);
    }
}