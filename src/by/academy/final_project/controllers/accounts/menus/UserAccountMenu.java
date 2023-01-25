package by.academy.final_project.controllers.accounts.menus;

import by.academy.final_project.controllers.Menu;
import by.academy.final_project.controllers.activities.menus.ConsumerMenu;
import by.academy.final_project.controllers.activities.menus.EventMenu;
import by.academy.final_project.controllers.activities.menus.TicketMenu;
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
import java.util.logging.*;

public class UserAccountMenu extends Menu {

    public UserAccountMenu(ConsumerService consumerService, FilmsService filmsService, EventService eventService, TicketService ticketService, FilesReaderService fileReaderService) {
        super(consumerService, filmsService, eventService, ticketService, fileReaderService);
    }


    public void userAccount(String login) {

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

            Scanner scanner = new Scanner(System.in);
            System.out.println();
            System.out.println("______________________________Ваш личный кабинет_____________________________");
            System.out.println("Нажмите \"1\", чтобы просмотреть афишу предстоящих показов.");
            System.out.println("Нажмите \"2\", чтобы приобрести билет.");
            System.out.println("Нажмите \"3\", чтобы сдать купленный ранее билет.");
            System.out.println("Нажмите \"4\", чтобы просмотреть наличие билетов на интересующее мероприятие.");
            System.out.println("Нажмите \"5\", чтобы просмотреть Ваши билеты.");
            System.out.println("Нажмите \"6\", чтобы изменить личные данные.");
            System.out.println("Нажмите \"7\", чтобы выйти из аккаунта.");
            System.out.println("Нажмите \"8\", чтобы выйти из приложения.");
            System.out.println("_____________________________________________________________________________");
            System.out.print("=>");
            String result = scanner.nextLine();
            switch (result) {
                case "1" -> {
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
                case "2" -> {
                    logger.info("Открыто меню покупки билетов;");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    ticketMenu = new TicketMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    ticketMenu.buyTicket(login);
                }
                case "3" -> {
                    logger.info("Открыто меню возврата билетов;");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    ticketMenu = new TicketMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    ticketMenu.refundTicket(login);
                }
                case "4" -> {
                    logger.info("Открыто меню просмотра купленных билетов на мероприятие;");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    ticketMenu = new TicketMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    ticketMenu.getSomeEventTickets(login);
                }
                case "5" -> {
                    logger.info("Открыто меню просмотра своих билетов;");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    ticketMenu = new TicketMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    ticketMenu.getSomeConsumerTickets(consumerService.getConsumer(login).getConsumerId());
                }
                case "6" -> {
                    logger.info("Открыто меню корректировки личных данных;");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    consumerMenu = new ConsumerMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    consumerMenu.updateConsumer(login, login);
                }
                case "7" -> {
                    logger.info("Пользователь " + login + " вышел из личного кабинета;");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    mainMenu = new MainMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    mainMenu.start();
                }
                case "8" -> {
                    logger.info("Пользователь " + login + " закончил работу с приложением;");
                    logger.removeHandler(fileHandler);
                    fileHandler.close();
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
