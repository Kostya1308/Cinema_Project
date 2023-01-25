package by.academy.final_project.services.film.service;

import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.Film;
import by.academy.final_project.entities.FilmsGenre;
import by.academy.final_project.exceptions.AgeLimit;
import by.academy.final_project.exceptions.NoSuchFilm;
import by.academy.final_project.exceptions.NoSuchGenre;
import by.academy.final_project.repository.film_repository.FilmRepository;

import java.util.List;

public class FilmServiceImpl implements FilmsService {

    FilmRepository filmRepository;

    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public boolean addFilm(Film film) {
        return filmRepository.create(film);
    }


    @Override
    public Film getFilm(int id) {
        return filmRepository.read(id);
    }

    @Override
    public Film getFilm(String film) {
        return filmRepository.read(film);
    }

    @Override
    public boolean updateFilm(Film film, Film newFilm) {
        return filmRepository.update(film, newFilm);
    }

    @Override
    public boolean deleteFilm(Film film) {
        return filmRepository.delete(film);
    }

    @Override
    public void isFilmExist(String name) throws NoSuchFilm {
        filmRepository.isFilmExist(name);
    }


    @Override
    public FilmsGenre getGenreFromString(String genre) throws NoSuchGenre {
        return filmRepository.getGenreFromString(genre);
    }

    @Override
    public void isAgeLimit(String filmName, Consumer consumer) throws AgeLimit {
        filmRepository.isAgeLimit(filmName, consumer);
    }
}
