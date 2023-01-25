package by.academy.final_project.controllers;

import by.academy.final_project.controllers.accounts.menus.AdminAccountMenu;
import by.academy.final_project.controllers.accounts.menus.ManagerAccountMenu;
import by.academy.final_project.controllers.accounts.menus.UserAccountMenu;
import by.academy.final_project.controllers.activities.menus.ConsumerMenu;
import by.academy.final_project.controllers.activities.menus.EventMenu;
import by.academy.final_project.controllers.activities.menus.FilmMenu;
import by.academy.final_project.controllers.activities.menus.TicketMenu;
import by.academy.final_project.controllers.common.menus.LoginMenu;
import by.academy.final_project.controllers.common.menus.MainMenu;
import by.academy.final_project.controllers.common.menus.RegistrationMenu;
import by.academy.final_project.entities.*;
import by.academy.final_project.exceptions.*;
import by.academy.final_project.services.consumer.service.ConsumerService;
import by.academy.final_project.services.event.service.EventService;
import by.academy.final_project.services.film.service.FilmsService;
import by.academy.final_project.services.readers.service.FilesReaderService;
import by.academy.final_project.services.ticket.service.TicketService;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Menu {

    protected ConsumerService consumerService;
    protected FilmsService filmsService;
    protected EventService eventService;
    protected TicketService ticketService;
    protected FilesReaderService fileReaderService;

    protected LoginMenu loginMenu;
    protected MainMenu mainMenu;
    protected RegistrationMenu registrationMenu;

    protected UserAccountMenu userAccountMenu;
    protected ManagerAccountMenu managerAccountMenu;
    protected AdminAccountMenu adminAccountMenu;
    protected ConsumerMenu consumerMenu;

    protected TicketMenu ticketMenu;
    protected EventMenu eventMenu;
    protected FilmMenu filmMenu;

    protected FileHandler fileHandler;
    protected ConsoleHandler consoleHandler;

    public final Logger logger = Logger.getLogger(Menu.class.getSimpleName());

    public Menu(ConsumerService consumerService, FilmsService filmsService, EventService eventService, TicketService ticketService, FilesReaderService fileReaderService) {
        this.consumerService = consumerService;
        this.filmsService = filmsService;
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.fileReaderService = fileReaderService;
    }

    protected void isCorrectId(String id) throws IncorrectIdFormat {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(id);
        if (!matcher.matches()) {
            throw new IncorrectIdFormat("Некорректный формат. Требуется ввести число");
        }
    }

    protected void isCorrectLogin(String login) throws IncorrectLoginFormat {
        Pattern pattern = Pattern.compile("[А-Яа-яA-za-z]([А-Яа-яA-za-z0-9]+)?(-{1})?(\\s{1})?[А-Яа-яA-za-z0-9]+");
        Matcher matcher = pattern.matcher(login);
        if (!matcher.matches()) {
            throw new IncorrectLoginFormat("Login введён некорректно.\nДопускаются:\n - латиница;\n- кириллица;\n- цифры и дефис.\n" +
                    "Недопускаются:\n- два и более дефисов или пробелов подряд;\n- дефис или пробел в начале или в конце;\n" +
                    "- цифра, дефис или пробел в начале.");
        }
    }

    protected void isCorrectPassword(String password) throws IncorrectPasswordFormat {
        Pattern pattern = Pattern.compile(".{8,16}");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new IncorrectPasswordFormat("Пароль введён некорректно. Требуется ввести от 8-ми до 16-ти любых символов.");
        }
    }

    protected void isCorrectTime(String time) throws IncorrectTimeFormat {
        Pattern pattern = Pattern.compile("\\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(time);
        if (!matcher.matches()) {
            throw new IncorrectTimeFormat("Время введено неверно. Требуемый формат ЧЧ-ММ.");
        }
    }

    protected void isCorrectDate(String date) throws IncorrectDateFormat {
        Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Matcher matcher = pattern.matcher(date);
        if (!matcher.matches()) {
            throw new IncorrectDateFormat("Дата введена неверно. Требуемый формат ГГГГ-ДД-ММ.");
        }
    }

    protected void isCorrectYear(String year) throws IncorrectYearFormat {
        Pattern pattern = Pattern.compile("\\d{4}");
        Matcher matcher = pattern.matcher(year);
        if (!matcher.matches()) {
            throw new IncorrectYearFormat("Год введен неверно. Требуемый формат ГГГГ.");
        }
    }

    protected void isCorrectRawOrSeat(String rawAndSeat) throws IncorrectFormatSeat {
        Pattern pattern = Pattern.compile("\\d{1,2}");
        Matcher matcher = pattern.matcher(rawAndSeat);
        if (!matcher.matches()) {
            throw new IncorrectFormatSeat("Номер введён не верно. В зале кинотеатра 10 рядов и 23 места.");
        }
    }

    protected void isCorrectAge(String age) throws IncorrectIdFormat {
        Pattern pattern = Pattern.compile("\\d{1,2}");
        Matcher matcher = pattern.matcher(age);
        if (!matcher.matches()) {
            throw new IncorrectIdFormat("Некорректный формат. Требуется ввести число");
        }
    }

    protected void showSeatingPlan() {
        Event event = new Event();
        Ticket[][] tickets = eventService.getEvent(event).getTickets();
        System.out.println();
        System.out.println("                           СХЕМА ЗАЛА                       ");
        System.out.println(" _____________________________________________________________");

        for (int i = 0; i < tickets.length - 5; i++) {
            System.out.print("| ");
            for (int j = 0; j < tickets[i].length; j++) {
                    System.out.print(tickets[i][j].getSeatNumber() + " ");
            }
            System.out.print("|   ряд №" + tickets[i][0].getRawNumber());
            System.out.println();
        }
        System.out.println(" _____________________________________________________________");
        System.out.println(" ___________________________ ПРОХОД __________________________");

        for (int i = 5; i < tickets.length; i++) {
            System.out.print("| ");
            for (int j = 0; j < tickets[i].length; j++) {
                System.out.print(tickets[i][j].getSeatNumber() + " ");
            }
            System.out.print("|   ряд №" + tickets[i][0].getRawNumber());
            System.out.println();
        }
        System.out.println(" _____________________________________________________________");
        System.out.println();

    }

    protected Event enterEvent() throws NoSuchFilm, IncorrectDateFormat, IncorrectTimeFormat, DateTimeParseException{
        Event event;
        while (true) {
            System.out.println("Введите название фильма:");
            System.out.print("=>");
            Scanner scanner = new Scanner(System.in);
            String filmName = scanner.nextLine();
            filmsService.isFilmExist(filmName);

            System.out.println("Введите дату сеанса в формате ГГГГ-ММ-ДД:");
            System.out.print("=>");
            String eventDateString = scanner.nextLine();
            isCorrectDate(eventDateString);
            LocalDate eventDate = LocalDate.parse(eventDateString);

            System.out.println("Введите время сеанса в формате ЧЧ:ММ:");
            System.out.print("=>");
            String eventTimeString = scanner.nextLine();
            isCorrectTime(eventTimeString);
            LocalTime eventTime = LocalTime.parse(eventTimeString);

            event = new Event(filmsService.getFilm(filmName), eventDate, eventTime);
            break;
        }
        return event;
    }

    public Film enterFilm() throws IncorrectYearFormat, NoSuchGenre, IncorrectIdFormat, DateTimeParseException {
        Film film;
        while (true) {
            System.out.println("Введите название фильма:");
            Scanner scanner = new Scanner(System.in);
            System.out.print("=>");
            String filmName = scanner.nextLine();

            System.out.println("Введите страну фильма");
            System.out.print("=>");
            String country = scanner.nextLine();

            System.out.println("Введите год выхода фильма в формате ГГГГ");
            System.out.print("=>");
            String releaseYearString;

            releaseYearString = enterYear();

            Year releaseYear = Year.parse(releaseYearString);

            System.out.println("Выберите жанр фильма c учётом регистра:");
            System.out.println(Arrays.stream(FilmsGenre.class.getEnumConstants()).toList());
            System.out.print("=>");
            String genreString = scanner.nextLine();
            FilmsGenre genre;

            genre = filmsService.getGenreFromString(genreString);

            System.out.println("Введите возрастное ограничение фильма:");
            System.out.print("=>");
            String ageLimitString;

            ageLimitString = enterAge();
            int ageLimit = Integer.parseInt(ageLimitString);

            film = new Film(filmName, country, releaseYear, genre, ageLimit);
            break;
        }
        return film;
    }

    public String enterPassword() throws IncorrectPasswordFormat {
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        isCorrectPassword(password);
        return password;
    }

    public String enterAge() throws IncorrectIdFormat {
        Scanner scanner = new Scanner(System.in);
        String age = scanner.nextLine();
        isCorrectAge(age);
        return age;
    }

    public String enterLogin() throws IncorrectLoginFormat {
        Scanner scanner = new Scanner(System.in);
        String login = scanner.nextLine();
        isCorrectLogin(login);
        return login;
    }

    public String enterDate() throws IncorrectDateFormat {
        Scanner scanner = new Scanner(System.in);
        String dateString = scanner.nextLine();
        isCorrectDate(dateString);
        return dateString;
    }

    public String enterYear() throws IncorrectYearFormat {
        Scanner scanner = new Scanner(System.in);
        String yearString = scanner.nextLine();
        isCorrectYear(yearString);
        return yearString;
    }


}
