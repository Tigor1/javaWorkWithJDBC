package ru.learnJava.tigor;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class App {
    private static final String USER = "tigor";
    private static final String PASSWD = "120197";
    private static final String URL = "jdbc:postgresql://172.18.0.2:5432/filmsCollection"; 


    public static void main(String[] args) {
        Connection connection = geConnection(URL, USER, PASSWD);
        FilmDao film = new FilmDao(connection);

        List<Actor> actors = new ArrayList<>();
        List<Award> awards = new ArrayList<>();
        List<Writer> writers = new ArrayList<>();

        writers.add(new Writer("Writer", "Writerovich", 34));
        actors.add(new Actor("Actor1", "Actorovich1", 21));
        actors.add(new Actor("Actor2", "Actorovich2", 28));
        actors.add(new Actor("Actor3", "Actorovich3", 31));
        awards.add(new Award("Award1", LocalDate.parse("2020-01-24"), "Best of the best"));
        Film film1 = new Film("New Film", LocalDate.parse("2020-01-23"), "Super studio", new Director("Director", "Directovich", 45),
                writers, actors, awards);
        film.addFilm(film1);

        for(Film f : film.getALLFilm())
            System.out.println(f);
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static Connection geConnection(String url, String user, String passwd) {
        System.out.print("com.postgres.jdbc.Driver available check: ");
        try
        {
            Class.forName("org.postgresql.Driver");
            System.out.println("ok");
        } catch(ClassNotFoundException ex) {
            System.out.println("fail: need postgres.jdbc.Driver!");
        }

        Connection connection = null;
        System.out.print("connection to database: ");
        try{
            connection = DriverManager.getConnection(url, user, passwd);
            System.out.println("ok");
        } catch (SQLException e) {
            System.out.println("fail: Could not connect to " + URL);
        }
        return connection;
    }
}