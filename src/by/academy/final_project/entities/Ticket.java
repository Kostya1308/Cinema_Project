package by.academy.final_project.entities;

import java.text.DecimalFormat;
import java.util.Objects;

public class Ticket {

    private int ticketId;
    private Consumer consumer;
    private Event event;
    private int rawNumber;
    private int seatNumber;
    private float price;
    boolean isBought;

    public Ticket(int ticketId, Consumer consumer, Event event, int rawNumber, int seatNumber, float price, boolean isBought) {
        this.ticketId = ticketId;
        this.consumer = consumer;
        this.event = event;
        this.rawNumber = rawNumber;
        this.seatNumber = seatNumber;
        this.price = price;
        this.isBought = isBought;
    }

    public Ticket(Consumer consumer, Event event, int rawNumber, int seatNumber, float price, boolean isBought) {
        this.consumer = consumer;
        this.event = event;
        this.rawNumber = rawNumber;
        this.seatNumber = seatNumber;
        this.price = price;
        this.isBought = isBought;
    }

    public int getTicketId() {
        return ticketId;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public Event getEvent() {
        return event;
    }

    public int getRawNumber() {
        return rawNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public float getPrice() {
        return price;
    }

    public boolean isBought() {
        return isBought;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return rawNumber == ticket.rawNumber && seatNumber == ticket.seatNumber && Objects.equals(event, ticket.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, rawNumber, seatNumber);
    }

    public String showAsBought() {
        if (seatNumber < 10) {
            return "_";
        } else {
            return "__";
        }
    }

    @Override
    public String toString() {
        String priceSrt = new DecimalFormat("#0.00").format(price);
        return "Билет №" + ticketId +
                ", фильм " + event.getFilm().getName() + ", дата - " + event.getEventDate() + ", время - " +
                event.getEventTime() + ", ряд - " + rawNumber + ", место - " + seatNumber + ", стоимость - " + priceSrt + " BYN.";
    }
}
