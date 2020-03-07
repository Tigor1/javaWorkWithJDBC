package ru.learnJava.tigor;

import java.time.LocalDate;
import java.util.Date;

public class Award {
    private int id;
    private String name;
    private LocalDate eventDate;
    private String nomination;

    public Award() {

    }

    public Award(String name, LocalDate eventDate, String nomination) {
        this.id = id;
        this.name = name;
        this.eventDate = eventDate;
        this.nomination = nomination;
    }

    public String getNomination() {
        return nomination;
    }

    public void setNomination(String nomination) {
        this.nomination = nomination;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }
}
