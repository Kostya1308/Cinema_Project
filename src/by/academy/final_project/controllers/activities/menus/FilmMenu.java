package by.academy.final_project.controllers.activities.menus;

import by.academy.final_project.controllers.Menu;
import by.academy.final_project.entities.Film;
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
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

public class FilmMenu extends Menu {

    public FilmMenu(ConsumerService consumerService, FilmsService filmsService, EventService eventService, TicketService ticketService, FilesReaderService fileReaderService) {
        super(consumerService, filmsService, eventService, ticketService, fileReaderService);
    }

    public void createFilm(String login) {

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
            Film film;
            try {
                film = enterFilm();
            } catch (IncorrectYearFormat | NoSuchGenre | IncorrectIdFormat | DateTimeParseException e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }
            boolean filmAdded = filmsService.addFilm(film);
            if (filmAdded) {
                consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
                logger.info("?????????? \"" + film.getName() + "\" ?????????????? ?????????????? ?? ???????? ????????????.");
                fileHandler.close();
                consoleHandler.close();
                consoleHandler.setFormatter(new MyFormatterRedForConsolle());
            } else {
                logger.info("?????????? \"" + film.getName() + "\" ???? ?????????????? ?? ???????? ????????????.");
                fileHandler.close();
            }
            logger.removeHandler(consoleHandler);
            logger.removeHandler(fileHandler);
            break;
        }
    }

    public void updateFilm(String login) {
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
            System.out.println("?????????????? ???????????????? ????????????, ?????????????? ???????????????????? ????????????????????????????????:");
            System.out.print("=>");
            Scanner scanner = new Scanner(System.in);
            String oldFilmName = scanner.nextLine();
            try {
                filmsService.isFilmExist(oldFilmName);
            } catch (NoSuchFilm e) {
                logger.warning(e.getMessage());
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            Film oldFilm = filmsService.getFilm(oldFilmName);
            System.out.println("?????????????????? ???????????? ??????????????????????????.");
            Film newFilm;
            try {
                newFilm = enterFilm();
            } catch (IncorrectYearFormat | NoSuchGenre | IncorrectIdFormat | DateTimeParseException e) {
                logger.warning(e.getMessage());
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            boolean isFilmUpdated = filmsService.updateFilm(oldFilm, newFilm);
            if (isFilmUpdated) {
                consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
                logger.info("?????????? \"" + newFilm.getName() + "\" ?????????????? ????????????????.");
                fileHandler.close();
                consoleHandler.setFormatter(new MyFormatterRedForConsolle());
            } else {
                logger.info("?????????? \"" + newFilm.getName() + "\" ???? ????????????????.");
                fileHandler.close();
            }
            logger.removeHandler(consoleHandler);
            logger.removeHandler(fileHandler);
            break;
        }
    }

    public void deleteFilm(String login) {
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
            System.out.println("?????????????? ???????????????? ????????????, ?????????????? ???????????????????? ??????????????:");
            System.out.print("=>");
            Scanner scanner = new Scanner(System.in);
            String oldFilmName = scanner.nextLine();
            try {
                filmsService.isFilmExist(oldFilmName);
            } catch (NoSuchFilm e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            boolean isFuture = eventService.getAllEvents().stream()
                    .anyMatch(value -> value.getFilm().getName().equals(oldFilmName));
            if (isFuture) {
                logger.warning("???????????????????? ??????????????, ?????? ?????? ?? ???????????? ?????????????? ?????????????????????????? ?????????????????????? ????????????.");
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            boolean deleted = filmsService.deleteFilm(filmsService.getFilm(oldFilmName));
            if (deleted) {
                consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
                logger.info("?????????? \"" + oldFilmName + "\" ?????????????? ????????????.");
                fileHandler.close();
                consoleHandler.close();
                consoleHandler.setFormatter(new MyFormatterRedForConsolle());
            } else {
                logger.info("?????????? \"" + oldFilmName + "\" ???? ????????????.");
                fileHandler.close();
                consoleHandler.close();
            }
            logger.removeHandler(consoleHandler);
            logger.removeHandler(fileHandler);
            break;
        }
    }
}
