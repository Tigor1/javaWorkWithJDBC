package ru.learnJava.tigor;

import java.sql.*;

public class App {
    private static final String USER = "tigor";
    private static final String PASSWD = "120197";
    private static final String URL = "jdbc:postgresql://172.18.0.2:5432/filmsCollection"; 


    public static void main(String[] args) {
        Connection connection = geConnection(URL, USER, PASSWD);
        FilmDao film = new FilmDao(connection);
        for(Film f : film.getALLFilm())
            System.out.println(f);
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