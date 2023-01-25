package by.academy.final_project.controllers.activities.menus;

import by.academy.final_project.comparators.EventTimeComparator;
import by.academy.final_project.comparators.EventsDateComparatop;
import by.academy.final_project.comparators.EventsGenreComparator;
import by.academy.final_project.controllers.Menu;
import by.academy.final_project.entities.Event;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

public class EventMenu extends Menu {

    public EventMenu(ConsumerService consumerService, FilmsService filmsService, EventService eventService, TicketService ticketService, FilesReaderService fileReaderService) {
        super(consumerService, filmsService, eventService, ticketService, fileReaderService);
    }

    public void createEvent(String login) {

        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatterRedForConsolle());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        try {
            fileHandler = new FileHandler(login + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        fileHandler.setFormatter(new MyFormatterForFile());
        logger.setUseParentHandlers(false);
        logger.addHandler(fileHandler);

        while (true) {
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
                eventService.isFuture(event.getEventDate(), event.getEventTime());
            } catch (EventPassed e) {
                logger.warning("Возможно создать только предстоящее мероприятие.\n");
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            try {
                eventService.isTimeFree(event);
            } catch (TimeOccupied e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            boolean isEventAdded = eventService.addEvent(event);
            if (isEventAdded) {
                consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
                logger.info("Показ фильма \"" + event.getFilm().getName() + "\" успешно записан. Дата показа - " +
                        event.getEventDate() + ", время - " + event.getEventTime());
                consoleHandler.setFormatter(new MyFormatterRedForConsolle());
                consoleHandler.close();
                fileHandler.close();
            } else {
                logger.warning("Мероприятие не записано.");

            }
            logger.removeHandler(consoleHandler);
            logger.removeHandler(fileHandler);
            break;
        }
    }

    public void updateEvent(String login) {

        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatterRedForConsolle());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        try {
            fileHandler = new FileHandler(login + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        fileHandler.setFormatter(new MyFormatterForFile());
        logger.setUseParentHandlers(false);
        logger.addHandler(fileHandler);

        while (true) {
            System.out.println("Внимание! Откорректировать возможно только дату и время мероприятия.");
            System.out.println("Требуются данные мероприятия, которое необходимо откорректировать.");
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
                eventService.isFuture(event.getEventDate(), event.getEventTime());
                eventService.isEventFound(event);
            } catch (EventPassed | NoSuchEvent e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("Требуются данные корректировки.");
            System.out.println("Введите дату сеанса в формате ГГГГ-ММ-ДД:");
            System.out.print("=>");
            String eventDateString = scanner.nextLine();
            try {
                isCorrectDate(eventDateString);
            } catch (IncorrectDateFormat e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }
            LocalDate eventDate = LocalDate.parse(eventDateString);

            System.out.println("Введите время сеанса в формате ЧЧ:ММ:");
            System.out.print("=>");
            String eventTimeString = scanner.nextLine();
            try {
                isCorrectTime(eventTimeString);
            } catch (IncorrectTimeFormat e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            LocalTime eventTime;

            try {
                eventTime = LocalTime.parse(eventTimeString);
            }
            catch (DateTimeParseException e){
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            Event newEvent = new Event(eventService.getEvent(event).getFilm(), eventDate, eventTime);

            try {
                assert false;
                eventService.isFuture(newEvent.getEventDate(), newEvent.getEventTime());
            } catch (EventPassed e) {
                logger.warning("Возможно создать только предстоящее мероприятие.\n");
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            try {
                eventService.isTimeFree(newEvent);
            } catch (TimeOccupied e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            boolean isEventAdded = eventService.updateEvent(event, newEvent);
            if (isEventAdded) {
                consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
                logger.info("Мероприятие успешно обновлено. Все реализованные билеты действительны.");
                consoleHandler.close();
                fileHandler.close();
                consoleHandler.setFormatter(new MyFormatterRedForConsolle());
            } else {
                logger.warning("Мероприятие не обновлено");
                consoleHandler.close();
                fileHandler.close();
            }
            logger.removeHandler(consoleHandler);
            logger.removeHandler(fileHandler);
            break;
        }
    }

    public void deleteEvent(String login) {
        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatterRedForConsolle());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        try {
            fileHandler = new FileHandler(login + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        fileHandler.setFormatter(new MyFormatterForFile());
        logger.setUseParentHandlers(false);
        logger.addHandler(fileHandler);

        while (true) {
            System.out.println("Внимание! При удалении мероприятия, все билеты, проданные на него, также будут недействительны.");
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

            ticketService.getTicketsOnEvent(eventService.getEvent(event)).
                    forEach(value -> ticketService.deleteTicket(value));

            boolean deleted = eventService.deleteEvent(event);
            if (!deleted) {
                logger.warning("Мероприятие не удалено.");
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
            logger.info("Мероприятие успешно удалено.");
            fileHandler.close();
            consoleHandler.close();
            logger.removeHandler(consoleHandler);
            logger.removeHandler(fileHandler);
            consoleHandler.setFormatter(new MyFormatterRedForConsolle());
            break;
        }
    }

    public void getPosterByDateTime() {
        System.out.println("Афиша предстоящих показов:");
        List<Event> allEvents = eventService.getAllEvents();
        allEvents.sort(new EventsDateComparatop().thenComparing(new EventTimeComparator()));
        eventService.showItemsByLines(allEvents);
    }

    public void getPosterByGenre() {
        System.out.println("Афиша предстоящих показов:");
        List<Event> allEvents = eventService.getAllEvents();
        allEvents.sort(new EventsGenreComparator());
        eventService.showItemsByLines(allEvents);
    }
}





