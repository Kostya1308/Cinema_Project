package by.academy.final_project.controllers.common.menus;

import by.academy.final_project.controllers.Menu;
import by.academy.final_project.services.consumer.service.ConsumerService;
import by.academy.final_project.services.event.service.EventService;
import by.academy.final_project.services.film.service.FilmsService;
import by.academy.final_project.services.readers.service.FilesReaderService;
import by.academy.final_project.services.ticket.service.TicketService;

import java.util.Scanner;

public class MainMenu extends Menu {

    public MainMenu(ConsumerService consumerService, FilmsService filmsService, EventService eventService, TicketService ticketService, FilesReaderService fileReaderService) {
        super(consumerService, filmsService, eventService, ticketService, fileReaderService);
    }

    public void start(){
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("________________Главное меню \"My Cinema\"________________");
            System.out.println("Нажмите \"1\", чтобы войти в личный кабинет пользователя.");
            System.out.println("Нажмите \"2\", чтобы зарегистрироваться.");
            System.out.println("Нажмите \"3\", чтобы посмотреть схему зала кинотеатра.");
            System.out.println("Нажмите \"4\", чтобы войти в меню \"О программе\".");
            System.out.println("Нажмите \"5\", чтобы выйти из приложения.");
            System.out.println();
            System.out.print("=>");
            String result = scanner.nextLine();
            switch (result ) {
                case "1" -> {
                    loginMenu = new LoginMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    loginMenu.login();
                }
                case "2" -> {
                    registrationMenu = new RegistrationMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    registrationMenu.signUp();
                }
                case "3" -> showSeatingPlan();
                case "4" -> fileReaderService.readProgrammeDescription();
                case "5" -> System.exit(0);
                default -> System.out.println("Введено неверное значение! Введите \"1\", \"2\", \"3\", \"4\" или \"5\".");
            }

        }
    }
}
