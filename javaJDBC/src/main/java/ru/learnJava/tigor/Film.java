package ru.learnJava.tigor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        StringBuilder result = new StringBuilder("id=" + id +
                ", " + title +
                ", " + releaseDate +
                ",  " + studio + "\n " +
                "\t\tactors:");
        for(Actor actor: actors)
                result.append(" ").append(actor.getName()).append(" ").append(actor.getSurname()).append(",");
        result.append("\n\t\twriters: ");
        for (Writer writer: writers)
            result.append(" ").append(writer.getName()).append(" ").append(writer.getSurname()).append(",");
        result.append("\n\t\tawards: ");
        for (Award award : awards)
            result.append(" ").append(award.getName()).append(" ").append(award.getEventDate()).append(" ").append(award.getNomination());
        return result.toString();
    }

    public List<Award> getAwards() {
        return awards;
    }

    public void setAwards(List<Award> awards) {
        this.awards = awards;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public List<Writer> getWriters() {
        return writers;
    }

    public void setWriters(List<Writer> writers) {
        this.writers = writers;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
}
