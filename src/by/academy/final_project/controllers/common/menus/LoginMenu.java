package by.academy.final_project.controllers.common.menus;

import by.academy.final_project.controllers.accounts.menus.ManagerAccountMenu;
import by.academy.final_project.controllers.Menu;
import by.academy.final_project.controllers.accounts.menus.UserAccountMenu;
import by.academy.final_project.controllers.accounts.menus.AdminAccountMenu;
import by.academy.final_project.entities.ConsumersRole;
import by.academy.final_project.exceptions.NoSuchLogin;
import by.academy.final_project.exceptions.PasswordIsNotEquals;
import by.academy.final_project.formatters.MyFormatterForFile;
import by.academy.final_project.formatters.MyFormatterRedForConsolle;
import by.academy.final_project.services.consumer.service.ConsumerService;
import by.academy.final_project.services.event.service.EventService;
import by.academy.final_project.services.film.service.FilmsService;
import by.academy.final_project.services.readers.service.FilesReaderService;
import by.academy.final_project.services.ticket.service.TicketService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

public class LoginMenu extends Menu {
    public LoginMenu(ConsumerService consumerService, FilmsService filmsService, EventService eventService, TicketService ticketService, FilesReaderService fileReaderService) {
        super(consumerService, filmsService, eventService, ticketService, fileReaderService);
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);
        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatterRedForConsolle());
        logger.addHandler(consoleHandler);
        logger.setUseParentHandlers(false);

        while (true) {
            System.out.println("Меню входа в личный кабинет");
            System.out.println("Введите имя пользователя:");
            System.out.print("=>");
            String login = scanner.nextLine();

            System.out.println("Введите пароль:");
            System.out.print("=>");
            String password = scanner.nextLine();

            try {
                consumerService.isSuchLoginExist(login);
                consumerService.isChecked(password, login);

            }  catch (NoSuchLogin | PasswordIsNotEquals e) {
                logger.warning("Неверные логин или пароль. Если Вы забыли пароль, обратитесь к администратору.");
                logger.removeHandler(consoleHandler);
                System.out.println();
                System.out.println("Введите \"1\", чтобы попробовать ввести логин и пароль заново.");
                System.out.println("Введите \"2\", чтобы перейти в меню регистрации нового пользователя.");
                System.out.println("Введите \"3\", чтобы выйти в главное меню.");
                System.out.println("Введите \"4\", чтобы выйти из программы.");
                System.out.print("=>");
                String result = scanner.nextLine();
                switch (result) {
                    case "1" -> login();
                    case "2" -> {
                        registrationMenu = new RegistrationMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                        registrationMenu.signUp();
                    }
                    case "3" -> {
                        mainMenu = new MainMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                        mainMenu.start();
                    }
                    case "4" -> System.exit(0);
                    default ->
                        System.out.println("Введено неверное значение! Введите \"1\", \"2\", \"3\" или \"4\".\n");
                }
                break;
            }
            catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                logger.warning(e.getMessage());

            }
            try {
                fileHandler = new FileHandler(login + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.addHandler(fileHandler);
            fileHandler.setFormatter(new MyFormatterForFile());
            logger.setUseParentHandlers(false);
            logger.removeHandler(consoleHandler);
            logger.info("Пользователь " + login + " зашёл в личный кабинет.");
            fileHandler.close();
            consoleHandler.close();
            logger.removeHandler(consoleHandler);
            logger.removeHandler(fileHandler);
            ConsumersRole role = consumerService.getConsumer(login).getRole();
            switch (role) {
                case USER -> {
                    userAccountMenu = new UserAccountMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    userAccountMenu.userAccount(login);
                }
                case MANAGER -> {
                    managerAccountMenu = new ManagerAccountMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    managerAccountMenu.managerAccount(login);
                }
                case ADMIN -> {
                    adminAccountMenu = new AdminAccountMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);
                    adminAccountMenu.adminAccount(login);
                }
            }
            break;
        }
    }
}
