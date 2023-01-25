package by.academy.final_project.controllers.accounts.menus;

import by.academy.final_project.controllers.*;
import by.academy.final_project.controllers.activities.menus.ConsumerMenu;
import by.academy.final_project.controllers.activities.menus.EventMenu;
import by.academy.final_project.controllers.activities.menus.FilmMenu;
import by.academy.final_project.controllers.activities.menus.TicketMenu;
import by.academy.final_project.controllers.common.menus.MainMenu;
import by.academy.final_project.controllers.common.menus.RegistrationMenu;
import by.academy.final_project.formatters.MyFormatterForFile;
import by.academy.final_project.formatters.MyFormatterRedForConsolle;
import by.academy.final_project.services.consumer.service.ConsumerService;
import by.academy.final_project.services.event.service.EventService;
import by.academy.final_project.services.film.service.FilmsService;
import by.academy.final_project.services.readers.service.FilesReaderService;
import by.academy.final_project.services.ticket.service.TicketService;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

public class AdminAccountMenu extends Menu {

    public AdminAccountMenu(ConsumerService consumerService, FilmsService filmsService, EventService eventService, TicketService ticketService, FilesReaderService fileReaderService) {
        super(consumerService, filmsService, eventService, ticketService, fileReaderService);
    }


    public void adminAccount(String login) {

        System.out.println("Здравствуйте, " + login + "!");
        try {
            fileHandler = new FileHandler(login + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fileHandler);
        fileHandler.setFormatter(new MyFormatterForFile());
        logger.setUseParentHandlers(false);

        while (true) {

            System.out.println();
            System.out.println("______________________________Ваш личный кабинет______________________________");
            System.out.println("Нажмите \"1\", чтобы создать новый фильм.");
            System.out.println("Нажмите \"2\", чтобы создать новое мероприятие.");
            System.out.println("Нажмите \"3\", чтобы создать нового пользователя.");
            System.out.println("Нажмите \"4\", чтобы отредактировать фильм.");
            System.out.println("Нажмите \"5\", чтобы отредактировать мероприятие.");
            System.out.println("Нажмите \"6\", чтобы отредактировать пользователя.");
            System.out.println("Нажмите \"7\", чтобы удалить фильм.");
            System.out.println("Нажмите \"8\", чтобы удалить мероприятие.");
            System.out.println("Нажмите \"9\", чтобы удалить пользователя.");
            System.out.println("Нажмите \"10\", чтобы изменить личные данные.");
            System.out.println("Нажмите \"11\", чтобы просмотреть наличие билетов на интересующее мероприятие.");
            System.out.println("Нажмите \"12\", чтобы просмотреть афишу предстоящих показов.");
            System.out.println("Нажмите \"13\", чтобы выйти из аккаунта.");
            System.out.println("Нажмите \"14\", чтобы выйти из приложения.");
            System.out.println("______________________________________________________________________________\n");
            System.out.print("=>");
            Scanner scanner = new Scanner(System.in);
            String result = scanner.nextLine();
            switch (result) {
                case "1" -> {
                    logger.info("Открыто меню создания фильма.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    logger.removeHandler(consoleHandler);
                    filmMenu = new FilmMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    filmMenu.createFilm(login);
                }
                case "2" -> {
                    logger.info("Открыто меню создания мероприятия.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    eventMenu = new EventMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    eventMenu.createEvent(login);
                }
                case "3" -> {
                    logger.info("Открыто меню регистрации нового пользователя.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    registrationMenu = new RegistrationMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    registrationMenu.signUp();
                }
                case "4" -> {
                    logger.info("Открыто меню корректировки фильма.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    filmMenu = new FilmMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    filmMenu.updateFilm(login);
                }
                case "5" -> {
                    logger.info("Открыто меню корректировки мероприятия.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    eventMenu = new EventMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    eventMenu.updateEvent(login);
                }
                case "6" -> {
                    logger.info("Открыто меню корректировки данных пользователя.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    System.out.println("Введите имя пользователя, данные которого необходимо откорректировать:");
                    System.out.print("=>");
                    String loginUser = scanner.nextLine();
                    consumerMenu = new ConsumerMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    consumerMenu.updateConsumer(loginUser, login);
                }
                case "7" -> {
                    logger.info("Открыто меню удаления фильма.");
                    fileHandler.close();
                    logger.removeHandler(consoleHandler);
                    logger.removeHandler(fileHandler);
                    filmMenu = new FilmMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    filmMenu.deleteFilm(login);
                }
                case "8" -> {
                    logger.info("Открыто меню удаления мероприятия.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    eventMenu = new EventMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    eventMenu.deleteEvent(login);
                }
                case "9" -> {
                    logger.info("Открыто меню удаления пользователя.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    consumerMenu = new ConsumerMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    consumerMenu.deleteConsumer();
                }
                case "10" -> {
                    logger.info("Открыто меню корректировки личных данных пользователя.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    consumerMenu = new ConsumerMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    consumerMenu.updateConsumer(login, login);
                }
                case "11" -> {
                    logger.info("Открыто меню просмотра купленных билетов на мероприятие;");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    ticketMenu = new TicketMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    ticketMenu.getSomeEventTickets(login);
                }

                case "12" -> {
                    logger.info("Открыто меню просмотра афиши;");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    System.out.println("Нажмите \"1\", чтобы сортировать афишу по дате.");
                    System.out.println("Нажмите \"2\", чтобы сортировать афишу по жанру.");
                    System.out.print("=>");
                    String result2 = scanner.nextLine();
                    switch (result2) {
                        case "1" -> {
                            eventMenu = new EventMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                            eventMenu.getPosterByDateTime();
                        }
                        case "2" -> {
                            eventMenu = new EventMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                            eventMenu.getPosterByGenre();
                        }
                        default -> System.out.println("Введено неверное значение! Введите \"1\" или \"2\".\n");
                    }
                }
                case "13" -> {
                    logger.info("Пользователь " + login + " вышел из личного кабинета;");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    mainMenu = new MainMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    mainMenu.start();
                }
                case "14" -> {
                    logger.info("Пользователь " + login + " закончил работу с приложением;");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);

                    System.exit(0);
                }
                default -> {
                    ConsoleHandler consoleHandler = new ConsoleHandler();
                    logger.addHandler(consoleHandler);
                    consoleHandler.setFormatter(new MyFormatterRedForConsolle());
                    logger.warning("Введено неверное значение.");
                    logger.removeHandler(consoleHandler);
                    logger.removeHandler(fileHandler);
                }

            }
        }
    }
}
