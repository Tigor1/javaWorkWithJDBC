package ru.learnJava.tigor;

import ru.learnJava.tigor.DAO.FilmDao;
import ru.learnJava.tigor.Entities.Actor;
import ru.learnJava.tigor.Entities.Director;
import ru.learnJava.tigor.Entities.Film;
import ru.learnJava.tigor.Entities.Writer;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class App {
    private static final String USER = "tigor";
    private static final String PASSWD = "120197";
    private static final String URL = "jdbc:postgresql://172.18.0.2:5432/filmsCollection"; 


    public static void main(String[] args) {
        Connection connection = geConnection(URL, USER, PASSWD);
        ConsoleHelper consoleHelper = new ConsoleHelper();
        FilmDao filmDao = new FilmDao(connection);

        printMenu();
        int command;
        try (Scanner scanner = new Scanner(System.in)) {
            do {
                System.out.print("Enter command: ");
                command = scanner.nextInt();

                switch (command) {
                    case 1:
                        Film film = consoleHelper.getFilmFromUser();
                        filmDao.addFilm(film);
                        break;
                    case 2:
                        List<Film> films =  filmDao.getMostAwardedFilm();
                        films.forEach(System.out::println);
                        break;
                    case 3:
                        Director director = consoleHelper.getDirectorFromUser();
                        int amount = filmDao.getAmountFilmOfDirector(director);
                        System.out.println(amount);
                        break;
                    case 4:
                        String studio = consoleHelper.getString();
                        List<Director> directors = filmDao.getDirectorsByStudio(studio);
                        directors.forEach(System.out::println);
                        break;
                    case 5:
                        Actor actor = consoleHelper.getActorFromUser();
                        LocalDate from = consoleHelper.getDate();
                        LocalDate to = consoleHelper.getDate();
                        List<Film> films3 = filmDao.getAllFilmOfActorForPeriod(actor, from, to);
                        films3.forEach(System.out::println);
                        break;
                    case 6:
                        Actor actor1 = consoleHelper.getActorFromUser();
                        Set<Writer> writers = filmDao.getAllWriterByActor(actor1);
                        System.out.println("Writers:");
                        writers.forEach(System.out::println);
                        break;
                    case 7:
                        List<Film> films2 = filmDao.getALLFilm();
                        films2.forEach(System.out::println);

                        break;
                    case 8:
                        System.out.println("Goodbye");
                        break;
                    default:
                        System.out.println("This command does not exist!");
                        break;
                }
            } while (command != 8);
        }

        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void printMenu() {
        System.out.println("-----------------------Film Collections menu-----------------------\n" +
                            "       [1]  Add film\n" +
                            "       [2]  Show the most awarded film(в задании 1)\n" +
                            "       [3]  Show amount films of Director(в задании 2)\n" +
                            "       [4]  Show all Directors which have film on particular studio(в задании 3)\n" +
                            "       [5]  Show all film in which particular actor plays(в задании 4)\n" +
                            "       [6]  Show all writers films in the films of which actors played(в задании 5)\n" +
                            "       [7]  Show all films\n" +
                            "       [8]  Exit\n" +
                "--------------------------------------------------------------------");
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