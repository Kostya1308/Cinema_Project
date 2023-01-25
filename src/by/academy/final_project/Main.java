package by.academy.final_project;

import by.academy.final_project.controllers.common.menus.MainMenu;

import by.academy.final_project.entities.ConsumersRole;
import by.academy.final_project.repository.consumer_repository.ConsumerRepository;
import by.academy.final_project.repository.consumer_repository.ConsumerRepositoryImpl;
import by.academy.final_project.repository.event_repository.EventRepository;
import by.academy.final_project.repository.event_repository.EventRepositoryImpl;
import by.academy.final_project.repository.film_repository.FilmRepository;
import by.academy.final_project.repository.film_repository.FilmRepositoryImpl;
import by.academy.final_project.repository.readers.FilesReader;
import by.academy.final_project.repository.readers.FilesReaderImp;
import by.academy.final_project.repository.ticket.repository.TicketRepository;
import by.academy.final_project.repository.ticket.repository.TicketRepositoryImpl;
import by.academy.final_project.services.consumer.service.ConsumerService;
import by.academy.final_project.services.consumer.service.ConsumerServiceImpl;
import by.academy.final_project.services.event.service.EventService;
import by.academy.final_project.services.event.service.EventServiceImpl;
import by.academy.final_project.services.film.service.FilmServiceImpl;
import by.academy.final_project.services.film.service.FilmsService;
import by.academy.final_project.services.readers.service.FilesReaderService;
import by.academy.final_project.services.readers.service.FilesReaderServiceImpl;
import by.academy.final_project.services.ticket.service.TicketService;
import by.academy.final_project.services.ticket.service.TicketServiceImpl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Main {

    public static void main(String[] args){

        ConsumerRepository consumerRepository = new ConsumerRepositoryImpl();
        ConsumerService consumerService = new ConsumerServiceImpl(consumerRepository);
        FilmRepository filmRepository = new FilmRepositoryImpl();
        FilmsService filmsService = new FilmServiceImpl(filmRepository);
        EventRepository eventRepository = new EventRepositoryImpl();
        EventService eventService = new EventServiceImpl(eventRepository);
        TicketRepository ticketRepository = new TicketRepositoryImpl();
        TicketService ticketService = new TicketServiceImpl(ticketRepository);
        FilesReader fileReader = new FilesReaderImp();
        FilesReaderService fileReaderService = new FilesReaderServiceImpl(fileReader);
        MainMenu mainMenu = new MainMenu(consumerService, filmsService, eventService, ticketService, fileReaderService);

        mainMenu.start();




    }
}
