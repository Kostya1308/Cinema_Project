package by.academy.final_project.entities;

import java.time.Year;
import java.util.Objects;

public class Film {

    private int filmId;
    private String name;
    private String country;
    private Year releaseYear;
    private FilmsGenre genre;
    private int ageLimit;

    public Film(int filmId, String name, String country, Year releaseYear, FilmsGenre genre, int ageLimit) {
        this.filmId = filmId;
        this.name = name;
        this.country = country;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.ageLimit = ageLimit;
    }

    public Film(String name, String country, Year releaseYear, FilmsGenre genre, int ageLimit) {
        this.name = name;
        this.country = country;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.ageLimit = ageLimit;
    }

    public int getFilmId() {
        return filmId;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Year getReleaseYear() {
        return releaseYear;
    }

    public FilmsGenre getGenre() {
        return genre;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return ageLimit == film.ageLimit && Objects.equals(name, film.name) && Objects.equals(country, film.country) && Objects.equals(releaseYear, film.releaseYear) && genre == film.genre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country, releaseYear, genre, ageLimit);
    }

    @Override
    public String toString() {
        return name + ": страна - " + country + ", год выхода - " + releaseYear + ", жанр - " + genre +", "+ ageLimit + "+.";
    }
}
