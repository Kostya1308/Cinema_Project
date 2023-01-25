package by.academy.final_project.repository.ticket.repository;

import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.Event;
import by.academy.final_project.entities.Ticket;
import by.academy.final_project.exceptions.*;
import by.academy.final_project.repository.consumer_repository.ConsumerRepositoryImpl;
import by.academy.final_project.repository.event_repository.EventRepositoryImpl;
import by.academy.final_project.services.consumer.service.ConsumerService;
import by.academy.final_project.services.consumer.service.ConsumerServiceImpl;
import by.academy.final_project.services.event.service.EventService;
import by.academy.final_project.services.event.service.EventServiceImpl;
import by.academy.final_project.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketRepositoryImpl implements TicketRepository {
    @Override
    public boolean create(Ticket ticket) {
        try {
            isContain(ticket);
            return false;
        } catch (NoSuchTicket e) {
            try (Connection connection = ConnectionManager.open()) {
                PreparedStatement stmt =
                        connection.prepareStatement("INSERT INTO ticket (user_id, event_id, raw_number, seat_number, price, is_bought) VALUES (?,?,?,?,?,?)");
                stmt.setInt(1, ticket.getConsumer().getConsumerId());
                stmt.setInt(2, ticket.getEvent().getId());
                stmt.setInt(3, ticket.getRawNumber());
                stmt.setInt(4, ticket.getSeatNumber());
                stmt.setFloat(5, ticket.getPrice());
                stmt.setBoolean(6, ticket.isBought());
                stmt.execute();
            } catch (SQLException | DriverNotLoaded ex) {
                System.out.println(e.getMessage());
            }
            return true;
        }
    }


    @Override
    public List<Ticket> readAll() {
        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM ticket");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id");
                String eventId = String.valueOf(resultSet.getInt("event_id"));
                int rawNumber = resultSet.getInt("raw_number");
                int seatNumber = resultSet.getInt("seat_number");
                float price = resultSet.getFloat("price");
                boolean isBought = resultSet.getBoolean("is_bought");
                ConsumerService consumerService = new ConsumerServiceImpl(new ConsumerRepositoryImpl());
                EventService eventService = new EventServiceImpl(new EventRepositoryImpl());
                Ticket ticket = new Ticket(id, consumerService.getConsumer(userId), eventService.getEvent(eventId), rawNumber, seatNumber, price, isBought);
                tickets.add(ticket);
            }
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
        }
        return tickets;
    }

    @Override
    public Ticket read(Ticket ticket) {
        return readAll().stream()
                .filter(ticket::equals)
                .findAny()
                .orElse(ticket);
    }

    @Override
    public Ticket read(String ticketIdString) {
        int ticketId = Integer.parseInt(ticketIdString);
        final Ticket[] returnTicket = {null};
        try {
            isContain(ticketIdString);
            readAll().stream()
                    .filter(ticket -> ticket.getTicketId() == ticketId)
                    .findAny().ifPresent(event -> returnTicket[0] = event);
        } catch (NoSuchTicket e) {
            e.printStackTrace();
        }
        return returnTicket[0];
    }

    @Override
    public boolean update(Ticket ticket, Ticket newTicket) {
        try {
            isContain(ticket);
            try (Connection connection = ConnectionManager.open()) {
                PreparedStatement stmt =
                        connection.prepareStatement("UPDATE ticket SET user_id=?, event_id=?, raw_number=?, seat_number=?, price=?, is_bought=? WHERE user_id=? AND event_id=? AND raw_number=? AND seat_number=? AND price=? AND is_bought=?");
                stmt.setInt(1, newTicket.getConsumer().getConsumerId());
                stmt.setInt(2, newTicket.getEvent().getId());
                stmt.setInt(3, newTicket.getRawNumber());
                stmt.setInt(4, newTicket.getSeatNumber());
                stmt.setFloat(5, newTicket.getPrice());
                stmt.setBoolean(6, newTicket.isBought());
                stmt.setInt(7, ticket.getConsumer().getConsumerId());
                stmt.setInt(8, ticket.getEvent().getId());
                stmt.setInt(9, ticket.getRawNumber());
                stmt.setInt(10, ticket.getSeatNumber());
                stmt.setFloat(11, ticket.getPrice());
                stmt.setBoolean(12, ticket.isBought());
                stmt.execute();
            } catch (SQLException | DriverNotLoaded e) {
                System.out.println(e.getMessage());
            }
            return true;

        } catch (NoSuchTicket e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean delete(Ticket ticket) {
        try {
            isContain(ticket);
            try (Connection connection = ConnectionManager.open()) {
                PreparedStatement stmt =
                        connection.prepareStatement("DELETE FROM ticket WHERE id=?");
                stmt.setInt(1, read(ticket).getTicketId());
                stmt.execute();
            } catch (SQLException | DriverNotLoaded e) {
                System.out.println(e.getMessage());
            }
            return true;
        } catch (NoSuchTicket e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void isContain(Ticket ticket) throws NoSuchTicket {
        boolean isContain = readAll().stream()
                .anyMatch(ticket::equals);
        if (!isContain) {
            throw new NoSuchTicket("Билет не найден.");
        }
    }

    @Override
    public void isContain(String ticketId) throws NoSuchTicket {
        Integer integer = Integer.valueOf(ticketId);
        boolean isContain = readAll().stream()
                .map(Ticket::getTicketId)
                .anyMatch(integer::equals);
        if (!isContain) {
            throw new NoSuchTicket("Билет не найден.");
        }
    }

    @Override
    public List<Ticket> readConsumersTickets(int userId) {
        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM ticket WHERE user_id=?");
            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userIdReaded = resultSet.getInt("user_id");
                String eventId = String.valueOf(resultSet.getInt("event_id"));
                int rawNumber = resultSet.getInt("raw_number");
                int seatNumber = resultSet.getInt("seat_number");
                float price = resultSet.getFloat("price");
                boolean isBought = resultSet.getBoolean("is_bought");
                ConsumerService consumerService = new ConsumerServiceImpl(new ConsumerRepositoryImpl());
                EventService eventService = new EventServiceImpl(new EventRepositoryImpl());
                Ticket ticket = new Ticket(id, consumerService.getConsumer(userIdReaded), eventService.getEvent(eventId), rawNumber, seatNumber, price, isBought);
                tickets.add(ticket);
            }
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
        }
        return tickets;
    }


    @Override
    public List<Ticket> readTicketsOnEvent(Event event) {
        return readAll().stream()
                .filter(ticket -> ticket.getEvent().equals(event))
                .toList();
    }

    @Override
    public boolean isConsumerHasTicket(String ticketIdString, List<Ticket> ticketList) {
        Integer ticketId = Integer.valueOf(ticketIdString);
        return ticketList.stream()
                .map(Ticket::getTicketId)
                .anyMatch(ticketId::equals);
    }

    @Override
    public void isSuchSeat(int rawNumber, int seatNumber) throws NoSuchSeat {
        if ((0 >= rawNumber) || (rawNumber > 10) || (0 >= seatNumber) || (seatNumber > 23)) {
            throw new NoSuchSeat("Выбранного Вами места не существует.\nС расположением мест вы можете ознакомиться" +
                    " в главном меню");
        }
    }

    @Override
    public float getDiscountPrice(Event event, int rawNumber, int seatNumber, Consumer consumer) {
        int discount = consumer.getDiscount();
        String discountString = String.valueOf(discount);
        float discountFloat = Float.parseFloat(discountString);
        return (float) ((event.getTickets()[rawNumber - 1][seatNumber - 1].getPrice()) * ((100.0 - discountFloat) / 100.0));
    }

    @Override
    public void isSeatFree(Ticket ticket) throws SeatIsOccupied {
        boolean isSeatFree = readAll().stream()
                .anyMatch(ticket::equals);
        if (isSeatFree) {
            throw new SeatIsOccupied("Место занято. С расположением свободных мест Вы можете ознакомиться в личном кабинете");
        }
    }

    @Override
    public void showEventTickets(Event event) {
        Ticket[][] tickets = event.getTickets();
        List<Ticket> tickets1 = readTicketsOnEvent(event);
        for (int i = 0; i < tickets.length; i++) {
            for (int j = 0; j < tickets[i].length; j++) {
                int rawNumber = i;
                int seatNumber = j;
                tickets1.stream()
                        .filter(ticket1 -> ticket1.getRawNumber() == (1 + rawNumber))
                        .filter(ticket1 -> ticket1.getSeatNumber() == (1 + seatNumber))
                        .findAny()
                        .ifPresent(value -> tickets[rawNumber][seatNumber] = value);
            }
        }
        System.out.println(" _____________________________________________________________");
        for (int i = 0; i < tickets.length - 5; i++) {
            System.out.print("| ");
            for (int j = 0; j < tickets[i].length; j++) {
                if (tickets[i][j].isBought()) {
                    System.out.print(tickets[i][j].showAsBought() + " ");
                } else {
                    System.out.print(tickets[i][j].getSeatNumber() + " ");
                }
            }
            System.out.print("|   ряд №" + tickets[i][0].getRawNumber());
            System.out.println();
        }
        System.out.println(" _____________________________________________________________");
        System.out.println(" ___________________________ ПРОХОД __________________________");

        for (int i = 5; i < tickets.length; i++) {
            System.out.print("| ");
            for (int j = 0; j < tickets[i].length; j++) {
                if (tickets[i][j].isBought()) {
                    System.out.print(tickets[i][j].showAsBought() + " ");
                } else {
                    System.out.print(tickets[i][j].getSeatNumber() + " ");
                }
            }
            System.out.print("|   ряд №" + tickets[i][0].getRawNumber());
            System.out.println();
        }
        System.out.println(" _____________________________________________________________");
        System.out.println("                 _ - занято   № - свободно\n");
    }

}
