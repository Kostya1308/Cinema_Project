package by.academy.final_project.repository.event_repository;

import by.academy.final_project.entities.Event;
import by.academy.final_project.exceptions.EventPassed;
import by.academy.final_project.exceptions.NoSuchEvent;
import by.academy.final_project.exceptions.TimeOccupied;
import by.academy.final_project.repository.Crud;

import java.time.LocalDate;
import java.time.LocalTime;

public interface EventRepository extends Crud<Event> {

    void isEventFound(Event event) throws NoSuchEvent;

    void isEventFound(String eventId) throws NoSuchEvent;

    void isFuture(LocalDate date, LocalTime time) throws EventPassed;

    void isTimeFree(Event event) throws TimeOccupied;

    public void isPassHours(LocalDate localDate, LocalTime localTime) throws EventPassed;


}
