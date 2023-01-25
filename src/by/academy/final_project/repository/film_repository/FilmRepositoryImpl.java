package by.academy.final_project.repository.film_repository;

import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.Film;
import by.academy.final_project.entities.FilmsGenre;
import by.academy.final_project.exceptions.AgeLimit;
import by.academy.final_project.exceptions.DriverNotLoaded;
import by.academy.final_project.exceptions.NoSuchFilm;
import by.academy.final_project.exceptions.NoSuchGenre;
import by.academy.final_project.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class FilmRepositoryImpl implements FilmRepository {

    @Override
    public boolean create(Film film) {
        try {
            isFilmExist(film);
            return false;
        } catch (NoSuchFilm e) {
            try (Connection connection = ConnectionManager.open()) {
                PreparedStatement stmt =
                        connection.prepareStatement("INSERT INTO film (film_name, country, release_year, genre, age_limit) VALUES (?,?,?,?,?)");
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getCountry());
                stmt.setString(3, film.getReleaseYear().toString());
                stmt.setInt(4, film.getGenre().ordinal() + 1);
                stmt.setInt(5, film.getAgeLimit());
                stmt.execute();
                return true;
            } catch (SQLException | DriverNotLoaded ex) {
                System.out.println(e.getMessage());
                return false;
            }
        }
    }

    @Override
    public List<Film> readAll() {
        List<Film> films = new ArrayList<>();

        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM film");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("film_name");
                String country = resultSet.getString("country");
                Year releaseYear = Year.of(resultSet.getDate("release_year").toLocalDate().getYear());
                FilmsGenre genre;
                try {
                    genre = getGenreFromString(resultSet.getString("genre"));
                } catch (NoSuchGenre e) {
                    e.printStackTrace();
                    return null;
                }
                int ageLimit = resultSet.getInt("age_limit");
                Film film = new Film(id, name, country, releaseYear, genre, ageLimit);
                films.add(film);
            }
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
        }
        return films;
    }

    @Override
    public Film read(Film film) {
        try {
            isFilmExist(film);
        } catch (NoSuchFilm e) {
            e.printStackTrace();
        }
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM film WHERE film_name=?");
            stmt.setString(1, film.getName());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String country = resultSet.getString("country");
                Year releaseYear = Year.of(resultSet.getDate("release_year").toLocalDate().getYear());
                FilmsGenre genre;
                try {
                    genre = getGenreFromString(resultSet.getString("genre"));
                } catch (NoSuchGenre e) {
                    e.printStackTrace();
                    return null;
                }
                int ageLimit = resultSet.getInt("age_limit");
                film = new Film(id, film.getName(), country, releaseYear, genre, ageLimit);
            }
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
        }
        return film;
    }

    @Override
    public Film read(String filmName) {
        try {
            isFilmExist(filmName);
        } catch (NoSuchFilm e) {
            e.printStackTrace();
        }
        Film film = null;
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM film WHERE film_name=?");
            stmt.setString(1, filmName);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String country = resultSet.getString("country");
                Year releaseYear = Year.of(resultSet.getDate("release_year").toLocalDate().getYear());
                FilmsGenre genre;
                try {
                    genre = getGenreFromString(resultSet.getString("genre"));
                } catch (NoSuchGenre e) {
                    e.printStackTrace();
                    return null;
                }
                int ageLimit = resultSet.getInt("age_limit");
                film = new Film(id, filmName, country, releaseYear, genre, ageLimit);
            }
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
        }
        return film;
    }

    @Override
    public Film read(int id) {
        try {
            isFilmExist(id);
        } catch (NoSuchFilm e) {
            e.printStackTrace();
        }
        Film film = null;
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM film WHERE id=?");
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                String filmName = resultSet.getString("film_name");
                String country = resultSet.getString("country");
                Year releaseYear = Year.of(resultSet.getDate("release_year").toLocalDate().getYear());
                FilmsGenre genre;
                try {
                    genre = getGenreFromString(resultSet.getString("genre"));
                } catch (NoSuchGenre e) {
                    e.printStackTrace();
                    return null;
                }
                int ageLimit = resultSet.getInt("age_limit");
                film = new Film(id, filmName, country, releaseYear, genre, ageLimit);
            }
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
        }
        return film;
    }

    @Override
    public boolean update(Film film, Film newFilm) {
        try {
            isFilmExist(film);
        } catch (NoSuchFilm e) {
            e.printStackTrace();
            return false;
        }
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt =
                    connection.prepareStatement("UPDATE film SET film_name=?, country=?, release_year=?, genre=?, age_limit=?  WHERE film_name=?");
            stmt.setString(1, newFilm.getName());
            stmt.setString(2, newFilm.getCountry());
            stmt.setString(3, newFilm.getReleaseYear().toString());
            stmt.setInt(4, newFilm.getGenre().ordinal() + 1);
            stmt.setInt(5, newFilm.getAgeLimit());
            stmt.setString(6, film.getName());
            stmt.execute();
            return true;
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Film film) {
        try {
            isFilmExist(film);
        } catch (NoSuchFilm e) {
            e.printStackTrace();
            return false;
        }
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt =
                    connection.prepareStatement("DELETE FROM film WHERE film_name=?");
            stmt.setString(1, film.getName());
            stmt.execute();
            return true;
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public FilmsGenre getGenreFromString(String genre) throws NoSuchGenre {
        FilmsGenre filmsGenre;
        switch (genre) {
            case "SHORT_FILM" -> filmsGenre = FilmsGenre.SHORT_FILM;
            case "ACTION" -> filmsGenre = FilmsGenre.ACTION;
            case "ADVENTURE" -> filmsGenre = FilmsGenre.ADVENTURE;
            case "COMEDY" -> filmsGenre = FilmsGenre.COMEDY;
            case "DRAMA" -> filmsGenre = FilmsGenre.DRAMA;
            case "CRIME" -> filmsGenre = FilmsGenre.CRIME;
            case "HORROR" -> filmsGenre = FilmsGenre.HORROR;
            case "FANTASY" -> filmsGenre = FilmsGenre.FANTASY;
            case "ROMANCE" -> filmsGenre = FilmsGenre.ROMANCE;
            case "THRILLER" -> filmsGenre = FilmsGenre.THRILLER;
            case "ANIMATION" -> filmsGenre = FilmsGenre.ANIMATION;
            case "FAMILY" -> filmsGenre = FilmsGenre.FAMILY;
            case "WAR" -> filmsGenre = FilmsGenre.WAR;
            case "DOCUMENTARY" -> filmsGenre = FilmsGenre.DOCUMENTARY;
            case "MUSICAL" -> filmsGenre = FilmsGenre.MUSICAL;
            case "BIOGRAPHY" -> filmsGenre = FilmsGenre.BIOGRAPHY;
            case "SCI_FI" -> filmsGenre = FilmsGenre.SCI_FI;
            case "WESTERN" -> filmsGenre = FilmsGenre.WESTERN;
            case "POST_APOCALYPTIC" -> filmsGenre = FilmsGenre.POST_APOCALYPTIC;
            default -> throw new NoSuchGenre("Жанр фильма не найден");
        }
        return filmsGenre;
    }

    @Override
    public void isFilmExist(Film film) throws NoSuchFilm {
        boolean isExist = readAll().stream()
                .anyMatch(film::equals);
        if (!isExist) {
            throw new NoSuchFilm("Фильм не найден.");
        }
    }

    @Override
    public void isFilmExist(String name) throws NoSuchFilm {
        boolean isExist = readAll().stream()
                .map(Film::getName)
                .anyMatch(name::equals);
        if (!isExist) {
            throw new NoSuchFilm("Фильм не найден.");
        }
    }

    @Override
    public void isFilmExist(int id) throws NoSuchFilm {
        boolean isExist = readAll().stream()
                .map(Film::getFilmId)
                .anyMatch(value -> id == value);
        if (!isExist) {
            throw new NoSuchFilm("Фильм не найден.");
        }

    }

    @Override
    public void isAgeLimit(String filmName, Consumer consumer) throws AgeLimit {
        int ageLimit = read(filmName).getAgeLimit();
        LocalDate dateOfBirth = consumer.getDateOfBirth();
        int years = Period.between(dateOfBirth, LocalDate.now()).getYears();
        if (years < ageLimit) {
            throw new AgeLimit("Возрастное ограничение - " + ageLimit + "+");
        }

    }
}
