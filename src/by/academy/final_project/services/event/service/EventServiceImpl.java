package by.academy.final_project.services.event.service;

import by.academy.final_project.entities.Event;
import by.academy.final_project.exceptions.EventPassed;
import by.academy.final_project.exceptions.NoSuchEvent;
import by.academy.final_project.exceptions.TimeOccupied;
import by.academy.final_project.repository.event_repository.EventRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EventServiceImpl implements EventService{

    EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public boolean addEvent(Event event) {
        return eventRepository.create(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.readAll();
    }

    @Override
    public Event getEvent(Event event) {
        return eventRepository.read(event);
    }

    @Override
    public Event getEvent(String eventId) {
        return eventRepository.read(eventId);
    }

    @Override
    public boolean updateEvent(Event event, Event newEvent) {
        return eventRepository.update(event, newEvent);
    }

    @Override
    public boolean deleteEvent(Event event) {
        return eventRepository.delete(event);
    }

    @Override
    public void isEventFound(Event event) throws NoSuchEvent {
        eventRepository.isEventFound(event);
    }

    @Override
    public void isEventFound(String eventId) throws NoSuchEvent {
        eventRepository.isEventFound(eventId);
    }

    @Override
    public void isFuture(LocalDate date, LocalTime time) throws EventPassed {
        eventRepository.isFuture(date, time);
    }

    @Override
    public void isTimeFree(Event event) throws TimeOccupied {
        eventRepository.isTimeFree(event);
    }

    @Override
    public void isPassHours(LocalDate localDate, LocalTime localTime) throws EventPassed {
        eventRepository.isPassHours(localDate, localTime);
    }
}

