package ru.learnJava.tigor.Entities;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    @Override
    public String toString() {
        return name + " " + nomination + " " + eventDate;
    }
}
