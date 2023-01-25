package by.academy.final_project.services.event.service;

import by.academy.final_project.entities.Event;
import by.academy.final_project.exceptions.EventPassed;
import by.academy.final_project.exceptions.NoSuchEvent;
import by.academy.final_project.exceptions.TimeOccupied;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;

public interface EventService {

    boolean addEvent(Event event);

    List<Event> getAllEvents();

    Event getEvent(Event event);

    Event getEvent(String eventId);

    boolean updateEvent(Event event, Event newEvent);

    boolean deleteEvent(Event event);

    void isEventFound(Event event) throws NoSuchEvent;

    void isEventFound(String eventId) throws NoSuchEvent;

    void isFuture(LocalDate date, LocalTime time) throws EventPassed;

    void isTimeFree(Event event) throws TimeOccupied;

    void isPassHours(LocalDate localDate, LocalTime localTime) throws EventPassed;

    default void showItemsByLines(List<Event> set) {
        Iterator<Event> it = set.iterator();
        if (set.size() == 0) {
            System.out.println("позиции отсутсвуют.");
        } else {
            while (it.hasNext()) {
                System.out.println(it.next());
            }
        }
        System.out.println();
    }
}

