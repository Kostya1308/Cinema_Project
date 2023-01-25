package by.academy.final_project.controllers.activities.menus;

import by.academy.final_project.controllers.Menu;
import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.Event;
import by.academy.final_project.entities.Ticket;
import by.academy.final_project.exceptions.*;
import by.academy.final_project.formatters.MyFormatterForFile;
import by.academy.final_project.formatters.MyFormatterRedForConsolle;
import by.academy.final_project.formatters.MyFormatterGreenForConsolle;
import by.academy.final_project.services.consumer.service.ConsumerService;
import by.academy.final_project.services.event.service.EventService;
import by.academy.final_project.services.film.service.FilmsService;
import by.academy.final_project.services.readers.service.FilesReaderService;
import by.academy.final_project.services.ticket.service.TicketService;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.*;

public class TicketMenu extends Menu {

    public TicketMenu(ConsumerService consumerService, FilmsService filmsService, EventService eventService, TicketService ticketService, FilesReaderService fileReaderService) {
        super(consumerService, filmsService, eventService, ticketService, fileReaderService);
    }

    public void buyTicket(String login) {

        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatterRedForConsolle());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        try {
            fileHandler = new FileHandler(login + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileHandler.setFormatter(new MyFormatterForFile());
        logger.setUseParentHandlers(false);
        logger.addHandler(fileHandler);
        while (true) {

            Consumer consumer = consumerService.getConsumer(login);
            try {
                consumerService.isSuchLoginExist(login);
            } catch (NoSuchLogin e) {
                System.out.println(e.getMessage() + "\n");
                break;
            }
            Event event;
            try {
                event = enterEvent();
            } catch (NoSuchFilm | IncorrectTimeFormat | IncorrectDateFormat | DateTimeParseException e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            try {
                eventService.isEventFound(event);
            } catch (NoSuchEvent e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            try {
                filmsService.isAgeLimit(event.getFilm().getName(), consumer);
            } catch (AgeLimit e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            LocalDate eventDate = event.getEventDate();
            LocalTime eventTime = event.getEventTime();

            try {
                eventService.isFuture(eventDate, eventTime);
            } catch (EventPassed e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите номер ряда:");
            System.out.print("=>");
            String rawString = scanner.nextLine();
            System.out.println("Введите номер места:");
            System.out.print("=>");
            String seatString = scanner.nextLine();
            try {
                isCorrectRawOrSeat(rawString);
                isCorrectRawOrSeat(seatString);
            } catch (IncorrectFormatSeat e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            int rawNumber = Integer.parseInt(rawString);
            int seatNumber = Integer.parseInt(seatString);

            try {
                ticketService.isSuchSeat(rawNumber, seatNumber);
            } catch (NoSuchSeat e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            float price = ticketService.getDiscountPrice(event, rawNumber, seatNumber, consumer);
            Ticket ticket = new Ticket(consumer, eventService.getEvent(event), rawNumber, seatNumber, price, true);
            try {
                ticketService.isSeatFree(ticket);
            } catch (SeatIsOccupied e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            boolean isDiscountNew = consumerService.UpDiscountValue(consumer);
            if (isDiscountNew) {
                consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
                logger.info("Персональная скидка постоянного покупателя увеличена до 10%!");
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
            }

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formattedPrice = new DecimalFormat("#0.00").format(price);

            if (!ticketService.addTicket(ticket)) {
                return;
            }

            logger.info("Билет приобретён. Стоимость: " + formattedPrice + " BYN.\n" +
                    "Фильм - " + event.getFilm().getName() + ", начало показа " + eventDate.format(dateTimeFormatter) +
                    " в " + eventTime + ".");
            fileHandler.close();
            consoleHandler.setFormatter(new MyFormatterRedForConsolle());
            logger.removeHandler(consoleHandler);
            logger.removeHandler(fileHandler);
            return;
        }
    }

    public void refundTicket(String login) {
        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatterRedForConsolle());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        try {
            fileHandler = new FileHandler(login + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileHandler.setFormatter(new MyFormatterForFile());
        logger.setUseParentHandlers(false);
        logger.addHandler(fileHandler);

        while (true) {
            try {
                consumerService.isSuchLoginExist(login);
            } catch (NoSuchLogin e) {
                logger.warning(e.getMessage());
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            if (ticketService.getConsumersTickets(consumerService.getConsumer(login).getConsumerId()).isEmpty()) {
                logger.warning("У Вас пока нет приобретенных билетов.");
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            System.out.println("Введите номер билета, возврат которого необходимо произвести:\n");
            List<Ticket> consumersTickets = ticketService.getConsumersTickets(consumerService.getConsumer(login).getConsumerId());
            ticketService.showItemsByLines(consumersTickets);
            Scanner scanner = new Scanner(System.in);
            System.out.print("=>");
            String ticketIdString = scanner.nextLine();

            try {
                isCorrectId(ticketIdString);
            } catch (IncorrectIdFormat e) {
                logger.warning(e.getMessage());
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }
            boolean isConsumerHasTicket = ticketService.isConsumerHasTicket(ticketIdString, consumersTickets);
            if (!isConsumerHasTicket) {
                logger.warning("Билет №" + ticketIdString + " не найден.\nСписок билетов пользователя можно просмотреть в личном кабинете");
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            Ticket ticket = ticketService.getTicket(ticketIdString);

            try {
                eventService.isFuture(ticket.getEvent().getEventDate(), ticket.getEvent().getEventTime());
            } catch (EventPassed e) {
                logger.warning(e.getMessage());
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            try {
                eventService.isPassHours(ticket.getEvent().getEventDate(), ticket.getEvent().getEventTime());
            } catch (EventPassed e) {
                logger.warning(e.getMessage());
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            boolean isDiscountNew = consumerService.LowDiscountValue(consumerService.getConsumer(login));
            if (isDiscountNew) {
                logger.info("Персональная скидка постоянного покупателя снижена до 0%");
                fileHandler.close();
                consoleHandler.close();
            }

            boolean isDeleted = ticketService.deleteTicket(ticket);
            if (isDeleted) {
                consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
                logger.info("Билет №" + ticketIdString + " успешно возвращен.\n");
                consoleHandler.setFormatter(new MyFormatterRedForConsolle());
                fileHandler.close();
            } else {
                logger.info("Билет не возвращен.");
                fileHandler.close();
            }
            logger.removeHandler(consoleHandler);
            logger.removeHandler(fileHandler);
            break;
        }
    }

    public void getSomeEventTickets(String login) {
        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatterRedForConsolle());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        try {
            fileHandler = new FileHandler(login + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileHandler.setFormatter(new MyFormatterForFile());
        logger.setUseParentHandlers(false);
        logger.addHandler(fileHandler);

        while (true) {
            Event event;
            try {
                event = enterEvent();
            } catch (NoSuchFilm | IncorrectTimeFormat | IncorrectDateFormat e) {
                logger.info(e.getMessage());
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }
            try {
                eventService.isEventFound(String.valueOf(eventService.getEvent(event).getId()));
            } catch (NoSuchEvent e) {
                logger.info(e.getMessage());
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }
            ticketService.showEventTickets(event);
            System.out.println();
            break;
        }
    }

    public void getSomeConsumerTickets(int id) {
        System.out.println("Ваши билеты :");
        ticketService.showItemsByLines(ticketService.getConsumersTickets(id));
        System.out.println();
    }
}
