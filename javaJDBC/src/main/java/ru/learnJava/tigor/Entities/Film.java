package ru.learnJava.tigor.Entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Film  {
    private int id;
    private String title;
    private LocalDate releaseDate;
    private String studio;
    private Director director;
    private List<Writer> writers = new ArrayList<>();
    private List<Actor> actors = new ArrayList<>();
    private List<Award> awards = new ArrayList<>();

    public Film() {

    }

    public Film(String title, LocalDate releaseDate, String studio, Director director, List<Writer> writers, List<Actor> actors, List<Award> awards) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.studio = studio;
        this.director = director;
        this.writers = writers;
        this.actors = actors;
        this.awards = awards;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("++++++++++++++++++++++++++++++++\n" +
                title +
                ", " + releaseDate +
                ",  " + studio + "\n ");
        if (!actors.isEmpty()) {
            result.append("\t\tactors:\n");
            for (Actor actor : actors)
                result.append("\t\t\t").append(actor).append("\n");
        }
        if (director != null) {
            result.append("\n\t\tdirector:\n");
            result.append("\t\t\t").append(director).append("\n");
        }
        if (!writers.isEmpty()) {
            result.append("\n\t\twriters:\n");
            for (Writer writer : writers)
                result.append("\t\t\t").append(writer).append("\n");;
        }
        if (!awards.isEmpty()) {
            result.append("\n\t\tawards:\n");
            for (Award award : awards)
                result.append("\t\t\t").append(award).append("\n");
        }
        return result.append("++++++++++++++++++++++++++++++++").toString();
    }
}
