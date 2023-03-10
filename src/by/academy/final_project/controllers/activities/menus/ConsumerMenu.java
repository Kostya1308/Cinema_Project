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

            System.out.println("?????????????? \"1\", ???????? ???? ???????????? ???????????????? ?????? ????????????????????????.");
            System.out.println("?????????????? \"2\", ???????? ???? ???????????? ???????????????? ????????????.");
            System.out.println("?????????????? \"3\", ???????? ???? ???????????? ???????????????? ?????????????? ??????????????.");
            System.out.println("?????????????? \"4\", ???????? ???? ???????????? ???????????????? ???????? ????????????????");
            System.out.print("=>");
            Scanner scanner = new Scanner(System.in);
            String result = scanner.nextLine();
            switch (result) {
                case "1" -> {
                    logger.removeHandler(consoleHandler);
                    logger.info("?????????????? ???????? ?????????????????????????? ??????????.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    updateName(oldConsumer, myLogin);
                }
                case "2" -> {
                    logger.removeHandler(consoleHandler);
                    logger.info("?????????????? ???????? ?????????????????????????? ????????????.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    updatePassword(oldConsumer, myLogin);
                }
                case "3" -> {
                    logger.removeHandler(consoleHandler);
                    logger.info("?????????????? ???????? ?????????????????????????? ???????????? ??????????????.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    updateRole(oldConsumer, myLogin);
                }
                case "4" -> {
                    logger.removeHandler(consoleHandler);
                    logger.info("?????????????? ???????? ?????????????????????????? ???????????? ???????? ????????????????.");
                    fileHandler.close();
                    logger.removeHandler(fileHandler);
                    updateDate(oldConsumer, myLogin);
                }
                default -> {
                    logger.warning("?????????????? ???????????????? ????????????????.");
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
            System.out.println("?????????????? ?????????? ?????? ????????????????????????:");
            System.out.print("=>");
            String newLogin = scanner.nextLine();
            try {
                consumerService.isSuchLoginExist(newLogin);
                logger.warning("???????????? ?????? ????????????.");
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                break;
            } catch (NoSuchLogin ee) {

                ConsumersRole myRole = consumerService.getConsumer(myLogin).getRole();
                if (!myRole.equals(ConsumersRole.ADMIN)) {
                    System.out.println("?????????????? ????????????:");
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
                    logger.info("?????????? \"" + oldConsumer.getLogin() + "\" ?????????????? ?????????????? ???? \"" + newLogin + "\".");
                    consoleHandler.setFormatter(new MyFormatterRedForConsolle());
                    consoleHandler.close();
                    fileHandler.close();
                    logger.removeHandler(consoleHandler);
                    logger.removeHandler(fileHandler1);
                    System.out.println("?????????? ?????????????????? ???????????????? ?? ????????, ???????????????????????? ???????????????????? ?????????? ?? ???????????? ?????????????? ?????? ?????????? ?????????????? ??????????????.\n");
                    if (!oldConsumer.getLogin().equals(myLogin)) {
                        mainMenu.start();
                    }
                } else {
                    logger.info("?????????? \"" + oldConsumer.getLogin() + "\" ???? ??????????????.");
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
            System.out.println("???????????????????? ?????????? ????????????, ?????????????????????? ???? ?????????? 8-???? ?? ???? ?????????? 16-???? ????????????????:");
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
                System.out.println("?????????????? ???????????? ????????????:");
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
                logger.info("???????????? ?????????????? ??????????????.");
                consoleHandler.setFormatter(new MyFormatterRedForConsolle());
            } else {
                logger.info("???????????? ???? ??????????????.");
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
            logger.warning("???????????????? ???????????? ????????????????????????????.");
            logger.removeHandler(consoleHandler);
            logger.removeHandler(fileHandler);
            return;
        }
        boolean updated;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("???????????????? ?????????? ?????????????? ??????????????:");
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
                System.out.println("?????????????? ????????????:");
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
                logger.info("?????????????? ???????????????????????? \"" + oldConsumer.getLogin() + "\" ?????????????? ?????????????? ???? \"" + newRole + "\".");
                consoleHandler.setFormatter(new MyFormatterRedForConsolle());
            } else {
                logger.info("?????????????? ???????????????????????? \"" + oldConsumer.getLogin() + "\" ?????????????? ?????????????? ???? \"" + newRole + "\".");
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
            System.out.println("?????????????? ?????????? ???????? ???????????????? ?? ?????????????? ????????-????-????:");
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
                System.out.println("?????????????? ????????????:");
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
                logger.info("???????? ???????????????? ?????????????? ???????????????? ???? " + newDate + ".");
                consoleHandler.setFormatter(new MyFormatterRedForConsolle());
            } else {
                logger.info("???????? ???????????????? ???? ????????????????.");
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
            System.out.println("?????????????? ?????? ????????????????????????, ???????????????? ???????????????????? ??????????????:");
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
                logger.warning("???????????????????? ?????????????? ???????????????????????? ?? ?????????????? ????????????????????????????.");
                consoleHandler.close();
                fileHandler.close();
                logger.removeHandler(consoleHandler);
                logger.removeHandler(fileHandler);
                return;
            } else {
                consumerService.deleteConsumer(consumerService.getConsumer(login));
                consoleHandler.setFormatter(new MyFormatterGreenForConsolle());
                logger.info("???????????????????????? \"" + login + "\" ?????????????? ????????????.");
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
