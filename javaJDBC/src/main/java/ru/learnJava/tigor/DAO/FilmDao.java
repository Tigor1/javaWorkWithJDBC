package ru.learnJava.tigor.DAO;

import ru.learnJava.tigor.Entities.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilmDao {
    private Connection connection;

    public FilmDao(Connection connection) {
        this.connection = connection;
    }

    public List<Actor> getActorsById(int id) {
        List<Actor> actors = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {

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

        try (Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery("SELECT fa.id, fa.name, fa.event_date, fa.nomination FROM film_has_award fha\n" +
                    "    INNER JOIN film_award fa ON fha.award_id = fa.id AND fha.film_id = " + id + ";");
            while (rs.next()) {
                Award award = new Award();
                award.setId(rs.getInt("id"));
                award.setName(rs.getString("name").trim());
                award.setEventDate(rs.getDate("event_date").toLocalDate());
                award.setNomination(rs.getString("nomination"));
                awards.add(award);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return awards;
    }

    public List<Writer> getWritersById(int id) {
        List<Writer> writers = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {

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

    public int getIdActor(Actor actor, PreparedStatement ps) throws SQLException {
        int id = 0;

        ps.setString(1, actor.getName());
        ps.setString(2, actor.getSurname());
        ps.setInt(3, actor.getAge());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) id = rs.getInt("id");
        return id;
    }

    public int getIdWrite(Writer writer, PreparedStatement ps) throws SQLException {
        int id = 0;

        ps.setString(1, writer.getName());
        ps.setString(2, writer.getSurname());
        ps.setInt(3, writer.getAge());
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            id = rs.getInt("id");
        return id;
    }

    public int getIdDirector(Director director, PreparedStatement ps) throws SQLException {
        int id = 0;

        ps.setString(1, director.getName());
        ps.setString(2, director.getSurname());
        ps.setInt(3, director.getAge());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) id = rs.getInt("id");
        return id;
    }

    //1
    public List<Film> getMostAwardedFilm() {
        List<Film> films = new ArrayList<>();

        try (Statement maf = connection.createStatement()) {
            connection.setAutoCommit(false);

            ResultSet rs = maf.executeQuery("SELECT id, title, release_date, studio FROM film f\n" +
                    "    WHERE id = (SELECT fa.i FROM (SELECT film_id AS i, count(award_id) as m_p FROM film_has_award GROUP BY i) AS fa\n" +
                    "    WHERE fa.m_p = (SELECT max(fa.m_p) FROM (SELECT film_id AS i, count(award_id) as m_p FROM film_has_award GROUP BY i) AS fa));");

            while (rs.next()) {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setTitle(rs.getString("title").trim());
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setStudio(rs.getString("studio").trim());
                films.add(film);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex1){
                System.out.println(ex1.getMessage());
            }
        }
        return films;
    }

    //2
    public int getAmountFilmOfDirector(Director director) {
        int amountFilm = 0;
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement("SELECT count(id) AS amount FROM film " +
                    "WHERE film.director = (SELECT id FROM film_director WHERE name = ? AND surname = ?);");

            ps.setString(1, director.getName());
            ps.setString(2, director.getSurname());

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                amountFilm = rs.getInt("amount");
        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException closeSqlEx) {
                System.out.println(closeSqlEx.getMessage());
            }
        }
        return amountFilm;
    }

    //3
    public List<Director> getDirectorsByStudio(String studio) {
        List<Director> directors = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement("SELECT id, name, surname, age FROM film_director " +
                "INNER JOIN (SELECT film.director as dir_id FROM film WHERE film.studio = ?) std\n" +
                "    ON id = std.dir_id;")) {
            ps.setString(1, studio);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Director director = new Director();
                director.setId(rs.getInt("id"));
                director.setName(rs.getString("name").trim());
                director.setSurname(rs.getString("surname").trim());
                director.setAge(rs.getInt("age"));
                directors.add(director);
            }
        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
        }
        return directors;
    }

    //4
    public List<Film> getAllFilmOfActorForPeriod(Actor actor, LocalDate from, LocalDate to) {
        if (from.isAfter(to))
            return null;

        List<Film> films = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement("SELECT f.id, f.title, f.release_date, f.studio FROM actor_has_film ahf\n" +
                "    INNER JOIN (SELECT id FROM actor WHERE name = ? AND surname = ?) AS atr ON ahf.actor_id = atr.id\n" +
                "    INNER JOIN film AS f ON film_id = f.id AND f.release_date > ? AND f.release_date < ?;")) {

            ps.setString(1, actor.getName());
            ps.setString(2, actor.getSurname());
            ps.setDate(3, Date.valueOf(from));
            ps.setDate(4, Date.valueOf(to));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setTitle(rs.getString("title").trim());
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setStudio(rs.getString("studio").trim());
                films.add(film);
            }

        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
        }
        return films;
    }

    //5
    public Set<Writer> getAllWriterByActor(Actor actor) {
        Set<Writer> writers = new HashSet<>();

        try (PreparedStatement ps = connection.prepareStatement("SELECT w.id, w.name, w.surname, w.age FROM actor_has_film ahf\n" +
                "    INNER JOIN actor a ON a.name = ? AND a.surname = ? AND ahf.actor_id = a.id\n" +
                "    INNER JOIN writer_has_film whf ON ahf.film_id = whf.film_id\n" +
                "    INNER JOIN writer w ON w.id = whf.writer_id;")) {

            ps.setString(1, actor.getName());
            ps.setString(2, actor.getSurname());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Writer writer = new Writer();

                writer.setId(rs.getInt("id"));
                writer.setName(rs.getString("name").trim());
                writer.setSurname(rs.getString("surname").trim());
                writer.setAge(rs.getInt("age"));
                writers.add(writer);
            }

        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
        }
        return writers;
    }

    public void addFilm(Film film) {

        String actorSql = "INSERT INTO actor (name, surname, age)" +
                            " VALUES (?, ?, ?) ON CONFLICT (name, surname, age) DO NOTHING";
        String writerSql = "INSERT INTO writer (name, surname, age) " +
                            " VALUES(?, ?, ?) ON CONFLICT (name, surname, age) DO NOTHING;";
        String awardSql = "INSERT INTO film_award (name, event_date, nomination) " +
                "VALUES(?, ?, ?) ON CONFLICT (name, event_date, nomination) DO NOTHING;";

        String directorSql = "INSERT INTO film_director(name, surname, age) " +
                "VALUES(?, ?, ?) ON CONFLICT (name, surname, age) DO NOTHING;";

        String filmSql = "INSERT INTO film (title, release_date, director, studio) " +
                " VALUES (?, ?, ?, ?) ON CONFLICT (title, release_date, director) DO NOTHING ;";

        String actorHasFilmSql = "INSERT INTO actor_has_film (film_id, actor_id) VALUES (?, ?);";
        String filmHasAwardSql = "INSERT INTO film_has_award (film_id, award_id) VALUES (?, ?);";
        String writerHasFilmSql = "INSERT INTO writer_has_film (film_id, writer_id) VALUES (?, ?);";
        String actorIdSql = "SELECT id FROM actor WHERE name = ? AND surname = ? AND age = ?;";
        String writeIdSql = "SELECT id FROM writer WHERE name = ? AND surname = ? AND age = ?;";
        String directorIdSql = "SELECT id FROM film_director WHERE name = ? AND surname = ? AND age = ?;";

        try (PreparedStatement addActor = connection.prepareStatement(actorSql, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement addWriter = connection.prepareStatement(writerSql, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement addAward = connection.prepareStatement(awardSql, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement addDirector = connection.prepareStatement(directorSql, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement addFilm = connection.prepareStatement(filmSql, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement addActorHasFilm = connection.prepareStatement(actorHasFilmSql);
            PreparedStatement addFilmHasAward = connection.prepareStatement(filmHasAwardSql);
            PreparedStatement addWriterHasFilm = connection.prepareStatement(writerHasFilmSql);
            PreparedStatement getIdA = connection.prepareStatement(actorIdSql);
            PreparedStatement getIdW = connection.prepareStatement(writeIdSql);
            PreparedStatement getIdD = connection.prepareStatement(directorIdSql)) {

            connection.setAutoCommit(false);

            addFilm.setString(1, film.getTitle());
            addFilm.setDate(2, Date.valueOf(film.getReleaseDate()));

            int idDirector = addDirector(film.getDirector(), addDirector);
            if (idDirector == 0) idDirector = getIdDirector(film.getDirector(), getIdD);
            addFilm.setInt(3, idDirector);
            addFilm.setString(4, film.getStudio());
            addFilm.executeUpdate();

            int idFilm = 0;
            ResultSet rs = addFilm.getGeneratedKeys();
            if (rs.next()) idFilm = rs.getInt(1);

            for (Actor actor : film.getActors()) {
                int actorId = addActor(actor, addActor);
                if (actorId == 0) actorId = getIdActor(actor, getIdA);
                addActorHasFilm(idFilm, actorId, addActorHasFilm);
            }
            for (Writer writer : film.getWriters()) {
                int writerId = addWriter(writer, addWriter);
                if (writerId == 0) writerId = getIdWrite(writer, getIdW);
                addWriterHasFilm(idFilm, writerId, addWriterHasFilm);
            }
            for (Award award : film.getAwards()) {
                int awardId = addAward(award, addAward);
                addFilmHasAward(idFilm, awardId, addFilmHasAward);
            }
            connection.commit();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                ex.printStackTrace();
            }
        }
    }

    public void addActorHasFilm(int filmId, int actorId, PreparedStatement addAhf) throws SQLException {
        addAhf.setInt(1, filmId);
        addAhf.setInt(2, actorId);
        addAhf.executeUpdate();
    }

    public void addFilmHasAward(int filmId, int awardId, PreparedStatement addFha) throws SQLException {
        addFha.setInt(1, filmId);
        addFha.setInt(2, awardId);
        addFha.executeUpdate();
    }

    public void addWriterHasFilm(int filmId, int writerId, PreparedStatement addWhf) throws SQLException {
        addWhf.setInt(1, filmId);
        addWhf.setInt(2, writerId);
        addWhf.executeUpdate();
    }

    public int addDirector(Director director, PreparedStatement addDirector) throws SQLException {
        int result = 0;

        addDirector.setString(1, director.getName());
        addDirector.setString(2, director.getSurname());
        addDirector.setInt(3, director.getAge());

        addDirector.executeUpdate();
        ResultSet rs = addDirector.getGeneratedKeys();
        if (rs.next())
            result = rs.getInt(1);
        return result;
    }

    public int addActor(Actor actor, PreparedStatement addActor) throws SQLException {
        int result = 0;

        addActor.setString(1, actor.getName());
        addActor.setString(2, actor.getSurname());
        addActor.setInt(3, actor.getAge());

        addActor.executeUpdate();
        ResultSet id = addActor.getGeneratedKeys();

        if (id.next())
            result = id.getInt(1);
        return result;
    }

    public int addWriter(Writer writer, PreparedStatement addWriter) throws SQLException {
        int result = 0;

        addWriter.setString(1, writer.getName());
        addWriter.setString(2, writer.getSurname());
        addWriter.setInt(3, writer.getAge());

        addWriter.executeUpdate();
        ResultSet id = addWriter.getGeneratedKeys();

        if (id.next())
            result = id.getInt(1);
        return result;
    }

    public int addAward(Award award, PreparedStatement addAward) throws SQLException {
        int result = 0;

        addAward.setString(1, award.getName());
        addAward.setDate(2, Date.valueOf(award.getEventDate()));
        addAward.setString(3, award.getNomination());

        addAward.executeUpdate();
        ResultSet id = addAward.getGeneratedKeys();

        if (id.next())
            result = id.getInt(1);
        return result;
    }

    public List<Film> getALLFilm() {
        List<Film> films = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery("SELECT f.id, f.title, f.studio, f.release_date, fd.name, fd.surname, fd.age, w.name, w.surname FROM film AS f" +
                    "    INNER JOIN film_director AS fd ON f.director = fd.id" +
                    "    INNER JOIN writer AS w ON w.id = (SELECT whf.writer_id FROM writer_has_film AS whf WHERE whf.film_id = f.id);");
            while (rs.next()) {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setTitle(rs.getString("title").trim());
                film.setStudio(rs.getString("studio").trim());
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setActors(getActorsById(film.getId()));
                film.setWriters(getWritersById(film.getId()));
                film.setDirector(new Director(rs.getString("name").trim(), rs.getString("surname").trim(), rs.getInt("age")));
                film.setAwards(getAwardsById(film.getId()));
                films.add(film);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return films;
    }
}
