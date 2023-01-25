package by.academy.final_project.controllers.accounts.menus;

import by.academy.final_project.controllers.Menu;
import by.academy.final_project.controllers.activities.menus.ConsumerMenu;
import by.academy.final_project.controllers.activities.menus.TicketMenu;
import by.academy.final_project.controllers.activities.menus.EventMenu;
import by.academy.final_project.controllers.activities.menus.FilmMenu;
import by.academy.final_project.controllers.common.menus.MainMenu;
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

public class ManagerAccountMenu extends Menu {

    public ManagerAccountMenu(ConsumerService consumerService, FilmsService filmsService, EventService eventService, TicketService ticketService, FilesReaderService fileReaderService) {
        super(consumerService, filmsService, eventService, ticketService, fileReaderService);
    }

    public void managerAccount(String login) {

        System.out.println("Здравствуйте, " + login + "!");

        while (true) {
            try {
                fileHandler = new FileHandler(login + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.addHandler(fileHandler);
            fileHandler.setFormatter(new MyFormatterForFile());
            logger.setUseParentHandlers(false);
            System.out.println();
            System.out.println("______________________________Ваш личный кабинет_____________________________");
            System.out.println("Нажмите \"1\", чтобы отредактировать фильм.");
            System.out.println("Нажмите \"2\", чтобы отредактировать мероприятие.");
            System.out.println("Нажмите \"3\", чтобы приобрести билет на имя определенного пользователя.");
            System.out.println("Нажмите \"4\", чтобы вернуть билет определенного пользователя.");
            System.out.println("Нажмите \"5\", чтобы изменить личные данные.");
            System.out.println("Нажмите \"6\", чтобы просмотреть наличие билетов на интересующее мероприятие.");
            System.out.println("Нажмите \"7\", чтобы просмотреть афишу предстоящих показов.");
            System.out.println("Нажмите \"8\", чтобы выйти из аккаунта.");
            System.out.println("Нажмите \"9\", чтобы выйти из приложения.");
            System.out.println("_____________________________________________________________________________");
            System.out.print("=>");
            Scanner scanner = new Scanner(System.in);
            String result = scanner.nextLine();
            switch (result) {
                case "1" -> {
                    logger.info("Открыто меню корректировки фильма.");
                    fileHandler.close();
                    filmMenu = new FilmMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    filmMenu.updateFilm(login);
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                }
                case "2" -> {
                    logger.info("Открыто меню корректировки мероприятия.");
                    fileHandler.close();
                    eventMenu = new EventMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    eventMenu.updateEvent(login);
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                }
                case "3" -> {
                    logger.info("Открыто меню покупки билета на имя пользователя.");
                    fileHandler.close();
                    System.out.println("Введите имя пользователя, на имя которого необходимо приобрести билет:");
                    System.out.print("=>");
                    String userLogin = scanner.nextLine();
                    ticketMenu = new TicketMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    ticketMenu.buyTicket(userLogin);
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                }
                case "4" -> {
                    logger.info("Открыто меню возврата билета на имя пользователя.");
                    fileHandler.close();
                    System.out.println("Введите имя пользователя, билет которого необходимо вернуть:");
                    System.out.print("=>");
                    String userLogin = scanner.nextLine();
                    ticketMenu = new TicketMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    ticketMenu.refundTicket(userLogin);
                    fileHandler.close();
                }
                case "5" -> {
                    logger.info("Открыто меню корректировки личных данных.");
                    fileHandler.close();
                    consumerMenu = new ConsumerMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    consumerMenu.updateConsumer(login, login);
                    fileHandler.close();
                }
                case "6" -> {
                    logger.info("Открыто меню просмотра купленных билетов на мероприятие;");
                    fileHandler.close();
                    ticketMenu = new TicketMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    ticketMenu.getSomeEventTickets(login);
                }
                case "7" -> {
                    logger.info("Открыто меню просмотра афиши;");
                    fileHandler.close();
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
                case "8" -> {
                    logger.info("Пользователь " + login + " вышел из личного кабинета;");
                    fileHandler.close();
                    mainMenu = new MainMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    mainMenu.start();
                }
                case "9" -> {
                    logger.info("Пользователь " + login + " закончил работу с приложением;");
                    fileHandler.close();
                    System.exit(0);
                }
                default -> {
                    ConsoleHandler consoleHandler = new ConsoleHandler();
                    logger.addHandler(consoleHandler);
                    consoleHandler.setFormatter(new MyFormatterRedForConsolle());
                    logger.warning("Введено неверное значение.");
                    logger.removeHandler(consoleHandler);
                }
            }
        }
    }
}
