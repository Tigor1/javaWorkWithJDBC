package ru.learnJava.tigor;

import ru.learnJava.tigor.Entities.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsoleHelper {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public LocalDate getDate() {
        System.out.println("Enter date(format yyyy-mm-dd): ");
        LocalDate date = null;
        try {
            date = LocalDate.parse(reader.readLine());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return date;
    }

    public int getIdFromUser() {
        int result = -1;
        try {
            result = Integer.parseInt(reader.readLine());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public String getString() {
        String result = "";
        try{
            System.out.println("Enter studio:");
            result = reader.readLine();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public Actor getActorFromUser() {
        Actor actor = new Actor();
        boolean isActorReady = false;

        while (!isActorReady) {
            try {
                System.out.println("Enter actor name: ");
                actor.setName(reader.readLine());
                System.out.println("Enter actor surname: ");
                actor.setSurname(reader.readLine());
                isActorReady = true;
            } catch (NumberFormatException numEx) {
                System.out.println("This isn't number!");
            }  catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return actor;
    }

    public Writer getWriterFromUser() {
        Writer writer = new Writer();
        boolean isWriteReady = false;

        while (!isWriteReady) {
            try {
                System.out.println("Enter write name: ");
                writer.setName(reader.readLine());
                System.out.println("Enter write surname: ");
                writer.setName(reader.readLine());
                isWriteReady = true;
            } catch (NumberFormatException numEx) {
                System.out.println("This isn't number!");
            }  catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return writer;
    }

    public Director getDirectorFromUser() {
        Director director = new Director();
        boolean isDirectorReady = false;
        
        while (!isDirectorReady) {
            try {
                System.out.println("Enter director name: ");
                director.setName(reader.readLine());
                System.out.println("Enter director surname: ");
                director.setSurname(reader.readLine());
                isDirectorReady = true;
            } catch (NumberFormatException numEx) {
                System.out.println("This isn't number!");
            }  catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return director;
    }

    public Award getAwardFromUser() {
        Award award = new Award();
        boolean isAwardReady = false;

        while (!isAwardReady) {
            try {
                System.out.println("Enter name of award: ");
                award.setName(reader.readLine());
                System.out.println("Enter nomination: ");
                award.setNomination(reader.readLine());
                System.out.println("Enter event date (format YYYY-MM-DD): ");
                LocalDate date = LocalDate.parse(reader.readLine());
                if (date.isAfter(LocalDate.now()))
                    throw new IOException("Date must be less then now");
                award.setEventDate(date);
                isAwardReady = true;
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return award;
    }

    public Film getFilmFromUser() {
        Film film = new Film();

        try {
            System.out.println("Enter title of film: ");
            film.setTitle(reader.readLine());
            System.out.println("Enter film studio: ");
            film.setStudio(reader.readLine());
            System.out.println("Enter release date (format YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(reader.readLine());
            if (date.isAfter(LocalDate.now()))
                throw new IOException("Date must be less then now");
            film.setDirector(getDirectorFromUser());

            List<Actor> actors = new ArrayList<>();
            while (true) {
                System.out.println("Add actor? ('y' - yes, any - no): ");
                if (!reader.readLine().equals("y"))
                    break;
                actors.add(getActorFromUser());
            }
            film.setActors(actors);

            List<Writer> writers = new ArrayList<>();
            while (true) {
                System.out.println("Add writer? ('y' - yes, any - no): ");
                if (!reader.readLine().equals("y"))
                    break;
                writers.add(getWriterFromUser());
            }
            film.setWriters(writers);

            List<Award> awards = new ArrayList<>();
            while (true) {
                System.out.println("Add award? ('y' - yes, any - no): ");
                if (!reader.readLine().equals("y"))
                    break;
                awards.add(getAwardFromUser());
            }
            film.setAwards(awards);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return film;
    }
}
