package by.academy.final_project.repository.event_repository;

import by.academy.final_project.entities.*;
import by.academy.final_project.exceptions.DriverNotLoaded;
import by.academy.final_project.exceptions.EventPassed;
import by.academy.final_project.exceptions.NoSuchEvent;
import by.academy.final_project.exceptions.TimeOccupied;
import by.academy.final_project.repository.film_repository.FilmRepositoryImpl;
import by.academy.final_project.services.film.service.FilmServiceImpl;
import by.academy.final_project.services.film.service.FilmsService;
import by.academy.final_project.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.Duration.between;

public class EventRepositoryImpl implements EventRepository {

    @Override
    public boolean create(Event event) {
        try {
            isEventFound(event);
            return false;
        } catch (NoSuchEvent e) {
            try (Connection connection = ConnectionManager.open()) {
                PreparedStatement stmt1 =
                        connection.prepareStatement("INSERT INTO event (film_id, event_date, event_time) VALUES (?,?,?)");
                stmt1.setInt(1, event.getFilm().getFilmId());
                stmt1.setString(2, event.getEventDate().toString());
                stmt1.setString(3, event.getEventTime().toString());
                stmt1.execute();
                return true;
            } catch (SQLException | DriverNotLoaded e1) {
                System.out.println(e.getMessage());
                return false;
            }
        }
    }

    @Override
    public List<Event> readAll() {
        List<Event> events = new ArrayList<>();

        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM event");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int filmId = resultSet.getInt("film_id");
                LocalDate eventDate = resultSet.getDate("event_date").toLocalDate();
                LocalTime eventTime = resultSet.getTime("event_time").toLocalTime();
                FilmsService filmsService = new FilmServiceImpl(new FilmRepositoryImpl());
                Film film = filmsService.getFilm(filmId);
                Event event = new Event(id, film, eventDate, eventTime);
                events.add(event);
            }
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
        }
        return events;
    }


    @Override
    public Event read(String eventIdString) {
        int eventId = Integer.parseInt(eventIdString);
        Event[] returnEvent = {new Event()};
        readAll().stream()
                .filter(event -> event.getId() == eventId)
                .findAny().ifPresent(event -> returnEvent[0] = event);
        return returnEvent[0];
    }

    @Override
    public Event read(Event event) {
        return readAll().stream()
                .filter(event::equals)
                .findAny()
                .orElse(event);
    }

    @Override
    public boolean update(Event event, Event newEvent) {
        try {
            isEventFound(newEvent);
            return false;
        } catch (NoSuchEvent e) {
            try (Connection connection = ConnectionManager.open()) {
                PreparedStatement stmt =
                        connection.prepareStatement("UPDATE event SET film_id=?, event_date=?, event_time=? WHERE film_id=? AND event_date=? AND event_time=?");
                stmt.setInt(1, newEvent.getFilm().getFilmId());
                stmt.setString(2, newEvent.getEventDate().toString());
                stmt.setString(3, newEvent.getEventTime().toString());
                stmt.setInt(4, event.getFilm().getFilmId());
                stmt.setString(5, event.getEventDate().toString());
                stmt.setString(6, event.getEventTime().toString());
                stmt.execute();
                return true;
            } catch (SQLException | DriverNotLoaded e1) {
                System.out.println(e.getMessage());
                return false;
            }
        }
    }

    @Override
    public boolean delete(Event event) {
        try {
            isEventFound(event);
        } catch (NoSuchEvent e) {
            System.out.println(e.getMessage()+"\n");
            return false;
        }
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt =
                    connection.prepareStatement("DELETE FROM event WHERE id=?");
            stmt.setInt(1, read(event).getId());
            stmt.execute();
            return true;
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void isEventFound(Event event) throws NoSuchEvent {
        boolean isFound = readAll().stream()
                .anyMatch(event::equals);
        if (!isFound) {
            throw new NoSuchEvent("Выбранный фильм не демонстрируется в выбранное время.\nС афишой предстоящих " +
                    "мероприятий Вы можете ознакомиться в личном кабинете.");
        }
    }

    @Override
    public void isEventFound(String eventId) throws NoSuchEvent {
        Integer i = Integer.valueOf(eventId);
        boolean isFound = readAll().stream()
                .map(Event::getId)
                .anyMatch(i::equals);
        if (!isFound) {
            throw new NoSuchEvent("Выбранный фильм не демонстрируется в выбранное время.\nС афишой предстоящих " +
                    "мероприятий Вы можете ознакомиться в личном кабинете.");
        }
    }

    @Override
    public void isFuture(LocalDate date, LocalTime time) throws EventPassed {
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        LocalDateTime now = LocalDateTime.now();
        long between = between(now, localDateTime).toSeconds();
        if (between <= 0) {
            throw new EventPassed("Мероприятие прошло или уже идёт.\nС афишой предстоящих мероприятий Вы можете " +
                    "ознакомиться в личном кабинете.");
        }
    }

    @Override
    public void isPassHours(LocalDate localDate, LocalTime localTime) throws EventPassed {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        long between = between(now, localDateTime).toSeconds();
        if (between < 10800) {
            throw new EventPassed("Вы не можете вернуть билет на мероприятие, до которого осталось менее 3-х часов.");
        }
    }

    @Override
    public void isTimeFree(Event event) throws TimeOccupied {
        LocalDate eventDate = event.getEventDate();
        LocalTime eventTime = event.getEventTime();
        boolean isOccupied = readAll().stream()
                .filter(value -> value.getEventDate().equals(eventDate))
                .anyMatch(value -> value.getEventTime().equals(eventTime));
        if (isOccupied) {
            throw new TimeOccupied("На выбранное время запланировано другое мероприятие.");
        }
    }
}
