package ru.learnJava.tigor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilmDao {
    private Connection connection;

    public FilmDao(Connection connection) {
        this.connection = connection;
    }

    public List<Actor> getActorsById(int id) {
        List<Actor> actors = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT a.name, a.surname, a.age FROM actor_has_film ahf" +
                    "    INNER JOIN actor a on ahf.actor_id = a.id AND ahf.film_id =" + id + ";");
            while (rs.next()) {
                Actor actor = new Actor();
                actor.setId(rs.getInt("age"));
                actor.setName(rs.getString("name").trim());
                actor.setSurname(rs.getString("surname").trim());
                actor.setAge(rs.getInt("age"));
                actors.add(actor);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return actors;
    }

    public List<Award> getAwardsById(int id) {
        List<Award> awards = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT fa.id, fa.name, fa.event_date FROM film_has_award fha\n" +
                    "    INNER JOIN film_award fa ON fha.award_id = fa.id AND fha.film_id = " + id + ";");
            while (rs.next()) {
                Award award = new Award();
                award.setId(rs.getInt("id"));
                award.setName(rs.getString("name").trim());
                award.setEventDate(LocalDate.parse(rs.getString("event_date")));
                awards.add(award);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return awards;
    }

    public List<Writer> getWritersById(int id) {
        List<Writer> writers = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT w.name, w.surname, w.age FROM writer_has_film whf " +
                    "    INNER JOIN writer w ON whf.writer_id = w.id AND whf.film_id = " + id + ";");
            while (rs.next()) {
                Writer writer = new Writer();
                writer.setId(rs.getInt("age"));
                writer.setName(rs.getString("name").trim());
                writer.setSurname(rs.getString("surname").trim());
                writer.setAge(rs.getInt("age"));
                writers.add(writer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return writers;
    }

    public List<Film> getALLFilm() {
        List<Film> films = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT f.id, f.title, f.studio, f.release_date, fd.name, fd.surname, w.name, w.surname FROM film AS f" +
                    "    INNER JOIN film_director AS fd ON f.director = fd.id" +
                    "    INNER JOIN writer AS w ON w.id = (SELECT whf.writer_id FROM writer_has_film AS whf WHERE whf.film_id = f.id);");
            while (rs.next()) {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setTitle(rs.getString("title").trim());
                film.setStudio(rs.getString("studio").trim());
                film.setReleaseDate(LocalDate.parse(rs.getString("release_date")));
                film.setActors(getActorsById(film.getId()));
                film.setWriters(getWritersById(film.getId()));
                film.setAwards(getAwardsById(film.getId()));
                films.add(film);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return films;
    }
}
