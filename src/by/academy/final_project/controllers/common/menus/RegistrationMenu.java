package by.academy.final_project.controllers.common.menus;

import by.academy.final_project.controllers.Menu;
import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.ConsumersRole;
import by.academy.final_project.exceptions.IncorrectDateFormat;
import by.academy.final_project.exceptions.IncorrectLoginFormat;
import by.academy.final_project.exceptions.IncorrectPasswordFormat;
import by.academy.final_project.exceptions.NoSuchLogin;
import by.academy.final_project.formatters.MyFormatterForFile;
import by.academy.final_project.formatters.MyFormatterRedForConsolle;
import by.academy.final_project.formatters.MyFormatterGreenForConsolle;
import by.academy.final_project.services.consumer.service.ConsumerService;
import by.academy.final_project.services.event.service.EventService;
import by.academy.final_project.services.film.service.FilmsService;
import by.academy.final_project.services.readers.service.FilesReaderService;
import by.academy.final_project.services.ticket.service.TicketService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

public class RegistrationMenu extends Menu {

    public RegistrationMenu(ConsumerService consumerService, FilmsService filmsService, EventService eventService, TicketService ticketService, FilesReaderService fileReaderService) {
        super(consumerService, filmsService, eventService, ticketService, fileReaderService);
    }

    public void signUp() {
        while (true) {
            System.out.println("Меню регистрации нового пользователя");
            System.out.println("Введите имя пользователя:");
            System.out.print("=>");
            String login;

            consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new MyFormatterRedForConsolle());
            logger.addHandler(consoleHandler);
            logger.setUseParentHandlers(false);
            try {
                login = enterLogin();
            } catch (IncorrectLoginFormat e) {
                logger.warning(e.getMessage());
                logger.removeHandler(consoleHandler);
                return;
            }
            try {
                consumerService.isSuchLoginExist(login);
                logger.warning("Имя " + login + " занято, выберите другое.");
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            } catch (NoSuchLogin ep) {
                System.out.println("Придумайте пароль, используйте не менее 8-ми и не более 16-ти символов:");
                System.out.print("=>");
                String password;
                try {
                    password = enterPassword();
                } catch (IncorrectPasswordFormat epx) {
                    logger.warning(epx.getMessage());
                    logger.removeHandler(consoleHandler);
                    logger.removeHandler(fileHandler);
                    return;
                }

                System.out.println("Введите дату рождения в формате ГГГГ-ММ-ДД:");
                System.out.print("=>");
                String dateOfBirthString;
                try {
                    dateOfBirthString = enterDate();
                } catch (IncorrectDateFormat e1) {
                    logger.warning(e1.getMessage());
                    logger.removeHandler(consoleHandler);
                    logger.removeHandler(fileHandler);
                    break;
                }
                LocalDate dateOfBirth;
                try {
                    dateOfBirth = LocalDate.parse(dateOfBirthString);
                } catch (DateTimeParseException exception) {
                    logger.warning(exception.getMessage());
                    logger.removeHandler(consoleHandler);
                    logger.removeHandler(fileHandler);
                    break;
                }

                Consumer consumer = new Consumer(login, password, ConsumersRole.USER, dateOfBirth, 0);

                try {
                    consumerService.addConsumer(consumer);
                    fileHandler = new FileHandler(login + ".txt");
                    fileHandler.setFormatter(new MyFormatterForFile());
                    logger.addHandler(fileHandler);
                    logger.setUseParentHandlers(false);
                    consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
                    logger.info("Пользователь " + login + " успешно зарегистрирован.");
                    consoleHandler.setFormatter(new MyFormatterRedForConsolle());
                    fileHandler.close();
                    logger.removeHandler(consoleHandler);
                    logger.removeHandler(fileHandler);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                    consoleHandler.setFormatter(new MyFormatterRedForConsolle());
                    logger.warning(e.getMessage());
                    logger.removeHandler(consoleHandler);
                    logger.removeHandler(fileHandler);
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}

