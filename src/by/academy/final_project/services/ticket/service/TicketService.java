package by.academy.final_project.services.ticket.service;

import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.Event;
import by.academy.final_project.entities.Ticket;
import by.academy.final_project.exceptions.NoSuchSeat;
import by.academy.final_project.exceptions.NoSuchTicket;
import by.academy.final_project.exceptions.SeatIsOccupied;
import by.academy.final_project.services.Service;

import java.util.List;

public interface TicketService extends Service<Ticket> {

    boolean addTicket(Ticket ticket);
    Ticket getTicket(String ticketId);
    boolean deleteTicket(Ticket ticket);
    List<Ticket> getConsumersTickets(int id);
    boolean isConsumerHasTicket(String ticketIdString, List<Ticket> ticketList);
    List<Ticket> getTicketsOnEvent(Event event);
    void isSuchSeat (int rawNumber, int seatNumber) throws NoSuchSeat;
    float getDiscountPrice (Event event, int rawNumber, int seatNumber, Consumer consumer);
    void isSeatFree(Ticket ticket) throws SeatIsOccupied;
    void showEventTickets(Event event);


}
