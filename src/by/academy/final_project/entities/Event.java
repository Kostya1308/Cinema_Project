package by.academy.final_project.entities;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Event {

    private int id;
    private Film film;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private Ticket[][] tickets;


    public Event() {
        this.tickets = createTickets(LocalTime.now());
    }

    public Event(int id, Film film, LocalDate eventDate, LocalTime eventTime) {
        this.id = id;
        this.film = film;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.tickets = createTickets(eventTime);
    }

    public Event(Film film, LocalDate eventDate, LocalTime eventTime) {
        this.film = film;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.tickets = createTickets(eventTime);
    }

    private Ticket[][] createTickets(LocalTime eventTime) {
        Ticket[][] tickets = new Ticket[10][23];
        float priceDependsTime = createPriceDependOnTime(eventTime);
        for (int i = 0; i < tickets.length; i++) {
            for (int j = 0; j < tickets[i].length; j++) {
                tickets[i][j] = new Ticket(null, this, 1 + i, 1 + j, priceDependsTime, false );
            }
        }
        return tickets;
    }

    public int getId() {
        return id;
    }

    public Film getFilm() {
        return film;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public LocalTime getEventTime() {
        return eventTime;
    }

    public Ticket[][] getTickets() {
        return tickets;
    }


    private float createPriceDependOnTime(LocalTime eventTime) {
        LocalTime timePriceRise = LocalTime.of(17, 0);
        long between = Duration.between(timePriceRise, eventTime).toHours();
        float price;
        if (between < 0) {
            price = 7.5f;
        } else {
            price = 9.0f;
        }
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventDate, event.eventDate) && Objects.equals(eventTime, event.eventTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventDate, eventTime);
    }

    @Override
    public String toString() {
        return "дата - " + getEventDate() + ", время - " + getEventTime() + "   -   " + film;
    }


}
