package by.academy.final_project.repository.film_repository;

import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.Film;
import by.academy.final_project.entities.FilmsGenre;
import by.academy.final_project.exceptions.AgeLimit;
import by.academy.final_project.exceptions.NoSuchFilm;
import by.academy.final_project.exceptions.NoSuchGenre;
import by.academy.final_project.repository.Crud;

public interface FilmRepository extends Crud<Film> {

    void isFilmExist(Film film) throws NoSuchFilm;

    void isFilmExist(String filmName) throws NoSuchFilm;

    void isFilmExist(int id) throws NoSuchFilm;

    Film read(int id);

    FilmsGenre getGenreFromString(String genre) throws NoSuchGenre;

    void isAgeLimit(String filmName, Consumer consumer) throws AgeLimit;

}
