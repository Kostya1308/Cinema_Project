package by.academy.final_project.services.film.service;

import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.Film;
import by.academy.final_project.entities.FilmsGenre;
import by.academy.final_project.exceptions.AgeLimit;
import by.academy.final_project.exceptions.NoSuchFilm;
import by.academy.final_project.exceptions.NoSuchGenre;
import by.academy.final_project.services.Service;

import java.util.List;

public interface FilmsService extends Service<Film> {

    boolean addFilm(Film film);

    Film getFilm(String film);
    Film getFilm(int id);
    boolean updateFilm(Film film, Film newFilm);
    boolean deleteFilm(Film film);
    void isFilmExist(String name) throws NoSuchFilm;
    FilmsGenre getGenreFromString(String genre) throws NoSuchGenre;
    void isAgeLimit(String filmName, Consumer consumer) throws AgeLimit;




}
