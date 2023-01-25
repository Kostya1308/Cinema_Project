package by.academy.final_project.repository.ticket.repository;

import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.Event;
import by.academy.final_project.entities.Ticket;
import by.academy.final_project.exceptions.NoSuchSeat;
import by.academy.final_project.exceptions.NoSuchTicket;
import by.academy.final_project.exceptions.SeatIsOccupied;
import by.academy.final_project.repository.Crud;

import java.util.List;

public interface TicketRepository extends Crud<Ticket> {

    void isContain (Ticket ticket) throws NoSuchTicket;

    void isContain (String ticketId) throws NoSuchTicket;

    List<Ticket> readConsumersTickets(int id);

    boolean isConsumerHasTicket(String ticketIdString, List<Ticket> ticketList);

    List<Ticket> readTicketsOnEvent(Event event);

    void isSuchSeat (int rawNumber, int seatNumber) throws NoSuchSeat;

    float getDiscountPrice(Event event, int rawNumber, int seatNumber, Consumer consumer);

    void isSeatFree(Ticket ticket) throws SeatIsOccupied;

    void showEventTickets(Event event);

}
