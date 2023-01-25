package by.academy.final_project.comparators;

import by.academy.final_project.entities.Event;

import java.util.Comparator;

public class EventsGenreComparator implements Comparator<Event>{
    @Override
    public int compare(Event o1, Event o2) {
        return o1.getFilm().getGenre().toString().compareTo(o2.getFilm().getGenre().toString());

    }
}
