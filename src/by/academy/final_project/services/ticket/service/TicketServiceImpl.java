package by.academy.final_project.services.ticket.service;

import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.Event;
import by.academy.final_project.entities.Ticket;
import by.academy.final_project.exceptions.NoSuchSeat;
import by.academy.final_project.exceptions.SeatIsOccupied;
import by.academy.final_project.repository.ticket.repository.TicketRepository;

import java.util.List;

public class TicketServiceImpl implements TicketService {

    TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public boolean addTicket(Ticket ticket) {
        return ticketRepository.create(ticket);
    }

    @Override
    public Ticket getTicket(String ticketId) {
        return ticketRepository.read(ticketId);
    }

    @Override
    public boolean deleteTicket(Ticket ticket) {
        return ticketRepository.delete(ticket);
    }

    @Override
    public List<Ticket> getConsumersTickets(int id) {
        return ticketRepository.readConsumersTickets(id);
    }

    @Override
    public boolean isConsumerHasTicket(String ticketIdString, List<Ticket> ticketList) {
        return ticketRepository.isConsumerHasTicket(ticketIdString, ticketList);
    }

    @Override
    public void isSuchSeat(int rawNumber, int seatNumber) throws NoSuchSeat {
        ticketRepository.isSuchSeat (rawNumber, seatNumber);
    }

    @Override
    public List<Ticket> getTicketsOnEvent(Event event) {
        return ticketRepository.readTicketsOnEvent(event);
    }

    @Override
    public float getDiscountPrice(Event event, int rawNumber, int seatNumber, Consumer consumer) {
        return ticketRepository.getDiscountPrice(event, rawNumber, seatNumber, consumer);
    }

    @Override
    public void isSeatFree(Ticket ticket) throws SeatIsOccupied {
        ticketRepository.isSeatFree(ticket);
    }

    @Override
    public void showEventTickets(Event event) {
        ticketRepository.showEventTickets(event);
    }
}
