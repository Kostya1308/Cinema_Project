package by.academy.final_project.controllers.activities.menus;

import by.academy.final_project.controllers.Menu;
import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.ConsumersRole;
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
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

public class ConsumerMenu extends Menu {

    public ConsumerMenu(ConsumerService consumerService, FilmsService filmsService, EventService eventService, TicketService ticketService, FilesReaderService fileReaderService) {
        super(consumerService, filmsService, eventService, ticketService, fileReaderService);
    }

    public void updateConsumer(String loginUser, String myLogin) {

        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatterRedForConsolle());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        try {
            fileHandler = new FileHandler(myLogin + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        fileHandler.setFormatter(new MyFormatterForFile());
        logger.setUseParentHandlers(false);
        logger.addHandler(fileHandler);

        while (true) {
            try {
                consumerService.isSuchLoginExist(loginUser);
            } catch (NoSuchLogin e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            ConsumersRole consumersRole = consumerService.getConsumer(myLogin).getRole();
            try {
                consumerService.idSuchRole(consumersRole);
            } catch (NoSuchRole e) {
                logger.warning(e.getMessage());
                fileHandler.close();
                consoleHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            Consumer oldConsumer;
            if (consumersRole.equals(ConsumersRole.ADMIN)) {
                oldConsumer = consumerService.getConsumer(loginUser);
            } else {
                oldConsumer = consumerService.getConsumer(myLogin);
            }

            System.out.println("Нажмите \"1\", если Вы хотите изменить имя пользователя.");
            System.out.println("Нажмите \"2\", если Вы хотите изменить пароль.");
            System.out.println("Нажмите \"3\", если Вы хотите изменить уровень доступа.");
            System.out.println("Нажмите \"4\", если Вы хотите изменить дату рождения");
            System.out.print("=>");
            Scanner scanner = new Scanner(System.in);
            String result = scanner.nextLine();
            switch (result) {
                case "1" -> {
                    logger.removeHandler(consoleHandler);
                    logger.info("Открыто меню корректировки имени.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    updateName(oldConsumer, myLogin);
                }
                case "2" -> {
                    logger.removeHandler(consoleHandler);
                    logger.info("Открыто меню корректировки пароля.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    updatePassword(oldConsumer, myLogin);
                }
                case "3" -> {
                    logger.removeHandler(consoleHandler);
                    logger.info("Открыто меню корректировки уровня доступа.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    updateRole(oldConsumer, myLogin);
                }
                case "4" -> {
                    logger.removeHandler(consoleHandler);
                    logger.info("Открыто меню корректировки уровня даты рождения.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    updateDate(oldConsumer, myLogin);
                }
                default -> {
                    logger.warning("Введено неверное значение.");
                    fileHandler.close();
                    consoleHandler.close();
                    logger.removeHandler(consoleHandler);
                    logger.removeHandler(fileHandler);
                }
            }
            break;
        }
    }

    private void updateName(Consumer oldConsumer, String myLogin) {
        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatterRedForConsolle());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        try {
            fileHandler = new FileHandler(myLogin + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        fileHandler.setFormatter(new MyFormatterForFile());
        logger.setUseParentHandlers(false);
        logger.addHandler(fileHandler);
        boolean updated;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите новое имя пользователя:");
            System.out.print("=>");
            String newLogin = scanner.nextLine();
            try {
                consumerService.isSuchLoginExist(newLogin);
                logger.warning("Данное имя занято.");
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            } catch (NoSuchLogin ee) {

                ConsumersRole myRole = consumerService.getConsumer(myLogin).getRole();
                if (!myRole.equals(ConsumersRole.ADMIN)) {
                    System.out.println("Введите пароль:");
                    System.out.print("=>");
                    String password = scanner.nextLine();
                    try {
                        consumerService.isChecked(password, oldConsumer.getLogin());
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException | PasswordIsNotEquals e) {
                        logger.warning(e.getMessage());
                        consoleHandler.close();
                        fileHandler.close();
                        logger.removeHandler(consoleHandler);
                        logger.removeHandler(fileHandler);
                        return;
                    }
                }

                Consumer newConsumer = new Consumer(newLogin, oldConsumer.getRole(), oldConsumer.getDateOfBirth(), oldConsumer.getDiscount());
                updated = consumerService.updateConsumerWithoutPassword(oldConsumer, newConsumer);
                if (updated) {
                    consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
                    FileHandler fileHandler1;
                    try {
                        fileHandler1 = new FileHandler(newLogin + "txt.");
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                    logger.removeHandler(fileHandler);
                    logger.addHandler(fileHandler1);
                    logger.info("Логин \"" + oldConsumer.getLogin() + "\" успешно изменен на \"" + newLogin + "\".");
                    consoleHandler.setFormatter(new MyFormatterRedForConsolle());
                    consoleHandler.close();
                    fileHandler.close();
                    logger.removeHandler(consoleHandler);
                    logger.removeHandler(fileHandler1);
                    System.out.println("Чтобы изменения вступили в силу, пользователю необходимо зайти в личный кабинет под новой учетной записью.\n");
                    if (!oldConsumer.getLogin().equals(myLogin)) {
                        mainMenu.start();
                    }
                } else {
                    logger.info("Логин \"" + oldConsumer.getLogin() + "\" не изменен.");
                    consoleHandler.close();
                    fileHandler.close();
                    logger.removeHandler(consoleHandler);
                    logger.removeHandler(fileHandler);
                }
                break;
            }
        }
    }

    private void updatePassword(Consumer oldConsumer, String myLogin) {
        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatterRedForConsolle());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        try {
            fileHandler = new FileHandler(myLogin + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        fileHandler.setFormatter(new MyFormatterForFile());
        logger.setUseParentHandlers(false);
        logger.addHandler(fileHandler);
        boolean updated;
        while (true) {
            System.out.println("Придумайте новый пароль, используйте не менее 8-ми и не более 16-ти символов:");
            System.out.print("=>");
            String newPassword;
            try {
                newPassword = enterPassword();
            } catch (IncorrectPasswordFormat e) {
                logger.warning(e.getMessage());
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }
            ConsumersRole myRole = consumerService.getConsumer(myLogin).getRole();
            if (!myRole.equals(ConsumersRole.ADMIN)) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Введите старый пароль:");
                System.out.print("=>");
                String password = scanner.nextLine();
                try {
                    consumerService.isChecked(password, oldConsumer.getLogin());
                } catch (NoSuchAlgorithmException | InvalidKeySpecException | PasswordIsNotEquals e) {
                    logger.warning(e.getMessage());
                    consoleHandler.close();
                    fileHandler.close();
                    logger.removeHandler(consoleHandler);
                    logger.removeHandler(fileHandler);
                    return;
                }
            }
            Consumer newConsumer = new Consumer(oldConsumer.getLogin(), newPassword, oldConsumer.getRole(), oldConsumer.getDateOfBirth(), oldConsumer.getDiscount());
            updated = consumerService.updateConsumer(oldConsumer, newConsumer);
            if (updated) {
                consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
                logger.info("Пароль успешно изменен.");
                consoleHandler.setFormatter(new MyFormatterRedForConsolle());
            } else {
                logger.info("Пароль не изменен.");
            }
            consoleHandler.close();
            fileHandler.close();
            logger.removeHandler(consoleHandler);
            logger.removeHandler(fileHandler);
            break;
        }
    }

    private void updateRole(Consumer oldConsumer, String myLogin) {

        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatterRedForConsolle());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        try {
            fileHandler = new FileHandler(myLogin + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        fileHandler.setFormatter(new MyFormatterForFile());
        logger.setUseParentHandlers(false);
        logger.addHandler(fileHandler);

        if (!consumerService.getConsumer(myLogin).getRole().equals(ConsumersRole.ADMIN)) {
            logger.warning("Доступно только администратору.");
            logger.removeHandler(consoleHandler);
            logger.removeHandler(fileHandler);
            return;
        }
        boolean updated;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Выберите новый уровень доступа:");
            System.out.println(Arrays.toString(ConsumersRole.class.getEnumConstants()));
            System.out.print("=>");
            String newRoleString = scanner.nextLine();
            ConsumersRole newRole;
            try {
                newRole = consumerService.getRoleFromString(newRoleString);
            } catch (NoSuchRole e) {
                logger.warning(e.getMessage());
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }
            ConsumersRole myRole = consumerService.getConsumer(myLogin).getRole();
            if (!myRole.equals(ConsumersRole.ADMIN)) {
                System.out.println("Введите пароль:");
                System.out.print("=>");
                String password = scanner.nextLine();
                try {
                    consumerService.isChecked(password, oldConsumer.getLogin());
                } catch (NoSuchAlgorithmException | InvalidKeySpecException | PasswordIsNotEquals e) {
                    logger.warning(e.getMessage());
                    consoleHandler.close();
                    fileHandler.close();
                    logger.removeHandler(consoleHandler);
                    logger.removeHandler(fileHandler);
                    return;
                }
            }
            Consumer newConsumer = new Consumer(oldConsumer.getLogin(), newRole, oldConsumer.getDateOfBirth(), oldConsumer.getDiscount());
            updated = consumerService.updateConsumerWithoutPassword(oldConsumer, newConsumer);
            if (updated) {
                consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
                logger.info("Уровень пользователя \"" + oldConsumer.getLogin() + "\" успешно изменен на \"" + newRole + "\".");
                consoleHandler.setFormatter(new MyFormatterRedForConsolle());
            } else {
                logger.info("Уровень пользователя \"" + oldConsumer.getLogin() + "\" успешно изменен на \"" + newRole + "\".");
            }
            consoleHandler.close();
            fileHandler.close();
            logger.removeHandler(consoleHandler);
            logger.removeHandler(fileHandler);
            break;
        }

    }

    private void updateDate(Consumer oldConsumer, String myLogin) {
        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatterRedForConsolle());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        try {
            fileHandler = new FileHandler(myLogin + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        fileHandler.setFormatter(new MyFormatterForFile());
        logger.setUseParentHandlers(false);
        logger.addHandler(fileHandler);
        boolean updated;

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите новую дату рождения в формате ГГГГ-ММ-ДД:");
            System.out.print("=>");
            String dateString;
            try {
                dateString = enterDate();
            } catch (IncorrectDateFormat e) {
                logger.warning(e.getMessage());
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }

            LocalDate newDate;
            try {
                newDate = LocalDate.parse(dateString);
            } catch (DateTimeParseException e) {
                logger.warning(e.getMessage());
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }


            ConsumersRole myRole = consumerService.getConsumer(myLogin).getRole();
            if (!myRole.equals(ConsumersRole.ADMIN)) {
                System.out.println("Введите пароль:");
                System.out.print("=>");
                String password = scanner.nextLine();
                try {
                    consumerService.isChecked(password, oldConsumer.getLogin());
                } catch (NoSuchAlgorithmException | InvalidKeySpecException | PasswordIsNotEquals e) {
                    logger.warning(e.getMessage());
                    consoleHandler.close();
                    fileHandler.close();
                    logger.removeHandler(consoleHandler);
                    logger.removeHandler(fileHandler);
                    return;
                }
            }
            Consumer newConsumer = new Consumer(oldConsumer.getLogin(), oldConsumer.getRole(), newDate, oldConsumer.getDiscount());
            updated = consumerService.updateConsumerWithoutPassword(oldConsumer, newConsumer);
            if (updated) {
                consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
                logger.info("Дата рождения успешно изменена на " + newDate + ".");
                consoleHandler.setFormatter(new MyFormatterRedForConsolle());
            } else {
                logger.info("Дата рождения не изменена.");
            }
            consoleHandler.close();
            fileHandler.close();
            logger.removeHandler(consoleHandler);
            logger.removeHandler(fileHandler);
            break;
        }
    }

    public void deleteConsumer() {
        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatterRedForConsolle());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите имя пользователя, которого необходимо удалить:");
            System.out.print("=>");
            String login = scanner.nextLine();
            try {
                fileHandler = new FileHandler(login + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            fileHandler.setFormatter(new MyFormatterForFile());
            logger.setUseParentHandlers(false);
            logger.addHandler(fileHandler);
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
            if (consumerService.getConsumer(login).getRole().equals(ConsumersRole.ADMIN)) {
                logger.warning("Невозможно удалить пользователя с правами администратора.");
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                return;
            } else {
                consumerService.deleteConsumer(consumerService.getConsumer(login));
                consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
                logger.info("Пользователь \"" + login + "\" успешно удалён.");
                consoleHandler.setFormatter(new MyFormatterRedForConsolle());
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            }
        }
    }
}
